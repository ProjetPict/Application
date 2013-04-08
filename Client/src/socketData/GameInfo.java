package socketData;
import java.io.Serializable;

/**
 * Cette classe contient les informations d'une partie nécessaires à l'explorateur de partie du client.
 *
 */
public class GameInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	public String name;
	public boolean password;
	public int nbPlayers;
	public int maxPlayers;
	public boolean started;
	public int difficulty;


	/**
	 * Constructeur de GameInfo
	 * @param name Le nom de la partie
	 * @param nbPlayers Le nombre de joueurs dans la partie
	 * @param maxPlayers Le nombre de joueurs maximum de la partie
	 * @param password True s'il y a un mot de passe, false sinon
	 * @param started True si la partie a démarré, false sinon
	 * @param difficulty La difficulté de la partie
	 */
	public GameInfo(String name, int nbPlayers, int maxPlayers, boolean password, 
			boolean started, int difficulty) {

		this.name = name;
		this.nbPlayers = nbPlayers;
		this.maxPlayers = maxPlayers;
		this.password = password;
		this.started = started;
		this.difficulty = difficulty;
	}
}
