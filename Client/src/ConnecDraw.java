import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class ConnecDraw extends Thread{
	private Socket drawSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Scanner sc;
	
	private boolean connected =false;
	private String login;
	
	
	
	public ConnecDraw(String host, String login)
	{
		this.login = login;
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
	public void run()
	{
		while(!connected){
			
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
				
				try{
					Object conf = in.readObject();
					if(conf instanceof String)
						System.out.println(conf);
					else
						throw new ClassNotFoundException(); 
					//si la tentative de connexion a réussi
					
					if(conf.equals("success")){
						connected=true;
					}
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
}
