package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import socketData.Command;

/**
 * Gère les nouvelles connexions dans un Thread à part du Server.
 * @author Jerome
 *
 */
public class Connection extends Thread {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	/**
	 * 
	 * @param socket
	 */
	public Connection(Socket socket)
	{
		this.socket = socket;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	 * Surcharge de la fonction run() de Thread. Incomplète.
	 * Elle servira à gérer l'authentification du joueur.
	 */
	public void run()
	{

		try {

			System.out.println("Quelqu'un s'est connecté");

			Object login;
			try {
				login = in.readObject();
				if(login instanceof Command)
				{
					Server.createPlayer(((Command)login).command, socket, this);
					out.writeObject(new Command("success"));
					System.out.println(login +" vient de se connecter ");
					out.flush();
				}	

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}