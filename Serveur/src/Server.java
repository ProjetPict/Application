import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Server extends Thread{

	private ArrayList<Game> games;
	private ArrayList<Player> players;

	private ServerSocket serverSocket;
	private Socket socket;

	public Server(){
		games = new ArrayList<Game>();
		players = new ArrayList<Player>();
		try {
			serverSocket = new ServerSocket(8448);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		players.add(new Player("Test", "0", false));
		games.add(new Game(players.get(0), null));
	}

	public void run() {
		games.get(0).start();
		//connection.start();
		try {
			System.out.println("L'adresse locale est : "+InetAddress.getLocalHost() );
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run()
			{
				System.out.println(games.size() + " parties sont en cours.");
			}
		}, 0, 10000);

		while(true){
			try {
				socket = serverSocket.accept();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Un client essaye de se connecter");

			Connection connec = new Connection(this, socket);
			connec.start();

			try {
				sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void createGame(Player creator){
		if(creator.getGame() == null)
			games.add(new Game(players.get(0), null));
	}

	public void createPlayer(String login, Socket socket)
	{
		//players.add(new Player())
	}
}