package server;


import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import socketData.AnswerCommand;
import socketData.Command;
import socketData.ChatCommand;
import socketData.CreateJoinCommand;
import socketData.Line;
import socketData.WordCommand;


/**
 * Cette classe est un thread qui permet de maintenir la connexion avec un joueur et d'intercepter
 * les donneees qu'il envoie depuis son socket.
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
	private int score;
	private boolean hasFound;
	private long nbPixels;
	private WordCommand choices;

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
		nbPixels = 0;
		hasFound=false;
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
	public boolean isGhost(){
		return ghost;
	}


	/**
	 * 
	 * @return Le login du joueur
	 */
	public String getLogin(){
		return login;
	}


	public long getNbPixels()
	{
		return nbPixels;
	}

	public void setNbPixels(long nbPixels)
	{
		this.nbPixels = nbPixels;
	}

	public int getScore()
	{
		return score;
	}

	public boolean hasFound(){
		return hasFound;
	}

	public void setFound(boolean found){
		hasFound = found;
	}

	public void setScore(int score)
	{
		this.score = score;
	}

	public void setChoices(WordCommand choices)
	{
		this.choices = choices;

		try {
			out.writeObject(choices);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			out.writeObject(new Command(result));
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				processTypeMessage(message);
			}

			in.close();
			Server.removePlayer(this);

		} catch (IOException e1) {
			Server.writeIn("Connexion � " + login + " perdue.");
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
				out.writeObject(new Command("success"));
				out.flush();
			}
			else
			{
				out.writeObject(new Command("fail"));
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public void sendResult(String result)
	{
		try {

			out.writeObject(new Command(result));
			out.flush();


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
		if(message instanceof Point || message instanceof Line){

			if(drawing){
				game.sendData(message, this);
				sendResult(message!=null);
			}
			else
				sendResult(false);
		}
		else if(message instanceof CreateJoinCommand)
		{
			processCreateJoinMessage((CreateJoinCommand) message);
		}
		else if(message instanceof AnswerCommand)
		{
			game.checkAnswer((AnswerCommand)message, this);
		}
		else if(message instanceof WordCommand)
		{
			processWordCommand((WordCommand) message);
		}
		else if(message instanceof ChatCommand)
		{
			game.sendChatMsg((ChatCommand) message);
		}
		else if(message instanceof Command)
		{
			processCommandMessage((Command) message);
		}


	}


	private void processWordCommand(WordCommand message) {
		if(game != null)
		{
			if(!message.command.equals("") && choices != null)
			{
				String choice = message.command;
				if(choice.equals(choices.word1) || choice.equals(choices.word2) 
						|| choice.equals(choices.word3))
				{
					game.setWord(choice);
					choices = null;
				}
			}
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
			Server.removePlayer(this);
			try {
				sendResult(true);
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(message.command.equals("quitgame"))
		{
			if(game!=null)
			{
				game.removePlayer(this);
				game = null;
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
		else if(message.command.equals("newline"))
		{
			game.sendCommand(message, this);
		}
		else if(message.command.equals("getdrawing"))
		{
			game.sendDrawing(this);
		}
		else if(message.command.equals("getscores"))
		{
			game.sendScores(this, false);
		}
		else
		{
			sendResult(false);
		}

	}

	/**
	 * Analyse une commande de type creategame ou joingame
	 * @param message
	 */
	private void processCreateJoinMessage(CreateJoinCommand message) 
	{
		if(message.command.equals("creategame"))
		{
			game = Server.createGame(this, message.name, message.password, message.pMax, 
					message.turns, message.difficulty);
			sendResult(game != null);

			if(game != null)
			{
				game.sendScores(this, false);
			}
		}
		else if(message.command.equals("joingame"))
		{
			Server.joinGame(this, message.name, message.password);
		}

	}
}