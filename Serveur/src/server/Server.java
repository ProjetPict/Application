package server;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import localDatabase.DbConnection;
import localDatabase.ServerDatabase;

import socketData.GameInfo;
import socketData.GameList;
import view.Fenetre;

/**
 * Cette classe gère les nouvelles connexions, ainsi que
 * les joueurs et les parties
 * @author Jerome
 *
 */
public class Server extends Thread{
	
	final static int MAX_PLAYER = 10;
	final static int TURNS = 5;
	private static ArrayList<Player> players;
	private static Map<String, Game> games;
	private ServerSocket serverSocket;
	private Socket socket;
	private static Fenetre fenetre;
	private Runtime runtime;
	private static DbConnection servDbConnec;
	private static ServerDatabase servDbLocale;
	private long launchTimeCalc;
	private static boolean launchState;

	public Server(boolean state) throws Exception{
		launchState = state;
		players = new ArrayList<Player>();
		games = new Hashtable<String, Game>();
		servDbConnec = new DbConnection();
		servDbLocale = new ServerDatabase(servDbConnec);
		
		int tentatives = 1;
		boolean success = false;
		
		if(launchState) {
			fenetre = new Fenetre(this);
			fenetre.setVisible(true);
		}
		writeIn("--------------------------------------------------------------------------------------------\nSERVEUR DRAWVS\nChristopher CACCIATORE, Matthieu DOURIS, Jérôme PORT, Quentin POUSSIER, Nicolas SPAGNULO\n--------------------------------------------------------------------------------------------\n");
		writeIn("> Connexion à la base de données...");
		while(tentatives<5 && !success) {
			if(servDbConnec.connectDatabase())
				success=true;
			else
				writeIn("Echec ("+tentatives+"/5)\n> Nouvelle tentative de connexion à la base de données...");
			tentatives++;
		}
		if(!success) {
			writeIn("Echec (5/5)\n> Veuillez vérifier les paramètres de connexion avant de relancer le serveur.\n");
			//throw new Exception("Echec lors de la connexion à la base de données");
		} else {
			tentatives = 0;
			success = false;
			writeIn("Terminé !\n> Préparation de la base de données...");
			while(tentatives<5 && !success) {
				if(servDbLocale.loadDatabase())
					success=true;
				else
					writeIn("Echec ("+tentatives+"/5)\n> Nouvelle tentative de récupération de la base de données...");
				tentatives++;
			}
			if(success)
				writeIn("Terminé !\n> Le serveur est maintenant fonctionnel.\n--------------------------------------------------------------------------------------------");
			else {
				writeIn("Echec (5/5)\n> Veuillez vérifier l'état de la base de données avant de relancer le serveur.\n");
				//throw new Exception("Echec lors de la récupération de la base de données");
			}
		}
		try {
			serverSocket = new ServerSocket(8448);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Surcharge de la fonction run() de Thread. On boucle à l'infini, et quand quelqu'un se connecte,
	 * on créé et on démarre une nouvelle Connection
	 */
	public void run() {

		Timer timer = new Timer();
		Timer autoSave = new Timer();
		Timer launchTime = new Timer();
		launchTimeCalc = 0;
		//On actualise toutes les 1 seconde les graphiques
		timer.schedule(new TimerTask() {
			public void run() {
				runtime = Runtime.getRuntime();
				if(launchState)
					fenetre.updateGraph(players.size(),(runtime.totalMemory()-runtime.freeMemory())/1024, games.size());
			}
		}, 0, 1000);
		// Sauvegarde automatique de l'historique dans la base de données toutes les 10 minutes
		autoSave.schedule(new TimerTask() {
			public void run() {
				writeIn("\n# Sauvegarde automatique de l'historique dans la base de données...");
				servDbLocale.saveDatabase();
				writeIn("Terminé !");
			}
		}, 600000, 600000);
		launchTime.schedule(new TimerTask() {
			Date current;
			public void run() {
				launchTimeCalc++;
				current = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss" );
				if(launchState)
					fenetre.setTimes(launchTimeCalc,dateFormat.format(current));
			}
		},0,1000);
		
		while(true){
			try {
				socket = serverSocket.accept();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Connection connec = new Connection(socket);
			connec.start();

			try {
				sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * Instancie une nouvelle partie
	 * @param creator
	 * @param name
	 * @param password
	 * @param pMax
	 * @return La partie créée
	 */
	public static Game createGame(Player creator, String name, String password, int pMax, 
			int turns, int difficulty){

		Game game = null;

		if(creator.getGame() == null && name.length() > 3)
		{
			if(!games.containsKey(name))
			{
				
				game = new Game(creator, name, password, pMax, turns, difficulty);
			
				games.put(name, game);
				game.start();
			}
		}
		return game;
	}


	/**
	 * Instancie un nouveau joueur
	 * @param login
	 * @param socket
	 * @param connec
	 */
	public static void createPlayer(String login, Socket socket, Connection connec)
	{
		Player player = new Player(login, socket, false, connec.getInput(), connec.getOutput());
		players.add(player);
		player.start();
	}


	/**
	 * Supprime un joueur
	 * @param player
	 */
	public static void removePlayer(Player player)
	{
		if(player.getGame() != null)
		{
			player.getGame().removePlayer(player);
			player.setGame(null);
		}
		
		players.remove(player);
		System.out.println(player.getLogin() + " s'est deconnecté.");
	}


	/**
	 * Supprime une partie
	 * @param game
	 */
	public static void removeGame(Game game)
	{
		System.out.println(game.getGameName() + " a été stoppée.");
		game.stopGame();
		games.remove(game.getGameName());
	}

	/**
	 * 
	 * @return La liste des parties existantes
	 */
	public static GameList getGames()
	{
		GameList gl = new GameList();
		Iterator<Entry<String, Game>> it = games.entrySet().iterator();
		Entry<String, Game> e;
		Game g;

		while(it.hasNext())
		{
			e = it.next();
			g = e.getValue();

			gl.games.add(new GameInfo(e.getKey().toString(), g.getNbPlayers(), 
					g.getJMax(), g.isPrivate(), g.isStarted(), g.getDifficulty()));
		}

		return gl;
	}


	/**
	 * Ajoute un joueur à une partie
	 * @param player
	 * @param name
	 * @return True si l'opération est réussie, false sinon
	 */
	public static boolean joinGame(Player player, String name, String password)
	{
		boolean result = false;

		if(games.containsKey(name) && player.getGame() == null)
		{
			Game game = games.get(name);

			try{
				if(game.getPassword() == null || password.equals(game.getPassword()))
				{
					result = game.addPlayer(player);
					System.out.println(player.getLogin() + " a rejoint la partie " + game.getGameName());
					if(result)
						player.setGame(game);
					else
					    player.sendResult("gamefull");
				}
				else
					player.sendResult("wrongpassword");
			}catch(Exception e)
			{
				player.sendResult("wrongpassword");
				return false;
			}

		}

		return result;
	}
	
	public static ServerDatabase getDbInfos() {
		return servDbLocale;
	}
	
	public static DbConnection getDbConnInfos() {
		return servDbConnec;
	}
	
	public static void writeIn(String s) {
		if(launchState)
			fenetre.writeAnnonce(s);
		else
			System.out.println(s);
	}
	
	
}
