package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import socketData.GameList;
import view.Main;

/**
 * Gere les connexions avec le serveur.
 * @author christopher
 *
 */
public class Model{
	private ConnecToServer Connec;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private GameObserver go;
	
	
	public Model(String host){
		Connec = new ConnecToServer(host);
		go = null;
	}
	
	public boolean connect(String login, String password){
		boolean res = Connec.connect(login, password);
		if(res)
		{
			out = Connec.getOutput();
			in = Connec.getInput();
			go = new GameObserver(in, out);
		}
		return res; 
	}
	
	public void disconnect(){
		Connec.disconnect();
	}
	
	public GameList getGameList(){
		return Connec.getGameList();
	}
	
	public String createGame(String name, String password, int Pmax){
		return Connec.createGame(name, password, Pmax);
	}
	
	public String joinGame(String name, String password)
	{
		return Connec.joinGame(name, password);
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
