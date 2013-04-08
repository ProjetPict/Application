package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import localDatabase.DbConnection;
import localDatabase.ServerDatabase;
import socketData.GameInfo;
import socketData.GameList;
import view.Window;


/**
 * Cette classe gère les nouvelles connexions, ainsi que
 * les joueurs et les parties
 *
 */
public class Server extends Thread{
	
	final static int MAX_PLAYER = 10;
	final static int TURNS = 5;
	private static ArrayList<Player> players;
	private static Map<String, Game> games;
	private ServerSocket serverSocket;
	private Socket socket;
	private static Window windowServer;
	private Runtime runtime;
	private static DbConnection servDbConnec;
	private static ServerDatabase servDbLocale;
	private long launchTimeCalc;
	private static boolean launchState;
	private static boolean importantProcess;

	public Server(boolean state) throws Exception {
		launchState = state;
		players = new ArrayList<Player>();
		games = new Hashtable<String, Game>();
		servDbConnec = new DbConnection();
		servDbLocale = new ServerDatabase(servDbConnec);
		
		int tentatives = 1;
		boolean success = false;
		
		if(launchState) {
			windowServer = new Window(this);
			windowServer.setVisible(true);
		}
		importantProcess = true;
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
			throw new Exception("Echec lors de la connexion à la base de données");
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
		importantProcess = false;
	}


	/**
	 * Surcharge de la fonction run() de Thread. On boucle à l'infini, et quand quelqu'un se connecte,
	 * on créé et on démarre une nouvelle Connection
	 */
	public void run() {

		Timer timer = new Timer();
		Timer autoSave = new Timer();
		Timer autoLoad = new Timer();
		Timer launchTime = new Timer();
		launchTimeCalc = 0;
		//On actualise toutes les 1 seconde les graphiques
		timer.schedule(new TimerTask() {
			public void run() {
				runtime = Runtime.getRuntime();
				if(launchState)
					windowServer.updateGraph(players.size(),(runtime.totalMemory()-runtime.freeMemory())/1024, games.size());
			}
		}, 0, 1000);
		// Sauvegarde automatique de l'historique dans la base de données toutes les 10 minutes
		autoSave.schedule(new TimerTask() {
			public void run() {
				importantProcess = true;
				writeIn("\n# Sauvegarde automatique de l'historique dans la base de données...");
				if(servDbLocale.saveDatabase())
					writeIn("Terminé !");
				else
					writeIn("Echec !");
				importantProcess = false;
			}
		}, 60000, 60000);
		// Chargement automatique de la base de données toutes les 5 minutes
		autoLoad.schedule(new TimerTask() {
			public void run() {
				importantProcess = true;
				writeIn("\n# Chargement de la nouvelle base de données...");
				servDbLocale.reloadDatabase();
				writeIn("Terminé !");
				importantProcess = false;
			}
		}, 300000, 300000);
		launchTime.schedule(new TimerTask() {
			Date current;
			public void run() {
				launchTimeCalc++;
				current = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss" );
				if(launchState)
					windowServer.setTimes(launchTimeCalc,dateFormat.format(current));
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
	 * @param creator Joueur ayant créé la partie
	 * @param name Nom de la partie
	 * @param password Mot de passe de la partie
	 * @param pMax Nombre de joueur max
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
	 * @param login Nom d'utilisateur
	 * @param socket
	 * @param connec
	 */
	public static void createPlayer(String login, Socket socket, Connection connec)
	{
		Player player = new Player(login, socket, false, connec.getInput(), connec.getOutput());
		players.add(player);
		player.start();
	}

	
	public static ServerDatabase getDB(){
		return servDbLocale;
	}

	/**
	 * Supprime un joueur
	 * @param player Joueur à supprimer
	 */
	public static void removePlayer(Player player)
	{
		if(player.getGame() != null)
		{
			player.getGame().removePlayer(player);
			player.setGame(null);
		}
		
		players.remove(player);
		writeIn(player.getLogin() + " s'est deconnecté.");
	}


	/**
	 * Supprime une partie
	 * @param game Partie à supprimer
	 */
	public static void removeGame(Game game)
	{
		writeIn(game.getGameName() + " a été stoppée.");
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
	 * @param player Joueur à ajouter
	 * @param name Nom de la partie
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
					writeIn(player.getLogin() + " a rejoint la partie " + game.getGameName());
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

	/**
	 * Permet de définir si l'on écrit dans la console (mode fenetre) ou dans la console (mode console)
	 * @param s String à écrire
	 */
	public static void writeIn(String s) {
		if(launchState) {
			if(importantProcess)
				windowServer.writeAnnonce(s);
			else
				windowServer.writeAnnonce("\n"+s);
		} else {
			System.out.println(s);
		}
	}	
	
	public boolean getLaunchState() {
		return launchState;
	}
	
	public Window getWindow() {
		return windowServer;
	}
}
