package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Observable;

import socketData.AnswerCommand;
import socketData.GameList;
import socketData.PlayerScore;

/**
 * Gere les connexions avec le serveur.
 * @author christopher
 *
 */
public class Model extends Observable{
	private ConnecToServer connec;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private GameObserver go;
	private Map<String, PlayerScore> scores;
	
	
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
	
	public void sendAnswer(String answer, long nbPixels)
	{
		AnswerCommand ans = new AnswerCommand(answer, nbPixels);
		
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
	
	public void addPlayerScore(PlayerScore ps)
	{
		scores.put(ps.login, ps);
		setChanged();
		notifyObservers();
	}
	
	public Map<String, PlayerScore> getScores()
	{
		return scores;
	}
	
	public String createGame(String name, String password, int pMax, int turns){
		String res = connec.createGame(name, password, pMax, turns);
		if(res.equals("success"))
			scores = new Hashtable<String, PlayerScore>();
		return res;
	}
	
	public String joinGame(String name, String password)
	{
		String res = connec.joinGame(name, password);
		if(res.equals("success"))
			scores = new Hashtable<String, PlayerScore>();
		return res;
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
