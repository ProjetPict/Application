package model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


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
	
	/**
	 * Surcharge de la fonction run() de Thread. Incompl�te.
	 * Elle servira � g�rer l'authentification du joueur.
	 */
	public String connect(String login, String password)
	{
		if(connected){
			String res = disconnect();
			if(res.equals("success"))
				connected = false;
			else
				return res;
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
}
