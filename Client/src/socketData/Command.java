package socketData;

import java.io.Serializable;

public class Command implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String command;
	
	
	protected Command(){
		command = "";
	}
	
	
	public Command(String command)
	{
		this.command = command;
	}
	
	

}
