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
	
	public PlayerScore(String login, int score, boolean hasFound, boolean ghost)
	{
		this.login = login;
		this.score = score;
		this.hasFound = hasFound;
		this.isGhost = ghost;
	}

	public String toString()
	{
		return login + " : " + score;
	}
}
