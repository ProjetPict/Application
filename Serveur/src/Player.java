
public class Player extends Thread{
	private String login;
	private String ip;
	private boolean ghost;
	private Game game;
	
	public Player(String login, String ip, boolean ghost){
		this.login = login;
		this.ip = ip;
		this.ghost = ghost;
		game = null;
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public Game getGame(){
		return game;
	}
	
	public void setGhost(boolean ghost){
		this.ghost = ghost;
	}
	
	public boolean getGhost(){
		return ghost;
	}
	
	public String getLogin(){
		return login;
	}
	
	public String getIp(){
		return ip;
	}

}
