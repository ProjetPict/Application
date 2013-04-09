package socketData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Cette classe contient un arraylist de PlayerScore, elle ne sert qu'à éviter des problèmes de transfert d'Object.
 *
 */
public class Scores implements Serializable {

	private static final long serialVersionUID = 1L;

	public ArrayList<PlayerScore> scores;


	/**
	 * Constructeur de Scores
	 */
	public Scores() {
		scores = new ArrayList<PlayerScore>();
	}
}
