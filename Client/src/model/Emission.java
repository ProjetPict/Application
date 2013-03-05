package model;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class Emission implements Runnable{

	private ObjectOutputStream out;
	
	public Emission(ObjectOutputStream out){
		this.out = out;
	}
	
	public void run(){
		// a des fins de test, on va demander la creation d'une partie
		// puis, on va la rejoindre.
		// enfin, on va envoyer des messages et des DrawingData
		
		try{
			Thread.currentThread();
			//do what you want to do before sleeping
			Thread.sleep(3000);//sleep for 3000 ms
			//do what you want to do after sleeptig
		}
		catch(InterruptedException ie){
			//If this thread was intrrupted by nother thread
		}
		
		
		//on demande la création d'une partie nommée partie1:
		try{
			System.out.print("Demande création partie1 : ");
			out.writeObject("creategame%partie1");
		}catch(IOException e){
			e.printStackTrace();
		}
		//la confirmation est reçue par le thread "Reception"
		
		try{
			Thread.currentThread();
			//do what you want to do before sleeping
			Thread.sleep(10000);//sleep for 1000 ms
			//do what you want to do after sleeptig
		}
		catch(InterruptedException ie){
			//If this thread was intrrupted by nother thread
		}
		
		
		
		//on quitte le jeu
		System.out.print("Demande pour quitter le jeu : ");
		try{
			out.writeObject("quit");
		}catch(IOException e){
			e.printStackTrace();
		}
		//la confirmation est reçue par le thread "Reception"
		

	}
	
}
