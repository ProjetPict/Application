import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Player extends Thread {
	private String login;
	private String ip;
	private boolean ghost;
	private Game game;
	private Socket socket;
	private boolean connected;

	public Player(String login, Socket socket, boolean ghost){
		this.login = login;
		this.socket = socket;
		this.ghost = ghost;
		game = null;
		connected = true;
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

	public void testMessage()
	{
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.println("Test de message");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run(){
		
		BufferedReader in;
		String message;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while(connected)
			{
				message = in.readLine();
				System.out.println("Nouvelle commande de " + login +" : " + message);
				processMessage(message);
			}

		} catch (IOException e) {
			System.out.println("Connexion à " + login + " perdue.");
			connected = false;
			Server.removePlayer(this);
		}
	}
	
	public void processMessage(String message){
		if(message.equals("creategame"))
		{
			game = Server.createGame(this);
		}
		else if(message.equals("quit"))
		{
			connected = false;
			
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				out.println("success");
				out.flush();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Server.removePlayer(this);
		}
	}

}