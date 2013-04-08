package model;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import socketData.ChatCommand;
import socketData.Command;
import socketData.Line;
import socketData.Picture;
import socketData.Scores;
import socketData.ValueCommand;
import socketData.WordCommand;
import view.Main;


/**
 * Cette classe est un thread qui boucle pour recevoir les informations du serveur pendant une partie.
 * Sert également à envoyer des données de dessin au serveur, et contient une référence de l'image
 * en cours d'utilisation
 *
 */

public class GameObserver extends Thread {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Picture pict;
	private boolean running;


	/**
	 * Constructeur de GameObserver
	 * @param in
	 * @param out
	 */
	public GameObserver(ObjectInputStream in, ObjectOutputStream out) {
		this.in=in;
		this.out=out;
	}


	/**
	 * Surcharge de la fonction run() de Thread. Récupère les données envoyés par le serveur 
	 * et agit en conséquence
	 */
	public void run() {
		running = true;

		while(running) {
			try {
				Object obj=in.readObject();

				if(obj instanceof Point) {
					pict.addPoint((Point)obj);
				}
				else if(obj instanceof Line) {
					pict.addLine((Line) obj);
				}
				else if(obj instanceof Scores) {
					Main.getModel().processScores((Scores) obj);
				}
				else if(obj instanceof WordCommand) {
					Main.getModel().processWordCommand((WordCommand) obj);
				}
				else if(obj instanceof ValueCommand) {
					Main.getModel().processValueCommand((ValueCommand) obj);
				}
				else if(obj instanceof ChatCommand) {
					Main.getModel().processChatMsg((ChatCommand) obj);
				}
				else if(obj instanceof Command) {

					if(((Command) obj).command.equals("quitgame")) {
						running = false;
					}
					else
						Main.getModel().processCommand((Command) obj);
				}
				else if(obj instanceof Picture) {
					Main.getView().setPicture((Picture) obj);
				}
				else
					obj = null;

			} catch (IOException e) {
				running = false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				running = false;
			}
		}
	}


	/**
	 * On envoie un point du dessin au serveur
	 * @param p
	 * @return
	 */
	public Boolean sendDrawingData(Point p) {
		try {
			out.writeObject(p);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	

		return true;		
	}

	/**
	 * On envoie une nouvelle ligne au serveur
	 * @param line
	 * @return
	 */
	public Boolean sendNewLine(Line line) {
		try {
			out.writeObject(line);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @return L'instance de Picture utilisée par ce GameObserver 
	 */
	public Picture getPicture() {
		return pict;
	}

	/**
	 * 
	 * @param pict
	 */
	public void setPicture(Picture pict) {
		this.pict = pict;
	}
}
