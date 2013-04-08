package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import socketData.Command;

public class PlayerNumberTest {
	private Socket drawSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private static String host = "localhost" ;
	
	/**
	 * @param args
	 */

	
	
	public boolean connect(String login, String password)
	{
		
		
		Command log = new Command(login);
		Command pass = new Command(password);
		
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

}
