package server;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import socketData.AnswerCommand;
import socketData.ChatCommand;
import socketData.Command;
import socketData.PlayerScore;
import socketData.Scores;
import socketData.ValueCommand;
import socketData.WordCommand;

/**
 * Gère une partie et ses joueurs.
 *
 */
public class Game extends Thread {

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
	private boolean interrupted;
	private String word;
	private int difficulty;
	private int nbAnswer;
	private int newGhosts;

	private int time;
	private Timer timer;


	/**
	 * Constructeur de Game
	 * @param creator Le Player qui a créé la partie
	 * @param name Le nom de la partie
	 * @param password Le mot de passe de la partie (peut être null)
	 * @param pMax Le nombre de joueurs maximum (0 = par défaut)
	 * @param turns Le nombre de tours (0 = par défaut)
	 * @param difficulty La difficulté de la partie
	 */
	public Game(Player creator, String name, String password, int pMax, int turns, int difficulty) {
		this.name = name;
		this.password = password; 	//vide si publique 
		players = new ArrayList<Player>();
		players.add(creator);
		drawingPlayer = null;
		word = "";
		nbAnswer = 0;
		newGhosts = 0;

		if(pMax > 1 && pMax <= 25)
			this.pMax = pMax;
		else
			this.pMax = Server.MAX_PLAYER;

		if(turns >= 1 && turns <= 10)
			this.turns = turns;
		else
			this.turns = Server.TURNS;

		if(difficulty >= 0 && difficulty <= 3)
			this.difficulty = difficulty;
		else
			this.difficulty = 0;

		currentTurn = 0;
		started = false;
		interrupted = false;
		firstAnswers = new ArrayList<Player>();
	}


