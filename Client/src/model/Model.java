package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;

import socketData.AnswerCommand;
import socketData.Command;
import socketData.ChatCommand;
import socketData.GameList;
import socketData.PlayerScore;
import socketData.Scores;
import socketData.ValueCommand;
import socketData.WordCommand;
import view.Main;

/**
 * Gere les connexions et l'intéraction avec le serveur.
 *
 */
public class Model extends Observable {

	private ConnecToServer connec;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private GameObserver go;
	private ArrayList<PlayerScore> scores;
	private String word;


	/**
	 * Constructeur de Model
	 * @param host L'adresse du serveur
	 */
	public Model(String host) {
		connec = new ConnecToServer(host);
		go = null;
	}


	/**
	 * Tente de se connecter au serveur
	 * @param login
	 * @param password
	 * @return
	 */
	public boolean connect(String login, String password) {
		boolean res = connec.connect(login, password);

		if(res) {
			out = connec.getOutput();
			in = connec.getInput();
		}

		return res; 
	}


	public void disconnect() {
		connec.disconnect();
	}


	/**
	 * 
	 * @return Le mot à faire deviner
	 */
	public String getWord() {
		return word;
	}


	/**
	 * Analyse le contenu d'une Command pour agir en fonction
	 * @param command
	 */
	public void processCommand(Command command) {

		if(command.command.equals("startturn")) {
			Main.getView().startTurn();
		} else if(command.command.equals("endturn")) {
			Main.getView().endTurn();
		} else if(command.command.equals("endgame")) {
			setChanged();
			this.notifyObservers(false);
			setChanged();
			this.notifyObservers("endgame");
		} else if(command.command.equals("startdraw")) {
			setChanged();
			this.notifyObservers(true);
		} else if(command.command.equals("stopdraw")) {
			setChanged();
			this.notifyObservers(false);
		} else if(command.command.equals("goodword") || command.command.equals("wrongword") 
				|| command.command.equals("startgame") || command.command.equals("gameowner")) {
			setChanged();
			this.notifyObservers(command.command);
		}
	}

	/**
	 * Relaie aux observeurs une commande de chat
	 * @param msg
	 */
	public void processChatMsg(ChatCommand msg) {
		setChanged();
		this.notifyObservers(msg);
	}

	/**
	 * Si le champ command de la WordCommand est vide, alors le serveur envoie les mots à choisir, 
	 * sinon c'est que command contient le mot à faire deviner
	 * @param command
	 */
	public void processWordCommand(WordCommand command) {

		if(command.command.equals("")) {
			Main.getView().chooseWord(command);	
		} else {
			word = command.command;
			Main.getView().closeDialog();
		}
	}


	/**
	 * Envoie la réponse au serveur
	 * @param answer
	 * @param nbPixels Le nombre de pixels du dessin que l'on affiche (anti-lag)
	 */
	public void sendAnswer(String answer, long nbPixels) {
		AnswerCommand ans = new AnswerCommand(answer, nbPixels);
		sendCommand(ans);
	}

	/**
	 * On transforme la String en Command et on l'envoie au serveur
	 * @param command
	 */
	public void sendCommand(String command) {
		sendCommand(new Command(command));
	}


	/**
	 * On envoie la commande au serveur, s'il s'agit d'une WordCommand, 
	 * alors on conserve le mot à faire deviner
	 * @param command
	 */
	public void sendCommand(Command command) {

		if(command instanceof WordCommand){
			word = command.command;
		}

		try {
			out.writeObject(command);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * On envoie un nouveau message de chat au serveur
	 * @param msg
	 */
	public void sendChatMessage(String msg) {
		sendCommand(new ChatCommand(msg, Main.player));
	}


	public GameList getGameList() {
		return connec.getGameList();
	}


	/**
	 * On remplace les anciens scores par les nouveaux et on notifie le changement
	 * @param s
	 */
	public void processScores(Scores s) {
		scores = s.scores;
		setChanged();
		notifyObservers();
	}



	public ArrayList<PlayerScore> getScores() {
		return scores;
	}

	/**
	 * On essaye de créer une nouvelle partie
	 * @param name Le nom de la partie
	 * @param password Le mot de passe de la partie (peut être null)
	 * @param pMax Le nombre de joueurs maximum (0 = par défaut)
	 * @param turns Le nombre de tours (0 = par défaut)
	 * @param difficulty La difficulté choisie
	 * @return
	 */
	public String createGame(String name, String password, int pMax, 
			int turns, int difficulty) {

		String res = connec.createGame(name, password, pMax, turns, difficulty);

		if(res.equals("success")) {
			scores = new ArrayList<PlayerScore>();
			go = new GameObserver(in, out);
		}

		return res;
	}


	/**
	 * On essaye de rejoindre une partie
	 * @param name Le nom de la partie
	 * @param password Le mot de passe de la partie (peut être null)
	 * @return
	 */
	public String joinGame(String name, String password) {
		String res = connec.joinGame(name, password);

		if(res.equals("success")) {
			scores = new ArrayList<PlayerScore>();
			go = new GameObserver(in, out);
		}

		return res;
	}


	public ObjectOutputStream getOutput() {
		return out;
	}


	public ObjectInputStream getInput() {
		return in;
	}


	public GameObserver getGameObserver() {
		return go;
	}


	/**
	 * On envoie la ValueCommand aux observeurs
	 * @param obj
	 */
	public void processValueCommand(ValueCommand obj) {
		setChanged();
		this.notifyObservers(obj);
	}

	/**
	 * On quitte la partie
	 */
	public void quitGame() {

		sendCommand(new Command("quitgame"));
		scores = null;
		word = "";
		this.deleteObservers();

		if(go != null) {
			try {
				go.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			go = null;
		}
	}
}
