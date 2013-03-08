package socketData;

public class AnswerCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public double time;
	
	public AnswerCommand(String answer, double time)
	{
		command = answer;
		this.time = time;
	}
}
