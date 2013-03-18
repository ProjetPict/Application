package server;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import socketData.AnswerCommand;
import socketData.Command;
import socketData.PlayerScore;
import socketData.WordCommand;

/**
 * G�re une partie et ses joueurs.
 * @author Jerome
 *
 */
public class Game extends Thread{


	private ArrayList<Player> podium; //Les trois premiers joueurs en terme de score
	private ArrayList<Player> firstAnswers; //Les trois premiers joueurs � avoir r�pondu
	private ArrayList<Player> players;
	private Player drawingPlayer;
	private String name;
	private String password;
	private int pMax;
	private int turns;
	private int currentTurn;
	private boolean running;
	private boolean started; //True si la partie a demarr�
	private String word;

	private int time;
	private Timer timer;


	/**
	 * 
	 * @param creator
	 * @param name
	 * @param password
	 */
	public Game(Player creator, String name, String password, int pMax, int turns){
		this.name = name;
		this.password = password; 	//vide si publique 
		players = new ArrayList<Player>();
		players.add(creator);
		drawingPlayer = null;
		word = "";

		if(pMax > 1 && pMax <= 25)
			this.pMax = pMax;
		else
			this.pMax = Server.MAX_PLAYER;

		if(turns > 1 && turns <= 10)
			this.turns = turns;
		else
			this.turns = Server.TURNS;

		currentTurn = 0;
		started = false;
		podium = new ArrayList<Player>();
		firstAnswers = new ArrayList<Player>();

	}


	/**
	 * 
	 * @param creator
	 * @param name
	 * @param password
	 * @param pMax
	 */
	public Game(Player creator, String name, String password, int pMax){
		players = new ArrayList<Player>();
		players.add(creator);
		this.password = password; 	//vide si publique 
		this.pMax = pMax;
		this.name = name;
	}


	/**
	 * Surcharge de la m�thode run() de Thread. Inutile pour l'instant mais elle devrait servir � faire
	 * "tourner" la partie (changement de joueurs, de tour, etc...)
	 */
	public void run() {
		running = true;

		while(running){

			while(started)
			{
				currentTurn = 0;
				drawingPlayer = null;
				podium.clear();
				while(currentTurn <= turns)
				{
					currentTurn++;
					startTurn();


					for(int i = 0; i < players.size(); i++)
					{
						drawingPlayer = players.get(i);
						if(!drawingPlayer.isGhost())
						{

							setupNextPlayer();
							
							sendCommand(new Command("startturn"));
							
							launchTimer(60);

							computeScores();

							for(int j = 0; j < players.size(); j++)
							{
								//sendScores(players.get(j));
							}
							
							drawingPlayer.setDrawing(false);
						}
					}

					endTurn();
				}

				started = false;
				Player player;
				for(int i = 0; i < players.size(); i++)
				{
					player = players.get(i);
					player.setScore(0);
					if(player.isGhost())
						player.setGhost(false);
				}
			}

			try {
				sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block 
				e.printStackTrace();
			}
		}
	}


	/**
	 * 
	 * @return True si la partie est priv�e, false sinon.
	 */
	public boolean isPrivate(){
		if(password != null)
			return true;
		else
			return false;
	}


	/**
	 * 
	 * @return True si la partie a demarr�, false sinon
	 */
	public boolean isStarted()
	{
		return started;
	}


	/**
	 * 
	 * @return Le mot de passe de la partie (peut �tre null).
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * 
	 * @return Le nombre de joueurs dans la partie
	 */
	public int getNbPlayers()
	{
		return players.size();
	}


	/**
	 * 
	 * @return Retourne le nombre de joueurs maximum autoris�s dans la partie.
	 */
	public int getJMax(){
		return pMax;
	}


	/**
	 * Ajoute un joueur dans la partie.
	 * @param p
	 * @return True si l'ajout est un succ�s, false sinon
	 */
	public boolean addPlayer(Player p){
		if(players.size() < pMax && !players.contains(p)){
			if(started)
				p.setGhost(true);
			players.add(p);
			return true;
		}
		return false;
	}

