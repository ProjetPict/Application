package socketData;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Cette classe contient un arraylist de GameInfo, elle ne sert qu'à éviter des problèmes de transfert d'Object.
 *
 */
public class GameList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<GameInfo> games;
	
	public GameList()
	{
		games = new ArrayList<GameInfo>();
	}
	
	public GameList(ArrayList<GameInfo> games)
	{
		this.games = games;
	}
}
