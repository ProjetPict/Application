package socketData;
import java.io.Serializable;

/**
 * Cette classe contient les informations d'une partie nécessaires à l'explorateur de partie du client.
 * @author Jerome
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
	
	
	public GameInfo(String name, int nbPlayers, int maxPlayers, boolean password, boolean started)
	{
		this.name = name;
		this.nbPlayers = nbPlayers;
		this.maxPlayers = maxPlayers;
		this.password = password;
		this.started = started;
	}

}
