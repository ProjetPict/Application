package socketData;

public class CreateJoinCommand extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public String name;
	public String password;
	public int pMax;
	public int turns;
	public int difficulty;

	
	/**
	 * Constructeur pour rejoindre une partie
	 * @param name
	 * @param password
	 */
	public CreateJoinCommand(String name, String password)
	{
		command = "joingame";
		this.name = name;
		setPassword(password);
		pMax = 0;
		turns = 0;
	}

	/**
	 * Constructeur pour créer une partie
	 * @param name
	 * @param password
	 * @param pMax
	 * @param turns
	 */
	public CreateJoinCommand(String name, String password, int pMax, int turns, int difficulty)
	{
		command = "creategame";
		this.name = name;
		setPassword(password);
		this.pMax = pMax;
		this.turns = turns;
		this.difficulty = difficulty;
	}
	
	private void setPassword(String password)
	{
		this.password = null;
		if(password != null)
		{
			if(password.length() >= 4)
				this.password = password;
			else
				this.password = null;
		}
	}
}
