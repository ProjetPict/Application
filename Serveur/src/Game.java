import java.util.ArrayList;


public class Game extends Thread{
	
	private ArrayList<Player> players;
	private String password;
	
	public Game(Player creator, String password){
		players = new ArrayList<Player>();
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

}
