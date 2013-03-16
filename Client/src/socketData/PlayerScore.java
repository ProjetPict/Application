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
	
	public PlayerScore(String login, int score, boolean hasFound)
	{
		this.login = login;
		this.score = score;
		this.hasFound = hasFound;
	}

}
