import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import socketData.GameInfo;
import socketData.GameList;

/**
 * Cette classe gère les nouvelles connexions, ainsi que
 * les joueurs et les parties
 * @author Jerome
 *
 */
public class Server extends Thread{
	final static int MAX_PLAYER = 10;
	private static ArrayList<Player> players;
	private static Map<String, Game> games;
	private ServerSocket serverSocket;
	private Socket socket;

	public Server(){
		players = new ArrayList<Player>();
		games = new Hashtable<String, Game>();
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

		//On affiche des infos toutes les 10 secondes
		timer.schedule(new TimerTask() {
			public void run()
			{
				System.out.println(games.size() + " parties sont en cours.");
				System.out.println(players.size() + " joueurs connectés.");
			}
		}, 0, 10000);

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
	public static Game createGame(Player creator, String name, String password, int pMax){

		Game game = null;

		if(creator.getGame() == null && name.length() > 3)
		{
			if(!games.containsKey(name))
			{
				if(pMax > 1 && pMax < 25)
					game = new Game(creator, name, password, pMax);
				else
					game = new Game(creator, name, password);
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
			player.getGame().removePlayer(player);
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

			gl.games.add(new GameInfo(e.getKey().toString(), g.getNbPlayers(), g.getJMax(), g.isPrivate(), g.isStarted()));
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
			if(password.equals(game.getPassword()))
			{
				result = game.addPlayer(player);
				System.out.println(player.getLogin() + " a rejoint la partie " + game.getGameName());
				if(result)
					player.setGame(game);
			}

		}

		return result;
	}
}
