package socketData;

import java.io.Serializable;

public class ChatCommand extends Command implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public String author;
	
	public ChatCommand(String msg, String author){
		this.command = msg;
		this.author = author;
	}
}
