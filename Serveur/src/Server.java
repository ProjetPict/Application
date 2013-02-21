import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Server extends Thread{

	private ArrayList<Game> games;
	private ArrayList<Player> players;

	public Server(){
		games = new ArrayList<Game>();
		players = new ArrayList<Player>();

		players.add(new Player("Test", "0", false));
		games.add(new Game(players.get(0), null));
	}

	public void run() {
		games.get(0).start();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run()
			{
				System.out.println(games.size() + " parties sont en cours.");
			}
		}, 0, 10000);

		while(true){


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
}
