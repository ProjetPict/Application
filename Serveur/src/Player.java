import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Cette classe est un thread qui permet de maintenir la connexion avec un joueur et d'intercepter
 * les donn�es qu'il envoie depuis son socket.
 * @author Jerome
 *
 */
public class Player extends Thread {

	private String login;
	private boolean ghost;
	private Game game;
	private Socket socket;
	private boolean connected;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	
	/**
	 * @param login
	 * @param socket
	 * @param ghost
	 * @param in
	 * @param out
	 */
	public Player(String login, Socket socket, boolean ghost, ObjectInputStream in, ObjectOutputStream out){
		this.login = login;
		this.socket = socket;
		this.ghost = ghost;
		this.in = in;
		this.out = out;
		game = null;
		connected = true;
	}

	
	/**
	 * 
	 * @param game
	 */
	public void setGame(Game game){
		this.game = game;
	}

	
	/**
	 * 
	 * @return L'instance de Game contenue dans Player
	 */
	public Game getGame(){
		return game;
	}

	
	/**
	 * 
	 * @param ghost
	 */
	public void setGhost(boolean ghost){
		this.ghost = ghost;
	}

	
	/**
	 * 
	 * @return La valeur de ghost. (true si le joueur est un joueur fant�me, false sinon)
	 */
	public boolean getGhost(){
		return ghost;
	}

	
	/**
	 * 
	 * @return Le login du joueur
	 */
	public String getLogin(){
		return login;
	}

	
	/**
	 * 
	 * @return L'output stream du socket du joueur.
	 */
	public ObjectOutputStream getOutput(){
		return out;
	}


	/**
	 * Surcharge de la fonction run() de Thread. On boucle � l'infini tant
	 * que la connexion n'est pas coup�e, et on r�cup�re les donn�es que le
	 * joueur envoie.
	 */
	public void run(){

		Object message;

		try {

			while(connected && !socket.isClosed())
			{
				message = in.readObject();
				System.out.println(login +" a envoy� la commande : " + message);
				processTypeMessage(message);
			}

			in.close();
			Server.removePlayer(this);

		} catch (IOException e1) {
			System.out.println("Connexion � " + login + " perdue.");
			connected = false;
			Server.removePlayer(this);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Cette m�thode envoie "success" ou "fail" au client.
	 * @param result
	 */
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
			e.printStackTrace();
		}
	}


	/**
	 * Cette m�thode d�termine le type du message pass� en param�tre, et appelle la fonction
	 * de traitement correspondante.
	 * @param message
	 */
	public void processTypeMessage(Object message)
	{
		if(message instanceof String){
			processStringMessage((String) message);
		}
		else if(message instanceof DrawingData){
			processDessinMessage((DrawingData) message);
		}
	}


	/**
	 * Envoie les donn�es � la Game
	 * @param data
	 */
	public void processDessinMessage(DrawingData data)
	{
		System.out.println("Nouvelle coordonn�e : " + data.x + " " + data.y);
		game.sendData(data, this);
		sendResult(data!=null);
	}


	/**
	 * Analyse la commande contenue dans le message et l'ex�cute.
	 * @param message
	 */
	public void processStringMessage(String message){
		if(message != null && !message.equals(""))
		{
			if(message.contains("%"))
			{
				String[] result = message.split("%"); //Le caract�re % est le d�limiteur entre les diff�rents champs de la String
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