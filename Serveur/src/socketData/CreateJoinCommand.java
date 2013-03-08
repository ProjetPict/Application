package socketData;

public class CreateJoinCommand extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public String name;
	public String password;
	public int pMax;

	public CreateJoinCommand(String name, String password, boolean join)
	{
		super();
		if(!join)
			command = "creategame";
		else
			command = "joingame";
		this.name = name;
		setPassword(password);
		pMax = 0;
	}

	public CreateJoinCommand(String name, String password, int pMax)
	{
		command = "creategame";

		this.name = name;
		setPassword(password);
		this.pMax = pMax;
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
