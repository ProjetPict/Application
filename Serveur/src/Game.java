import java.util.ArrayList;

public class Game extends Thread{
	
	private ArrayList<Player> players;
	private String name;
	private String password;
	private int pMax;
	private boolean running;
	
	public Game(Player creator, String name, String password){
		players = new ArrayList<Player>();
		this.name = name;
		players.add(creator);
		this.password = password; 	//vide si publique 
		pMax = 10;					//valeur par défaut ?
	}
	
	public Game(Player creator, String password, int pMax){
		players = new ArrayList<Player>();
		players.add(creator);
		this.password = password; 	//vide si publique 
		this.pMax = pMax;
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
		return pMax;
	}
	
	public boolean addPlayer(Player p){
		if(players.size() < pMax && !players.contains(p)){
			players.add(p);
			return true; //joueur ajouté
		}
		return false; //joueurs max atteint
	}
	
	public void removePlayer(Player player){
		players.remove(player);
		System.out.println(player.getLogin() + " a quitté la partie " + name);
		if(players.size() <= 0)
		{
			running = false;
			Server.removeGame(this);
		}
	}
	
	public String getGameName()
	{
		return name;
	}

}
