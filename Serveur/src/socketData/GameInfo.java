package socketData;
import java.io.Serializable;

/**
 * Cette classe contient les informations d'une partie nécessaires à l'explorateur de partie du client.
 *
 */
public class GameInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String name;
	public boolean password;
	public int nbPlayers;
	public int maxPlayers;
	public boolean started;
	public int difficulty;
	
	
	public GameInfo(String name, int nbPlayers, int maxPlayers, boolean password, 
			boolean started, int difficulty)
	{
		this.name = name;
		this.nbPlayers = nbPlayers;
		this.maxPlayers = maxPlayers;
		this.password = password;
		this.started = started;
		this.difficulty = difficulty;
	}

}
