package view;

import server.Server;

public class ShutDownEmergency extends Thread {
	private static final int TIMER_PERIOD = 1000;
	private int max_count;
	private Console console;
	private Server serverInfos;

	public ShutDownEmergency(int i, Console c, Server s) {
		max_count = i;
		console = c;
		serverInfos = s;
	}
	
	@Override
	public void run() {
		int count = max_count;
		int sub = 13;
		String s;
		console.writeAnnonce("\n> Préparation à l'extinction du serveur. Veuillez patienter...\n> Sauvegarde dans la base de données...");
		serverInfos.getDbInfos().saveStatistiques();
		console.writeAnnonce("Terminé !\n> Annonce aux joueurs de l'interruption serveur...");
		console.writeAnnonce("Terminé !\n> Arrêt du serveur dans 9 secondes...");
		while(count>0) {
			try {
				this.sleep(TIMER_PERIOD);
			} catch (InterruptedException e) {}
			count--;
			s = console.getConsoleContent();
			if(count>1)
				console.rewriteConsoleContent(s.substring(0, s.length()-sub)+count+" secondes...");
			else
				console.rewriteConsoleContent(s.substring(0, s.length()-sub)+count+" seconde...");
			console.getConsole().selectAll();
		}
		System.exit(0);
	}
}
