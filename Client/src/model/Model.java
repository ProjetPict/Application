package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import socketData.GameList;

/**
 * Gère les connexions avec le serveur.
 * @author christopher
 *
 */
public class Model{
	private ConnecToServer Connec;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public Model(String host){
		Connec = new ConnecToServer(host);
		out = Connec.getOutput();
		in = Connec.getInput();

	}
	
	public String connect(String login, String password){
		return Connec.connect(login, password);
	}
	
	public void disconnect(){
		Connec.disconnect();
	}
	
	public GameList getGameList(){
		return Connec.getGameList();
	}
	
	public String createGame(String name){
		return Connec.createGame(name);
	}
	
	public ObjectOutputStream getOutput(){
		return out;
	}
	
	public ObjectInputStream getInput(){
		return in;
	}
	
}
