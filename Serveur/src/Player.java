import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import socketData.AnswerCommand;
import socketData.Command;
import socketData.CreateJoinCommand;
import socketData.DrawingData;

/**
 * Cette classe est un thread qui permet de maintenir la connexion avec un joueur et d'intercepter
 * les données qu'il envoie depuis son socket.
 * @author Jerome
 *
 */
public class Player extends Thread {

	private String login;
	private boolean ghost;
	private Game game;
	private Socket socket;
	private boolean connected;
	private boolean drawing;
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
		drawing = false;
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
	 * @return La valeur de ghost. (true si le joueur est un joueur fantôme, false sinon)
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
	 * 
	 * @return True si le joueur dessine, false sinon
	 */
	public boolean getDrawing(){
		return drawing;
	}


	/**
	 * On change la valeur de drawing, et on envoie une commande au client pour prendre ce changement en compte
	 * @param drawing
	 */
	public void setDrawing(boolean drawing)
	{
		this.drawing = drawing;
		String result = "";
		if(drawing){
			result = "startdraw";
		}
		else
		{
			result = "stopdraw";
		}

		try {
			out.writeObject(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}




	/**
	 * Surcharge de la fonction run() de Thread. On boucle à l'infini tant
	 * que la connexion n'est pas coupée, et on récupère les données que le
	 * joueur envoie.
	 */
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



	/**
	 * Cette méthode envoie "success" ou "fail" au client.
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

	public void sendResult(String result)
	{
		try {

			out.writeObject(result);
			out.flush();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Cette méthode détermine le type du message passé en paramètre, et appelle la fonction
	 * de traitement correspondante.
	 * @param message
	 */
	public void processTypeMessage(Object message)
	{
		if(message instanceof DrawingData){
			processDessinMessage((DrawingData) message);
		}
		else if(message instanceof CreateJoinCommand)
		{
			processCreateJoinMessage((CreateJoinCommand) message);
		}
		else if(message instanceof AnswerCommand)
		{
			processAnswerMessage((AnswerCommand) message);
		}
		else if(message instanceof Command)
		{
			processCommandMessage((Command) message);
		}
	}



	/**
	 * Analyse une commande simple
	 * @param message
	 */
	private void processCommandMessage(Command message) {

		if(message.command.equals("quit"))
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
		else if(message.command.equals("startgame"))
		{
			if(game!=null)
			{
				sendResult(game.startGame());
			}
			else
			{
				sendResult(false);
			}
		}
		else if(message.command.equals("getlist"))
		{
			try {
				out.writeObject(Server.getGames());
				out.flush();
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



	/**
	 * Analyse un message de reponse
	 * @param message
	 */
	private void processAnswerMessage(AnswerCommand message) {
		// TODO Auto-generated method stub

	}



	/**
	 * Analyse une commande de type creategame ou joingame
	 * @param message
	 */
	private void processCreateJoinMessage(CreateJoinCommand message) 
	{
		if(message.command.equals("creategame"))
		{
			game = Server.createGame(this, message.name, message.password, message.pMax);
			sendResult(game != null);
		}
		else if(message.command.equals("joingame"))
		{
			sendResult(Server.joinGame(this, message.name, message.password));
		}

	}



	/**
	 * Envoie les données à la Game
	 * @param data
	 */
	public void processDessinMessage(DrawingData data)
	{
		if(drawing){
			System.out.println("Nouvelle coordonnée : " + data.x + " " + data.y);
			game.sendData(data, this);
			sendResult(data!=null);
		}
		else
			sendResult(false);
	}



	/**
	 * Analyse la commande contenue dans le message et l'exécute. Obsolete.
	 * @param message
	 */
	public void processStringMessage(String message){
		if(message != null && !message.equals(""))
		{
			if(message.contains("%"))
			{
				String[] result = message.split("%"); //Le caractère % est le délimiteur entre les différents champs de la String
				if(result.length == 2){
					if(result[0].equals("creategame"))
					{
						game = Server.createGame(this, result[1], null, 0);
						sendResult(game!=null);
					}
					else if(result[0].equals("joingame"))
					{
						sendResult(Server.joinGame(this, result[1], null));
					}	
				}
				else if(result.length == 4)
				{
					System.out.println(result[3]);
					if(result[0].equals("creategame") && result[2].equals("password"))
					{
						game = Server.createGame(this, result[1], result[3], 0);
						sendResult(game!=null);
					}
					else if(result[0].equals("joingame") && result[2].equals("password"))
					{
						sendResult(Server.joinGame(this, result[1], result[3]));
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
				else if(message.equals("startgame"))
				{
					if(game!=null)
					{
						sendResult(game.startGame());
					}
					else
					{
						sendResult(false);
					}
				}
				else if(message.equals("getlist"))
				{
					try {
						out.writeObject(Server.getGames());
						out.flush();
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