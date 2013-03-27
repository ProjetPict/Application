package model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import socketData.*;


/**
 * Gère la connexion et l'envoi de requêtes (creategame, joingame, quit...) au serveur
 * @author christopher
 *
 */
public class ConnecToServer{
	private Socket drawSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean connected;
	private String host;
	
	
	public ConnecToServer(String host)
	{
		connected = false;
		this.host = host;
		
	}
	
	
	public boolean connect(String login, String password)
	{
		
		
		Command log = new Command(login);
		Command pass = new Command(password);
		
		if(connected){
			return false;
		}
		
		try{
			drawSocket = new Socket(host ,8448);
			out = new ObjectOutputStream(drawSocket.getOutputStream());
			in = new ObjectInputStream(drawSocket.getInputStream());
		}catch (IOException e) {
			return false;
		}
		
		try{
			//attente du signal d'envoi et envoi du login
			
			out.writeObject(log);
			out.flush();
			out.writeObject(pass);
			out.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}

		//attente de la confirmation de connexion
		try{
			Object conf = in.readObject();
			if(!(conf instanceof Command))
				throw new ClassNotFoundException(); 
	
			if(((Command)conf).command.equals("success")){
				connected = true;
				return true;
			}
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			return false;
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public String joinGame(String name, String password)
	{
		CreateJoinCommand com = new CreateJoinCommand(name, password);
		
		Command result = null;
		try{
			out.writeObject(com);
			out.flush();
			result = (Command)in.readObject();
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return result.command;
	}
	
	
	public String disconnect(){
		
		Command com = new Command("quit");
		if(!connected)
			return "fail";
		
		
		try{
			out.writeObject(com);
			out.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
		connected = false;
		
		return "success";
	}
	
	/**
	 * Renvoie la liste des parties en cours
	 * @return
	 */
	public GameList getGameList(){
		Command com = new Command("getlist");
		Object res = null;
		try {
			out.writeObject(com);
			out.flush();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
			return null;
		}
		
		try {
			res = in.readObject();
		} catch (IOException e) {
			//En cas de problème de connexion
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			//Au cas où le serveur n'envoie pas une donnée du bon type
			e.printStackTrace();
			return null;
		}
		
		return (GameList)res;
		
	}
	
	/**
	 * Demande la création d'une partie au serveur
	 * @param name
	 * @return
	 */
	public String createGame(String name, String password, int pMax, 
			int turns, int difficulty){
		CreateJoinCommand com = new CreateJoinCommand(name, password, pMax, turns, difficulty);
		Object conf=null;
		
		try{
			out.writeObject(com);
			out.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		try {
			conf = in.readObject();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
		return ((Command)conf).command;
	}
	
	
	/**
	 * 
	 * @return L'input stream de la connexion.
	 */
	public ObjectInputStream getInput()
	{
		return in;
	}

	
	/**
	 * 
	 * @return L'output stream de la connexion.
	 */
	public ObjectOutputStream getOutput(){
		return out;
	}
}
