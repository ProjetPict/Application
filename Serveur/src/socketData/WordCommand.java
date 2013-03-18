package socketData;

public class WordCommand extends Command{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String word1;
	public String word2;
	public String word3;
	public int difficulty;
	
	public WordCommand(String word1, String word2, String word3, int difficulty)
	{
		this.command = "";
		this.word1 = word1;
		this.word2 = word2;
		this.word3 = word3;
		this.difficulty = difficulty;
	}

}
