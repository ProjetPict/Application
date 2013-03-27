package view;

import server.Server;

public class ShutDownEmergency extends Thread {
	private static final int TIMER_PERIOD = 1000;
	private int max_count;
	private Console console;
	private Server serverInfos;
	private boolean shutDown;

	public ShutDownEmergency(int i, Console c, Server s, boolean b) {
		max_count = i;
		console = c;
		serverInfos = s;
		shutDown = b;
	}
	
	@Override
	public void run() {
		int count = max_count;
		int sub = 13;
		String s;
		console.writeAnnonce("\n> Pr�paration � l'extinction du serveur. Veuillez patienter...\n> Sauvegarde dans la base de donn�es...");
		serverInfos.getDbInfos().saveDatabase();
		console.writeAnnonce("Termin� !\n> Annonce aux joueurs de l'interruption serveur...");
		console.writeAnnonce("Termin� !\n> Arr�t du serveur dans 9 secondes...");
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
		if(shutDown)
			System.exit(0);
	}
}
