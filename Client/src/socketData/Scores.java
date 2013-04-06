package socketData;

import java.io.Serializable;
import java.util.ArrayList;

public class Scores implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ArrayList<PlayerScore> scores;
	
	public Scores()
	{
		scores = new ArrayList<PlayerScore>();
	}

}
