import java.util.ArrayList;

public class Commander extends Thread {
	ArrayList<String> myHistory;
	private boolean myStopSignal;
	
	public Commander() {
		myHistory = new ArrayList<String>();
		myStopSignal = false;
	}    
	
	@Override
	public void run() {
		while(!myStopSignal) {
			System.out.println("Test");
		}
	}
	
	public void askCommand(String cmd) {
		if(cmd.equals("shutdown")) {
			System.out.println("> Voulez vous vraiment éteindre le serveur ? (encore X parties en cours et X utilisateurs connectés) [O/N]");
			if(1==1)
				shutDown();				
		}
		else if(cmd.equals("help")) {
			System.out.println("> Liste des commandes :");
		}
		else {
			System.out.println("> Commande invalide. Tapez \"help\" pour la liste des commandes.");
		}
	}
	
	public void shutDown() {
		myStopSignal = true;
		System.out.println("> Extinction du serveur en cours...");
		System.out.println("> Annonce de l'arrêt des parties en cours aux clients.");
		notifyClients("Arrêt des serveurs dans 1min30.");
		System.out.println("> Sauvegarde de la base de données.");
		saveDatabase();
	}
	
	public void notifyClients(String s) {
		
	}
	
	public void saveDatabase() {
		
	}
	
}
