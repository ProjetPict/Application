package model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import socketData.GameList;


/**
 * Gère la connexion et l'envoi de requêtes (creategame, joingame, quit...) au serveur
 * @author christopher
 *
 */
public class ConnecToServer{
	private Socket drawSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean connected = false;
	
	
	public ConnecToServer(String host)
	{
		try{
			drawSocket = new Socket(host ,8448);
			out = new ObjectOutputStream(drawSocket.getOutputStream());
			in = new ObjectInputStream(drawSocket.getInputStream());
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String connect(String login, String password)
	{
		if(connected){
			return "already connected";
		}
		
		try{
			//attente du signal d'envoi et envoi du login
			try{
				System.out.println(in.readObject());
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
			out.writeObject(login);
			out.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}

		//attente de la confirmation de connexion
		try{
			Object conf = in.readObject();
			if(conf instanceof String)
				System.out.println(conf);
			else
				throw new ClassNotFoundException(); 
		
			if(conf.equals("success")){
				connected = true;
				return (String)conf;
			}
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			return "fail";
		}catch(IOException e) {
			e.printStackTrace();
			return "fail";
		}
		return "fail";
	}
	
	
	public String disconnect(){
		
		if(!connected)
			return "fail";
		
		String conf = "";
		try{
			out.writeObject("quit");
			out.flush();
			conf = (String)in.readObject();
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		if(conf.equals("success"))
			connected = false;
		
		return (String)conf;
	}
	
	/**
	 * Renvoie la liste des parties en cours
	 * @return
	 */
	public GameList getGameList(){
		Object res = null;
		try {
			out.writeObject("getlist");
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
	public String createGame(String name){
		Object conf=null;
		
		try{
			out.writeObject("creategame%"+name);
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
		
		return (String)conf;
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