	/**
	 * Surcharge de la méthode run() de Thread. Elle sert à faire
	 * "tourner" la partie (changement de joueurs, de tour, etc...)
	 * 
	 */
	public void run() {
		running = true;

		while(running){
			while(started) {

				newGhosts = 0;
				currentTurn = 0;
				drawingPlayer = null;

				sendCommandTo(new Command("startgame"), players.get(0));
				sendScores();
				sendCommand(new ValueCommand("turns", turns));

				while(currentTurn < turns && players.size() >= 2) {

					currentTurn++;
					sendCommand(new ValueCommand("turn", currentTurn));

					int i = 0;

					while(i < players.size() && players.size() >= 2) {
						drawingPlayer = players.get(i);

						if(!drawingPlayer.isGhost()) {

							setupNextPlayer();
							sendCommand(new Command("startturn"));
							drawingPlayer.setDrawing(true);
							sendScores();
							launchTimer(60);

							if(!interrupted)
								computeScores();

							sendCommand(new Command("endturn"));
							Server.getDbInfos().getStatistics().addNewStatsToWord(word, true, nbAnswer, players.size()-1);

							if(drawingPlayer != null)
								drawingPlayer.setDrawing(false);

							for(Player p: players)
								p.setFound(false);

							sendScores();
						}

						i++;
						newGhosts = 0;
					}
				}

				started = false;
				Player player;

				for(int i = 0; i < players.size(); i++) {
					player = players.get(i);
					player.setScore(0);
					if(player.isGhost())
						player.setGhost(false);
				}

				sendCommand(new Command("endgame"));
			}

			try {
				sleep(160);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 
	 * @return True si la partie est privée, false sinon.
	 */
	public boolean isPrivate() {
		if(password != null)
			return true;
		else
			return false;
	}


	/**
	 * 
	 * @return True si la partie a demarré, false sinon
	 */
	public boolean isStarted() {
		return started;
	}


	/**
	 * 
	 * @return Le mot de passe de la partie (peut être null).
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @return Le nombre de joueurs dans la partie
	 */
	public int getNbPlayers() {
		return players.size();
	}


	/**
	 * 
	 * @return La difficulté de la partie
	 */
	public int getDifficulty() {
		return difficulty;
	}


	/**
	 * 
	 * @return Retourne le nombre de joueurs maximum autorisés dans la partie.
	 */
	public int getJMax() {
		return pMax;
	}


	/**
	 * Ajoute un joueur dans la partie.
	 * @param p
	 * @return True si l'ajout est un succès, false sinon
	 */
	public boolean addPlayer(Player p) {

		if(players.size() < pMax && !players.contains(p)) {
			if(started) {
				p.setGhost(true);
				newGhosts++;
			}

			players.add(p);
			p.sendResult(true);
			sendScores(p, true);
			sendCommand(new ChatCommand(p.getLogin() + " a rejoint la partie.", null));
			return true;
		}
		return false;
	}


	/**
	 * On arrete la partie
	 */
	public void stopGame() {

		if(timer != null)
			timer.cancel();
		running = false;
		started = false;
	}


	/**
	 * Supprime un joueur de la partie
	 * @param player
	 */
	public void removePlayer(Player player) {

		int index = players.indexOf(player);		
		players.remove(player);
		firstAnswers.remove(player);
		sendCommandTo(new Command("quitgame"), player);

		if(drawingPlayer == player) {
			drawingPlayer = null;
			interrupted = true;
			time = 0;
		} else if(players.size() < 2) {
			interrupted = true;
			time = 0;
		}

		if(index == 0 && players.size() > 0)
			sendCommandTo(new Command("gameowner"), players.get(0));

		if(player.hasFound())
			nbAnswer--;

		System.out.println(player.getLogin() + " a quitté la partie " + name);

		//S'il n'y a plus de joueurs, on arrête la partie
		if(players.size() < 1) {
			running = false;
			Server.removeGame(this);
		} else
			sendCommand(new ChatCommand(player.getLogin() + " a quitté la partie.", null));

		sendScores();
	}


	/**
	 * 
	 * @return Le nom de la partie
	 */
	public String getGameName() {
		return name;
	}


	/**
	 * Envoie data à tout les joueurs de la partie, à l'exception du joueur d'origine
	 * @param p
	 * @param sender
	 */
	public void sendData(Object data, Player sender) {

		if(sender == drawingPlayer) {

			for(int i = 0; i < players.size(); i++) {
				if(sender != players.get(i)) {
					ObjectOutputStream out = players.get(i).getOutput();
					try {
						out.writeObject(data);
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


	/**
	 * Envoie un message de chat à tout les joueurs
	 * @param msg
	 */
	public void sendChatMsg(ChatCommand msg) {

		if(!word.equals("")) {
			String temp = convertToAnswerStringFormat(msg.command);
			int ind = temp.indexOf(word);

			if(ind >= 0) {
				msg.command = msg.command.substring(0, ind) + "****" 
						+ msg.command.substring(ind+word.length(), msg.command.length());
			}
		}

		for(int i = 0; i < players.size(); i++) {
			sendCommandTo(msg, players.get(i));
		}
	}

	/**
	 * Envoie une commande à tout le monde sauf à sender
	 * @param cmd La commande
	 * @param sender Le joueur qui envoie la commande
	 */
	public void sendCommand(Command cmd, Player sender) {

		for(int i = 0; i < players.size(); i++) {

			if(sender == null || sender != players.get(i)) {
				sendCommandTo(cmd, players.get(i));
			}
		}
	}


	/**
	 * Envoie une commande à un joueur
	 * @param cmd La commande
	 * @param player Le destinataire
	 */
	public void sendCommandTo(Command cmd, Player player) {

		ObjectOutputStream out = player.getOutput();
		try {
			out.writeObject(cmd);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Envoie une commande à tout le monde
	 * @param cmd La commande à envoyer
	 */
	public void sendCommand(Command cmd) {
		sendCommand(cmd, null);
	}


	private void launchTimer(int t) {

		if(timer == null) {
			this.time = t;
			timer = new Timer();

			timer.schedule(new TimerTask() {
				public void run() {
					time--;
				}
			}, 0, 1000);

			while(time > 0) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			timer.cancel();
			timer = null;
		}
	}

	/**
	 * On prépare les différents paramètres pour changer de joueur dessinateur
	 */
	private void setupNextPlayer() {
		firstAnswers.clear();
		word = "";
		nbAnswer = 0;
		interrupted = false;

		String[] words = chooseNextWords();
		WordCommand choices = new WordCommand(words[0], words[1], words[2], difficulty);
		drawingPlayer.setChoices(choices);

		Server.getDbInfos().getStatistics().addNewStatsToWord(words[0], false);
		Server.getDbInfos().getStatistics().addNewStatsToWord(words[1], false);
		Server.getDbInfos().getStatistics().addNewStatsToWord(words[2], false);

		launchTimer(15);

		if(word.equals("")) {
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
				e.printStackTrace();
			}
		}
	}


	/**
	 * 
	 * @return Trois mots pris au hasard dans la base de données
	 */
	private String[] chooseNextWords() {
		ArrayList<String> words = Server.getDbInfos().getWordsList(difficulty);
		String[] choices = new String[]{"", "", ""};

		String random;
		int randomIndex;

		for(int i = 0; i < 3; i++) {
			do {
				randomIndex = (int)(Math.random() * words.size());
				random = words.get(randomIndex);
			} while(choices[0].equals(random) || choices[1].equals(random) 
					|| choices[2].equals(random));

			choices[i] = random;
		}

		return choices;
	}


	/**
	 * On calcule les scores
	 */
	private void computeScores() {

		for(Player p: players) {
			if(p.hasFound() && p != drawingPlayer) {

				if(firstAnswers.contains(p)) {
					int rank = firstAnswers.indexOf(p);

					switch(rank) {
					case 0 : p.setScore(p.getScore()+5); break;
					case 1 : p.setScore(p.getScore()+4); break;
					case 2 : p.setScore(p.getScore()+3); break;
					}
				} else {
					p.setScore(p.getScore()+1);
				}
			}
		}

		int findingRatio = (nbAnswer/(players.size()-1))*100;

		if(findingRatio == 100) {
			drawingPlayer.setScore(drawingPlayer.getScore()+7);
		} else if(findingRatio > 75) {
			drawingPlayer.setScore(drawingPlayer.getScore()+6);
		} else if(findingRatio > 50) {
			drawingPlayer.setScore(drawingPlayer.getScore()+5);
		} else if(findingRatio > 25) {
			drawingPlayer.setScore(drawingPlayer.getScore()+3);
		} else if(nbAnswer > 0) {
			drawingPlayer.setScore(drawingPlayer.getScore()+2);
		}
	}


	/**
	 * On vérifie si la réponse est juste
	 * @param answer La réponse
	 * @param player Le joueur qui envoie la réponse
	 * @return True si la réponse est bonne, false sinon
	 */
	public boolean checkAnswer(AnswerCommand answer, Player player) {
		boolean res = false;
		String ans = convertToAnswerStringFormat(answer.command);

		if(ans.equals(word)) {
			res = true;
			nbAnswer++;

			if(nbAnswer >= players.size()-1 - newGhosts) {
				time = 0;
			}

			player.setNbPixels(answer.nbPixels);

			Player p;
			boolean inserted = false;

			for(int i = 0; i < firstAnswers.size(); i++) {
				p = firstAnswers.get(i);

				if(p.getNbPixels() > player.getNbPixels()) {
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


	/**
	 * On envoie les scores
	 * @param p Un joueur particulier
	 * @param except Selon la valeur de except, on envoie à tout le monde sauf p, ou bien on envoie qu'à p
	 */
	public void sendScores(Player p, boolean except) {
		Scores scores = new Scores();

		for(int i = 0; i < players.size(); i++) {
			scores.scores.add(new PlayerScore(players.get(i).getLogin(), players.get(i).getScore(), 
					players.get(i).hasFound(), players.get(i).isGhost(), players.get(i).getDrawing()));
		}

		Collections.sort(scores.scores, new Comparator<PlayerScore>() {
			@Override
			public int compare(PlayerScore p1, PlayerScore p2) {
				return Integer.signum(p2.score - p1.score);
			}
		});

		if(!except) {
			ObjectOutputStream out = p.getOutput();

			try {
				out.writeObject(scores);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			for(int i = 0; i < players.size(); i++) {
				if(p != players.get(i)) {
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

	/**
	 * On envoie les scores à tout le monde
	 */
	public void sendScores() {
		sendScores(null, true);
	}

	/**
	 * On lance la partie
	 * @return True si la partie est lancée, false sinon
	 */
	public boolean startGame() {
		if(players.size() >= 2) {
			started = true;
			return true;
		} else
			return false;
	}


	/**
	 * On affecte le mot à faire deviner et on arrête le timer du choix des mots
	 * @param word
	 */
	public void setWord(String word) {
		if(timer != null)
			timer.cancel();
		time = 0;
		this.word = convertToAnswerStringFormat(word);;
	}


	/**
	 * On enlève les accents et on remplace les majuscules par des miniscules dans la String s
	 * @param s La string à modifier
	 * @return La string modifiée
	 */
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
