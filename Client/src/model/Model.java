package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Observable;

import socketData.AnswerCommand;
import socketData.Command;
import socketData.ChatCommand;
import socketData.GameList;
import socketData.PlayerScore;
import socketData.WordCommand;
import view.Main;

/**
 * Gere les connexions avec le serveur.
 * @author christopher cacciatore
 *
 */
public class Model extends Observable{
	private ConnecToServer connec;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private GameObserver go;
	private Map<String, PlayerScore> scores;
	private String word;
	
	
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
	
	public String getWord()
	{
		return word;
	}
	
	public void processCommand(Command command)
	{
		if(command.command.equals("startturn"))
		{
			Main.getView().startTurn();
		}
		else if(command.command.equals("endturn"))
		{
			Main.getView().endTurn();
		}
		else if(command.command.equals("endgame"))
		{
			setChanged();
			this.notifyObservers(false);
			setChanged();
			this.notifyObservers("endgame");
		}
		else if(command.command.equals("startgame"))
		{
			setChanged();
			this.notifyObservers("startgame");
		}
		else if(command.command.equals("startdraw"))
		{
			setChanged();
			this.notifyObservers(true);
		}
		else if(command.command.equals("stopdraw"))
		{
			setChanged();
			this.notifyObservers(false);
		}
	}
	
	public void processChatMsg(ChatCommand msg){
		setChanged();
		this.notifyObservers(msg);
	}
	
	public void processWordCommand(WordCommand command)
	{
		if(command.command.equals(""))
		{
			Main.getView().chooseWord(command);	
		}
		else{
			word = command.command;
			Main.getView().closeDialog();
		}
	}
	
	public void sendAnswer(String answer, long nbPixels)
	{
		AnswerCommand ans = new AnswerCommand(answer, nbPixels);
		
		sendCommand(ans);
		
	}
	
	public void sendCommand(String command)
	{
		sendCommand(new Command(command));
	}
	
	public void sendCommand(Command command)
	{
		try {
			out.writeObject(command);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendChatMessage(String msg)
	{
		sendCommand(new ChatCommand(msg, Main.player));
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
	
	public String createGame(String name, String password, int pMax, 
			int turns, int difficulty){
		String res = connec.createGame(name, password, pMax, turns, difficulty);
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
