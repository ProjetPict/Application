package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import socketData.AnswerCommand;
import socketData.GameList;

/**
 * Gere les connexions avec le serveur.
 * @author christopher
 *
 */
public class Model{
	private ConnecToServer connec;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private GameObserver go;
	
	
	public Model(String host){
		connec = new ConnecToServer(host);
		go = null;
	}
	
	public boolean connect(String login, String password){
		boolean res = connec.connect(login, password);
		if(res)
		{
			out = connec.getOutput();
			in = connec.getInput();
			go = new GameObserver(in, out);
		}
		return res; 
	}
	
	public void disconnect(){
		connec.disconnect();
	}
	
	public void sendAnswer(String answer)
	{
		AnswerCommand ans = new AnswerCommand(answer, 62.0);
		
		try {
			out.writeObject(ans);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public GameList getGameList(){
		return connec.getGameList();
	}
	
	public String createGame(String name, String password, int Pmax){
		return connec.createGame(name, password, Pmax);
	}
	
	public String joinGame(String name, String password)
	{
		return connec.joinGame(name, password);
	}
	
	public ObjectOutputStream getOutput(){
		return out;
	}
	
	public ObjectInputStream getInput(){
		return in;
	}
	
	public GameObserver getGameObserver()
	{
		return go;
	}
	
}