	public void stopGame()
	{
		if(timer != null)
			timer.cancel();
		running = false;
		started = false;
	}


	/**
	 * Supprime un joueur de la partie
	 * @param player
	 */
	public void removePlayer(Player player){
		players.remove(player);
		podium.remove(player);
		firstAnswers.remove(player);
		if(drawingPlayer == player)
			drawingPlayer = null;
		System.out.println(player.getLogin() + " a quitt� la partie " + name);

		if(players.size() <= 0) //S'il n'y a plus de joueurs, on arr�te la partie
		{
			running = false;
			Server.removeGame(this);
		}
	}


	/**
	 * 
	 * @return Le nom de la partie
	 */
	public String getGameName()
	{
		return name;
	}


	/**
	 * Envoie data � tout les joueurs de la partie, � l'exception du joueur d'origine
	 * @param p
	 * @param sender
	 */
	public void sendData(Object data, Player sender)
	{
		//TODO test : if sender == drawingplayer
		for(int i = 0; i < players.size(); i++){
			if(sender != players.get(i)){
				ObjectOutputStream out = players.get(i).getOutput();
				try {
					out.writeObject(data);
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public void sendCommand(Command cmd, Player sender)
	{
		for(int i = 0; i < players.size(); i++){
			if(sender == null || sender != players.get(i)){
				ObjectOutputStream out = players.get(i).getOutput();
				try {
					out.writeObject(cmd);
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public void sendCommand(Command cmd)
	{
		sendCommand(cmd, null);
	}


	private boolean launchTimer(int t)
	{
		boolean res = true;
		
		if(timer == null)
		{
			this.time = t;

			timer = new Timer();

			timer.schedule(new TimerTask() {
				public void run() {
					time--;
				}
			}, 0, 1000);

			while(time > 0)
			{
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			timer.cancel();
			timer = null;
		}
		else
			res = false;

		return res;
	}

	private void setupNextPlayer()
	{
		firstAnswers.clear();
		word = "";
		drawingPlayer.setDrawing(true);

		//TODO integrer le tirage de mots al�atoire
		WordCommand choices = new WordCommand("test1", "test2", "test3", 1);
		drawingPlayer.setChoices(choices);

		launchTimer(30);

		if(word.equals(""))
		{
			ObjectOutputStream out = drawingPlayer.getOutput();
			choices = new WordCommand("", "", "", 1);

			//TODO choix aleatoire entre les trois mots
			choices.command = "default";
			word = "default";
			try {
				out.writeObject(choices);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		//TODO setupNextPlayer
	}

	private void startTurn()
	{
		//TODO startTurn
	}

	private void endTurn()
	{
		//TODO endTurn
	}

	private void computeScores()
	{
		//TODO computeScores
	}

	public boolean checkAnswer(AnswerCommand answer, Player player)
	{
		boolean res = false;

		if(answer.command.equals(word))
		{
			res = true;
			player.setNbPixels(answer.nbPixels);

			Player p;
			boolean inserted = false;
			for(int i = 0; i < firstAnswers.size(); i++)
			{
				p = firstAnswers.get(i);

				if(p.getNbPixels() > player.getNbPixels())
				{
					inserted = true;
					firstAnswers.add(i, player);
					if(firstAnswers.size() > 3)
						firstAnswers.remove(firstAnswers.size()-1);
				}
			}

			if(!inserted)
				firstAnswers.add(player);

		}

		return res;
	}

	public void sendScores(Player player)
	{
		ObjectOutputStream out = player.getOutput();
		PlayerScore ps;
		for(int i = 0; i < players.size(); i++){
			//TODO Mettre les vraies valeurs
			ps = new PlayerScore(player.getLogin(), 0, false);
			try {
				out.writeObject(ps);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public boolean startGame() {

		if(players.size() >= 2)
		{
			started = true;
			return true;
		}
		else
			return false;
	}

	public void setWord(String word)
	{
		if(timer != null)
			timer.cancel();
		time = 0;
		this.word = word;
	}
}
