import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import socketData.DrawingData;

/**
 * G�re une partie et ses joueurs.
 * @author Jerome
 *
 */
public class Game extends Thread{

	private ArrayList<Player> players;
	private String name;
	private String password;
	private int pMax;
	private boolean running;
	private boolean started; //True si la partie a demarr�


	/**
	 * 
	 * @param creator
	 * @param name
	 * @param password
	 */
	public Game(Player creator, String name, String password){
		this.name = name;
		this.password = password; 	//vide si publique 
		players = new ArrayList<Player>();
		players.add(creator);
		pMax = Server.MAX_PLAYER;					//valeur par d�faut ?
		started = false;
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
		if(players.size() > 0)
			System.out.println("Test de la partie de " + players.get(0).getLogin());
		while(running){

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
			players.add(p);
			return true;
		}
		return false;
	}


	/**
	 * Supprime un joueur de la partie
	 * @param player
	 */
	public void removePlayer(Player player){
		players.remove(player);
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
	 * @param data
	 * @param sender
	 */
	public void sendData(DrawingData data, Player sender)
	{
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


	public boolean startGame() {

		if(players.size() >= 2)
		{
			started = true;
			return true;
		}
		else
			return false;
	}
}
