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
 * les données qu'il envoie depuis son socket.
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
	 * Le constructeur de Player
	 * @param login Le login du joueur
	 * @param socket Le socket du joueur
	 * @param ghost True si le joueur est "fantôme", false sinon
	 * @param in
	 * @param out
	 */
	public Player(String login, Socket socket, boolean ghost, ObjectInputStream in, ObjectOutputStream out) {
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
	 * @param game La partie dans laquelle le joueur se trouve
	 */
	public void setGame(Game game) {
		this.game = game;
	}


	/**
	 * 
	 * @return L'instance de Game contenue dans Player
	 */
	public Game getGame() {
		return game;
	}


	/**
	 * 
	 * @param ghost
	 */
	public void setGhost(boolean ghost) {
		this.ghost = ghost;
	}


	/**
	 * 
	 * @return La valeur de ghost (true si le joueur est un joueur fantôme, false sinon)
	 */
	public boolean isGhost() {
		return ghost;
	}


	/**
	 * 
	 * @return Le login du joueur
	 */
	public String getLogin() {
		return login;
	}


	/**
	 * 
	 * @return Le nombre de pixels dans le dessin affiché sur l'écran du joueur
	 */
	public long getNbPixels() {
		return nbPixels;
	}

	/**
	 * 
	 * @param nbPixels
	 */
	public void setNbPixels(long nbPixels) {
		this.nbPixels = nbPixels;
	}


	/**
	 * 
	 * @return Le score du joueur
	 */
	public int getScore() {
		return score;
	}

	/**
	 * 
	 * @return True si le joueur a trouvé le mot, false sinon
	 */
	public boolean hasFound() {
		return hasFound;
	}

	/**
	 * 
	 * @param found
	 */
	public void setFound(boolean found) {
		hasFound = found;
	}

	/**
	 * 
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * On envoie les mots à choisir au joueur
	 * @param choices
	 */
	public void setChoices(WordCommand choices) {
		this.choices = choices;

		try {
			out.writeObject(choices);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return L'output stream du socket du joueur.
	 */
	public ObjectOutputStream getOutput() {
		return out;
	}


	/**
	 * 
	 * @return True si le joueur dessine, false sinon
	 */
	public boolean getDrawing() {
		return drawing;
	}


	/**
	 * On change la valeur de drawing, et on envoie une commande au client pour prendre ce changement en compte
	 * @param drawing
	 */
	public void setDrawing(boolean drawing) {
		this.drawing = drawing;
		String result = "";

		if(drawing)
			result = "startdraw";
		else
			result = "stopdraw";

		try {
			out.writeObject(new Command(result));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Surcharge de la fonction run() de Thread. On boucle à l'infini tant
	 * que la connexion n'est pas coupée, et on récupère les données que le
	 * joueur envoie.
	 */
	public void run() {

		Object message;

		try {

			while(connected && !socket.isClosed()) {
				message = in.readObject();
				processTypeMessage(message);
			}

			in.close();
			Server.removePlayer(this);

		} catch (IOException e1) {
			Server.writeIn("Connexion à " + login + " perdue.");
			connected = false;
			Server.removePlayer(this);
		} catch (ClassNotFoundException e) {
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
			if(result) {
				out.writeObject(new Command("success"));
				out.flush();
			} else {
				out.writeObject(new Command("fail"));
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * On envoie un résultat sous forme de String
	 * @param result Le résultat à envoyer
	 */
	public void sendResult(String result) {
		try {
			out.writeObject(new Command(result));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Cette méthode détermine le type du message passé en paramètre, et appelle la fonction
	 * de traitement correspondante.
	 * @param message Le message à traiter
	 */
	public void processTypeMessage(Object message) {

		if(message instanceof Point || message instanceof Line) {
			if(drawing) {
				game.sendData(message, this);
				sendResult(message!=null);
			} else
				sendResult(false);
		} else if(message instanceof CreateJoinCommand)
			processCreateJoinMessage((CreateJoinCommand) message);
		else if(message instanceof AnswerCommand)
			game.checkAnswer((AnswerCommand)message, this);
		else if(message instanceof WordCommand)
			processWordCommand((WordCommand) message);
		else if(message instanceof ChatCommand)
			game.sendChatMsg((ChatCommand) message);
		else if(message instanceof Command)
			processCommandMessage((Command) message);
	}


	/**
	 * On traite un message de type WordCommand
	 * @param message La commande à traiter
	 */
	private void processWordCommand(WordCommand message) {
		if(game != null) {
			if(!message.command.equals("") && choices != null) {	
				String choice = message.command;

				if(choice.equals(choices.word1) || choice.equals(choices.word2) 
						|| choice.equals(choices.word3)) {
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

		if(message.command.equals("quit")) {
			connected = false;
			Server.removePlayer(this);
			try {
				sendResult(true);
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(message.command.equals("quitgame")) {
			if(game!=null) {
				game.removePlayer(this);
				game = null;
				this.drawing = false;
				this.ghost = false;
				this.hasFound = false;
				this.score = 0;
				this.nbPixels = 0;
			}
		} else if(message.command.equals("startgame")) {
			if(game!=null)
				sendResult(game.startGame());
			else
				sendResult(false);
		} else if(message.command.equals("getlist")) {
			try {
				out.writeObject(Server.getGames());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if(message.command.equals("newline")) {
			if(game!=null)
				game.sendCommand(message, this);
		} else if(message.command.equals("getscores")) {
			if(game!=null)
				game.sendScores(this, false);
		} else
			sendResult(false);
	}

	/**
	 * Analyse une commande de type creategame ou joingame
	 * @param message
	 */
	private void processCreateJoinMessage(CreateJoinCommand message) 
	{
		if(message.command.equals("creategame")) {
			game = Server.createGame(this, message.name, message.password, message.pMax, 
					message.turns, message.difficulty);
			sendResult(game != null);

			if(game != null)
				game.sendScores(this, false);
		} else if(message.command.equals("joingame"))
			Server.joinGame(this, message.name, message.password);
	}
}