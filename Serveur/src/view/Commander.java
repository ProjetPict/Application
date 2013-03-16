package view;

import java.util.ArrayList;

import server.Server;

/**
 * Cette classe permet la gestion du serveur par des commandes
 * @author Matthieu
 *
 */
public class Commander {
	
	private Server serverInfos;
	private Console consoleCmd;
	
	public Commander(Console conso, Server serv) {
		serverInfos = serv;
		consoleCmd = conso;
		
	}
	
	public void askCmd(String s) {
		if(s.equals("historique")) {
			ArrayList<String> historique = consoleCmd.getHistory();
			String a = "";
			if(historique.size()-1==0)
				a+= "Aucun élément dans l'historique.";
			else {
				a+= historique.size()+" éléments dans l'historique";
				for(int i=0; i<historique.size(); i++)
					a+="\n>"+i+". "+historique.get(i);
			}
			consoleCmd.writeAnswer(a);
		}
		else if(s.equals("clear-historique")) {
			consoleCmd.resetHistory();
			consoleCmd.writeAnswer("Historique vidé.\n");
		}
		else {
			consoleCmd.writeAnswer("Commande non reconnue.");
		}
			
	}
}
