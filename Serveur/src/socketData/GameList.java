package socketData;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Cette classe contient un arraylist de GameInfo, elle ne sert qu'� �viter des probl�mes de transfert d'Object.
 *
 */
public class GameList implements Serializable {

	private static final long serialVersionUID = 1L;
	public ArrayList<GameInfo> games;

	/**
	 * Constructeur de GameList
	 */
	public GameList() {
		games = new ArrayList<GameInfo>();
	}

	/**
	 * Constructeur de GameList
	 * @param games
	 */
	public GameList(ArrayList<GameInfo> games) {
		this.games = games;
	}
}
