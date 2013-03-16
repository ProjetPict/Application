package view;

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
		consoleCmd.writeAnswer("Erreur commande non reconnue.");
	}
	
}
