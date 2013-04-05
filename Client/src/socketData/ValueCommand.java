package socketData;

public class ValueCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int value;
	
	public ValueCommand(String command, int value)
	{
		super(command);
		
		this.value = value;
	}
}
