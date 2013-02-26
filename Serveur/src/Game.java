import java.util.ArrayList;

public class Game extends Thread{
	
	private ArrayList<Player> players;
	private String password;
	private int jMax;
	private boolean running;
	
	public Game(Player creator, String password){
		players = new ArrayList<Player>();
		players.add(creator);
		this.password = password; 	//vide si publique 
		jMax = 10;					//valeur par défaut ?
	}
	
	public Game(Player creator, String password, int jMax){
		players = new ArrayList<Player>();
		playerJoin(creator);
		this.password = password; 	//vide si publique 
		this.jMax = jMax;
	}
	
	public void run() {
		running = true;
		if(players.size() > 0)
			System.out.println("Test partie de " + players.get(0).getLogin());
		while(running){
			
			try {
				sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block 
				e.printStackTrace();
			}
		}
	}
	
	public boolean isPrivate(){
		if(password != null)
			return true;
		else
			return false;
	}

	public String getPassword(){
		return password;
	}

	public int getJMax(){
		return jMax;
	}
	
	public boolean playerJoin(Player p){
		if(players.size() < jMax){
			players.add(p);
			return true; //joueur ajouté
		}
		return false; //joueurs max atteint
	}
	
	public void removePlayer(Player player){
		players.remove(player);
		if(players.size() <= 0)
		{
			running = false;
			Server.removeGame(this);
		}
	}

}
