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
	private BufferedReader in;
	private PrintWriter out;

	public Player(String login, Socket socket, boolean ghost){
		this.login = login;
		this.socket = socket;
		this.ghost = ghost;
		game = null;
		connected = true;

		try {
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		out.println("Test de message");
		out.flush();
	}

	public void run(){

		String message;

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while(connected && !socket.isClosed())
			{
				message = in.readLine();
				System.out.println(login +" a envoyé la commande : " + message);
				processMessage(message);
			}

			in.close();
			Server.removePlayer(this);

		} catch (IOException e1) {
			System.out.println("Connexion à " + login + " perdue.");
			connected = false;
			Server.removePlayer(this);
		}


	}
	
	public void sendResult(boolean result)
	{
		if(result)
		{
			out.println("success");
			out.flush();
		}
		else
		{
			out.println("fail");
			out.flush();
		}
	}

	public void processMessage(String message){
		if(message != null)
		{
			if(message.contains("="))
			{
				String[] result = message.split("=");

				if(result.length >= 2){
					if(result[0].equals("creategame"))
					{
						game = Server.createGame(this, result[1], 0);
						sendResult(game!=null);
					}
					else if(result[0].equals("joingame"))
					{
						sendResult(Server.joinGame(this, result[1]));
					}	
				}

			}
			else 
			{
				if(message.equals("quit"))
				{
					connected = false;

					try {

						sendResult(true);
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					sendResult(false);
				}
			}
		}
	}

}