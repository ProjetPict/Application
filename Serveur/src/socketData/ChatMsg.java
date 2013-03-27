package socketData;

import java.io.Serializable;
/**
 * 
 * @author Quentin
 *
 */
public class ChatMsg implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String msg;
	public String author;
	
	public ChatMsg(String msg, String author){
		this.msg = msg;
		this.author = author;
	}
}
