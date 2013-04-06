package server;


import java.awt.Point;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import socketData.AnswerCommand;
import socketData.Command;
import socketData.ChatCommand;
import socketData.Line;
import socketData.Picture;
import socketData.PlayerScore;
import socketData.Scores;
import socketData.ValueCommand;
import socketData.WordCommand;

/**
 * Gï¿½re une partie et ses joueurs.
 * @author Jerome
 *
 */
public class Game extends Thread{


	private ArrayList<Player> podium; //Les trois premiers joueurs en terme de score
	private ArrayList<Player> firstAnswers; //Les trois premiers joueurs à avoir répondu
	private ArrayList<Player> players;
	private Player drawingPlayer;
	private String name;
	private String password;
	private int pMax;
	private int turns;
	private int currentTurn;
	private boolean running;
	private boolean started; //True si la partie a demarré
	private String word;
	private int difficulty;
	private int nbAnswer;

	private Picture currentDrawing;

	private int time;
	private Timer timer;


	/**
	 * 
	 * @param creator
	 * @param name
	 * @param password
	 */
	public Game(Player creator, String name, String password, int pMax, int turns, int difficulty){
		this.name = name;
		this.password = password; 	//vide si publique 
		players = new ArrayList<Player>();
		players.add(creator);
		drawingPlayer = null;
		currentDrawing = new Picture();
		word = "";
		nbAnswer = 0;

		if(pMax > 1 && pMax <= 25)
			this.pMax = pMax;
		else
			this.pMax = Server.MAX_PLAYER;

		if(turns >= 1 && turns <= 10)
			this.turns = turns;
		else
			this.turns = Server.TURNS;

		if(difficulty >= 0 && difficulty <= 3)
		{
			this.difficulty = difficulty;
		}
		else
			this.difficulty = 0;

		currentTurn = 0;
		started = false;
		podium = new ArrayList<Player>();
		firstAnswers = new ArrayList<Player>();

	}


