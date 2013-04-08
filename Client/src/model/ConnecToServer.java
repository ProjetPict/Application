package model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import socketData.*;


/**
 * G�re la connexion et l'envoi de requ�tes (creategame, joingame, quit...) au serveur
 *
 */
public class ConnecToServer {

	private Socket drawSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean connected;
	private String host;


	/**
	 * Constructeur de ConnecToServer
	 * @param host L'adresse du serveur
	 */
	public ConnecToServer(String host) {
		connected = false;
		this.host = host;	
	}


	/**
	 * Envoie les donn�es n�cessaires � l'authentification et r�cup�re la r�ponse
	 * @param login
	 * @param password
	 * @return
	 */
	public boolean connect(String login, String password) {

		Command log = null;
		Command pass = null;

		try {
			byte[] hash = MessageDigest.getInstance("SHA1").digest(password.getBytes());
			StringBuilder hashString = new StringBuilder();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(hash[i]);

				if (hex.length() == 1) {
					hashString.append('0');
					hashString.append(hex.charAt(hex.length() - 1));
				}
				else
					hashString.append(hex.substring(hex.length() - 2));
			}

			log = new Command(login);
			pass = new Command(hashString.toString());

		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(connected) {
			return false;
		}

		try {
			drawSocket = new Socket(host ,8448);
			out = new ObjectOutputStream(drawSocket.getOutputStream());
			in = new ObjectInputStream(drawSocket.getInputStream());
		} catch (IOException e) {
			return false;
		}

		try {
			//attente du signal d'envoi et envoi du login
			out.writeObject(log);
			out.flush();
			out.writeObject(pass);
			out.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}

		//attente de la confirmation de connexion
		try {
			Object conf = in.readObject();
			if(!(conf instanceof Command))
				throw new ClassNotFoundException(); 

			if(((Command)conf).command.equals("success")) {
				connected = true;
				return true;
			}
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}


	/**
	 * Envoie les informations n�c�ssaires au serveur pour rejoindre une partie
	 * et r�cup�re la r�ponse
	 * @param name Le nom de la partie � rejoindre
	 * @param password Le mot de passe, peut �tre null
	 * @return
	 */
	public String joinGame(String name, String password) {

		CreateJoinCommand com = new CreateJoinCommand(name, password);
		Command result = null;

		try {
			out.writeObject(com);
			out.flush();
			result = (Command)in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.command;
	}


	/**
	 * Envoie une commande pour se d�connecter du serveur
	 * @return "success" ou "fail" selon la r�ponse du serveur
	 */
	public String disconnect() {

		Command com = new Command("quit");
		if(!connected)
			return "fail";

		try {
			out.writeObject(com);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		connected = false;
		return "success";
	}


	/**
	 * Renvoie la liste des parties en cours
	 * @return GameList
	 */
	public GameList getGameList() {

		Command com = new Command("getlist");
		GameList res = null;

		try {
			out.writeObject(com);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		try {
			res = (GameList) in.readObject();
		} catch (IOException e) {
			//En cas de probl�me de connexion
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			//Au cas o� le serveur n'envoie pas une donn�e du bon type
			e.printStackTrace();
			return null;
		}

		return res;
	}


	/**
	 * Demande la cr�ation d'une partie au serveur
	 * @return La Command envoy� par le serveur
	 */
	public String createGame(String name, String password, int pMax, 
			int turns, int difficulty) {

		CreateJoinCommand com = new CreateJoinCommand(name, password, pMax, turns, difficulty);
		Object conf=null;

		try {
			out.writeObject(com);
			out.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}

		try {
			conf = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return ((Command)conf).command;
	}


	/**
	 * 
	 * @return L'input stream de la connexion.
	 */
	public ObjectInputStream getInput() {
		return in;
	}


	/**
	 * 
	 * @return L'output stream de la connexion.
	 */
	public ObjectOutputStream getOutput() {
		return out;
	}
}
