import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Player extends Thread {
	private String login;
	private String ip;
	private boolean ghost;
	private Game game;
	private Socket socket;
	private boolean connected;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public Player(String login, Socket socket, boolean ghost, ObjectInputStream in, ObjectOutputStream out){
		this.login = login;
		this.socket = socket;
		this.ghost = ghost;
		game = null;
		connected = true;

		this.in = in;
		this.out = out;
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
			out.writeObject("Test de message");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run(){

		Object message;

		try {

			while(connected && !socket.isClosed())
			{

				message = in.readObject();

				System.out.println(login +" a envoyé la commande : " + message);
				processTypeMessage(message);
			}

			in.close();
			Server.removePlayer(this);

		} catch (IOException e1) {
			System.out.println("Connexion à " + login + " perdue.");
			connected = false;
			Server.removePlayer(this);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void sendResult(boolean result)
	{
		try {
			if(result)
			{

				out.writeObject("success");
				out.flush();
			}
			else
			{
				out.writeObject("fail");
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void processTypeMessage(Object message)
	{
		if(message instanceof String)
		{
			processStringMessage((String) message);
		}
		else if(message instanceof DrawingData)
		{
			processDessinMessage((DrawingData) message);
		}
	}

	public void processDessinMessage(DrawingData data)
	{
		System.out.println("Nouvelle coordonnée : " + data.x + " " + data.y);
	}

	public void processStringMessage(String message){
		if(message != null && !message.equals(""))
		{
			if(message.contains("%"))
			{
				String[] result = message.split("%");

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