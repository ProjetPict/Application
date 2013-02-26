import java.util.ArrayList;

public class Game extends Thread{
	
	private ArrayList<Player> players;
	private String password;
	private int jMax;
	
	public Game(Player creator, String password){
		players = new ArrayList<Player>();
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
	
		while(true){
			//System.out.println("Test partie");
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
	
	public boolean playerLeave(Player p){
		return players.remove(p); //true si p trouvé et supprimé
	}

}