	/**
	 * Surcharge de la méthode run() de Thread. Elle sert à faire
	 * "tourner" la partie (changement de joueurs, de tour, etc...)
	 */
	public void run() {
		running = true;

		while(running){

			while(started)
			{
				sendCommandTo(new Command("startgame"), players.get(0));
				currentTurn = 0;
				drawingPlayer = null;
				podium.clear();

				sendScores();
				sendCommand(new ValueCommand("turns", turns));

				while(currentTurn < turns)
				{
					currentTurn++;

					sendCommand(new ValueCommand("turn", currentTurn));

					for(int i = 0; i < players.size(); i++)
					{
						drawingPlayer = players.get(i);
						if(!drawingPlayer.isGhost())
						{

							setupNextPlayer();

							sendCommand(new Command("startturn"));

							drawingPlayer.setDrawing(true);
							sendScores();

							launchTimer(60);

							computeScores();
							
							sendCommand(new Command("endturn"));

							drawingPlayer.setDrawing(false);

							for(Player p: players){
								p.setFound(false);
							}
							
							sendScores();

						}
					}
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

				sendCommand(new Command("endgame"));
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
	 * @return True si la partie est privée, false sinon.
	 */
	public boolean isPrivate(){
		if(password != null)
			return true;
		else
			return false;
	}


	/**
	 * 
	 * @return True si la partie a demarré, false sinon
	 */
	public boolean isStarted()
	{
		return started;
	}


	/**
	 * 
	 * @return Le mot de passe de la partie (peut être null).
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


	public int getDifficulty()
	{
		return difficulty;
	}


	/**
	 * 
	 * @return Retourne le nombre de joueurs maximum autorisés dans la partie.
	 */
	public int getJMax(){
		return pMax;
	}


	/**
	 * Ajoute un joueur dans la partie.
	 * @param p
	 * @return True si l'ajout est un succès, false sinon
	 */
	public boolean addPlayer(Player p){
		if(players.size() < pMax && !players.contains(p)){
			if(started)
				p.setGhost(true);
			players.add(p);
			p.sendResult(true);
			sendScores(p, true);
			sendCommand(new ChatCommand(p.getLogin() + " a rejoint la partie.", null));
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
		System.out.println(player.getLogin() + " a quitté la partie " + name);

		if(players.size() <= 0) //S'il n'y a plus de joueurs, on arrï¿½te la partie
		{
			running = false;
			Server.removeGame(this);
		}
		else
			sendCommand(new ChatCommand(player.getLogin() + " a quitté la partie.", null));
		sendCommandTo(new Command("quitgame"), player);
		sendScores();
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
	 * Envoie data à tout les joueurs de la partie, à l'exception du joueur d'origine
	 * @param p
	 * @param sender
	 */
	public void sendData(Object data, Player sender)
	{
		if(sender == drawingPlayer)
		{

			if(data instanceof Point)
				currentDrawing.addPoint((Point) data);
			else
				currentDrawing.addLine((Line) data);

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
	}


	public void sendChatMsg(ChatCommand msg)
	{
		if(!word.equals(""))
		{
			String temp = convertToAnswerStringFormat(msg.command);
			int ind = temp.indexOf(word);

			if(ind >= 0)
			{
				msg.command = msg.command.substring(0, ind) + "****" + msg.command.substring(ind+word.length(), msg.command.length());
			}
		}

		for(int i = 0; i < players.size(); i++){
			sendCommandTo(msg, players.get(i));
		}
	}

	public void sendCommand(Command cmd, Player sender)
	{
		for(int i = 0; i < players.size(); i++){
			if(sender == null || sender != players.get(i)){
				sendCommandTo(cmd, players.get(i));
			}
		}
	}

	public void sendCommandTo(Command cmd, Player player)
	{
		ObjectOutputStream out = player.getOutput();
		try {
			out.writeObject(cmd);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void sendCommand(Command cmd)
	{
		sendCommand(cmd, null);
	}


	private void launchTimer(int t)
	{
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
	}

	private void setupNextPlayer()
	{
		firstAnswers.clear();
		word = "";
		nbAnswer = 0;
		currentDrawing.clear();

		String[] words = chooseNextWords();
		WordCommand choices = new WordCommand(words[0], words[1], words[2], difficulty);
		drawingPlayer.setChoices(choices);

		launchTimer(15);

		if(word.equals(""))
		{
			ObjectOutputStream out = drawingPlayer.getOutput();
			int random = (int)(Math.random() * 3);
			choices = new WordCommand("", "", "", 1);

			choices.command = words[random];
			word = words[random];
			word = convertToAnswerStringFormat(word);

			try {
				out.writeObject(choices);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private String[] chooseNextWords() {
		ArrayList<String> words = Server.getDbInfos().getWordsList(difficulty);
		String[] choices = new String[]{"", "", ""};

		String random;
		int randomIndex;

		for(int i = 0; i < 3; i++)
		{
			do{
				randomIndex = (int)(Math.random() * words.size());
				random = words.get(randomIndex);
			}while(choices[0].equals(random) || choices[1].equals(random) 
					|| choices[2].equals(random));
			choices[i] = random;
		}

		return choices;
	}


	private void computeScores()
	{
		//Set score for "finding players"
		for(Player p: players){
			if(p.hasFound() && p != drawingPlayer){

				if(firstAnswers.contains(p)){
					int rank = firstAnswers.indexOf(p);
					switch(rank){
					case 0 : p.setScore(p.getScore()+5); break;
					case 1 : p.setScore(p.getScore()+4); break;
					case 2 : p.setScore(p.getScore()+3); break;
					}
				}else{
					p.setScore(p.getScore()+1);
				}
			}
		}

		//Set score for the "drawing player"
		int findingRatio = (nbAnswer/(players.size()-1))*100;

		if(findingRatio == 100){
			drawingPlayer.setScore(drawingPlayer.getScore()+7);
		}else if(findingRatio > 75){
			drawingPlayer.setScore(drawingPlayer.getScore()+6);
		}else if(findingRatio > 50){
			drawingPlayer.setScore(drawingPlayer.getScore()+5);
		}else if(findingRatio > 25){
			drawingPlayer.setScore(drawingPlayer.getScore()+3);
		}else if(nbAnswer > 0){
			drawingPlayer.setScore(drawingPlayer.getScore()+2);
		}

	}


	public boolean checkAnswer(AnswerCommand answer, Player player)
	{
		boolean res = false;

		String ans = convertToAnswerStringFormat(answer.command);

		if(ans.equals(word))
		{
			res = true;
			nbAnswer++;

			if(nbAnswer >= players.size()-1)
			{
				time = 0;
			}

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
			player.setFound(true);
			sendScores();
		}

		String command;
		if(res)
			command = "goodword";
		else
			command = "wrongword";

		sendCommandTo(new Command(command), player);

		return res;
	}

	public void sendScores(Player p, boolean except)
	{
		Scores scores = new Scores();

		for(int i = 0; i < players.size(); i++)
		{
			scores.scores.add(new PlayerScore(players.get(i).getLogin(), players.get(i).getScore(), 
					players.get(i).hasFound(), players.get(i).isGhost(), players.get(i).getDrawing()));
		}
		
		Collections.sort(scores.scores, new Comparator<PlayerScore>() {
			@Override
			public int compare(PlayerScore p1, PlayerScore p2) {
				return Integer.signum(p2.score - p1.score);
			}
		});

		if(!except)
		{
			ObjectOutputStream out = p.getOutput();

			try {
				out.writeObject(scores);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			for(int i = 0; i < players.size(); i++)
			{
				if(p != players.get(i))
				{
					ObjectOutputStream out = players.get(i).getOutput();

					try {
						out.writeObject(scores);
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void sendScores()
	{
		sendScores(null, true);
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
		System.out.println("Test");
		if(timer != null)
			timer.cancel();
		time = 0;
		this.word = convertToAnswerStringFormat(word);;
	}


	public void sendDrawing(Player player) {
		if(player.isGhost())
		{
			ObjectOutputStream out = player.getOutput();
			try {
				out.writeObject(currentDrawing);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String convertToAnswerStringFormat(String s) {

		String withAccents = "âäàçéêëèîïôöûüù";
		String withoutAccents = "aaaceeeeiioouuu";

		for (int i = 0 ; i < withAccents.length () ; i++) {
			s = s.replace(withAccents.charAt(i), withoutAccents.charAt(i));
		}
		s = s.toLowerCase();
		return s;
	}

}
