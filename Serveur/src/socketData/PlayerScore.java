package socketData;

import java.io.Serializable;

/**
 * Contient les informations nécessaires pour l'affichage des scores dans le client
 *
 */
public class PlayerScore implements Serializable {

	private static final long serialVersionUID = 1L;

	public String login;
	public int score;
	public boolean hasFound;
	public boolean isGhost;
	public boolean drawing;


	/**
	 * Constructeur de PlayerScore
	 * @param login Le nom du joueur
	 * @param score Le score du joueur
	 * @param hasFound True si le joueur a trouvé le mot, false sinon
	 * @param ghost True si le joueur est "fantôme", false sinon
	 * @param drawing True si le joueur est le dessinateur, false sinon
	 */
	public PlayerScore(String login, int score, boolean hasFound, boolean ghost, boolean drawing) {
		this.login = login;
		this.score = score;
		this.hasFound = hasFound;
		this.isGhost = ghost;
		this.drawing = drawing;
	}

	public String toString() {
		return login + " : " + score + " pts";
	}
}
