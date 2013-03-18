package socketData;

public class AnswerCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public long nbPixels;
	
	public AnswerCommand(String answer, long nbPixels)
	{
		command = answer;
		this.nbPixels = nbPixels;
	}
}
