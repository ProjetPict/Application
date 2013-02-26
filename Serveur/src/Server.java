import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread{

	private static ArrayList<Game> games;
	private static ArrayList<Player> players;
	private ServerSocket serverSocket;
	private Socket socket;

	public Server(){
		games = new ArrayList<Game>();
		players = new ArrayList<Player>();

		try {
			serverSocket = new ServerSocket(8448);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run()
			{
				System.out.println(games.size() + " parties sont en cours.");
				if(players.size() >=1)
				{
					players.get(0).testMessage();
				}
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

	public static Game createGame(Player creator){
		
		Game game = null;

		if(creator.getGame() == null)
		{
			game = new Game(creator, null);
			games.add(game);
			game.start();
		}
		
		return game;
	}

	public static void createPlayer(String login, Socket socket)
	{
		Player player = new Player(login, socket, false);
		players.add(player);
		player.start();
	}

	public static void removePlayer(Player player)
	{
		if(player.getGame() != null)
			player.getGame().removePlayer(player);
		players.remove(player);
		System.out.println(player.getLogin() + " s'est deconnecté.");
	}
	
	public static void removeGame(Game game)
	{
		System.out.println("Une partie a été stoppée");
		games.remove(game);
	}
}
