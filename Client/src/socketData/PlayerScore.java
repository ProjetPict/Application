package socketData;

import java.io.Serializable;

public class PlayerScore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String login;
	public int score;
	public boolean hasFound;
	public boolean isGhost;
	public boolean drawing;
	
	public PlayerScore(String login, int score, boolean hasFound, boolean ghost, boolean drawing)
	{
		this.login = login;
		this.score = score;
		this.hasFound = hasFound;
		this.isGhost = ghost;
		this.drawing = drawing;
	}

	public String toString()
	{
		return login + " : " + score + " pts";
	}
}
