package model;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

import socketData.DrawingData;


public class Reception implements Runnable{
	
	private ObjectInputStream in;
	private DrawingData drwData;
	private String message;
	
	public Reception(ObjectInputStream in){
		this.in = in;
	}
	
	public void run(){
		while(true){
			try{
				Object obj = in.readObject();

				//si l'objet reçu est un DrawingData
				if(obj instanceof DrawingData){
					drwData = new DrawingData(((DrawingData) obj).x, ((DrawingData) obj).y, ((DrawingData) obj).size, ((DrawingData) obj).color);
					System.out.print("DrawingData reçue : \n" + drwData.toString() + "\n");
				}//si c'est un message (String)
				else if(obj instanceof String){
					message = (String)obj;
					System.out.print("Message reçu : \n" + message + "\n");
				}
				else //si c'est autre chose, il y a un problème
					throw new ClassNotFoundException();
			}catch(EOFException e){
				System.out.print("Vous êtes déconnecté du serveur\n");
				break;
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}			
		}
	}

}
