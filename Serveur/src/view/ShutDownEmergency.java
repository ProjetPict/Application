package view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import server.Server;

public class ShutDownEmergency extends Thread {
	private static final int TIMER_PERIOD = 1000;
	private int max_count;
	private Console console;
	private Window window;
	private Server server;

	public ShutDownEmergency(int i, Console c, Window f, Server s) {
		max_count = i;
		console = c;
		window = f;
		server = s;
	}
	
	public ShutDownEmergency(int i) {
		max_count = i;
		console = null;
	}
	
	@Override
	public void run() {
		int count = max_count;
		if(console!=null) {
			window.setLockElements(false);
		}
		writeIn("\n> Préparation à l'extinction du serveur. Veuillez patienter...\n> Sauvegarde dans la base de données...");
		server.getDbInfos().saveDatabase();
		writeIn("Terminé !\n> Annonce aux joueurs de l'interruption serveur...");
		writeIn("Terminé !\n> Arrêt du serveur dans 10 secondes...");
		while(count>0) {
			try {
				sleep(TIMER_PERIOD);
			} catch (InterruptedException e) {}
			count--;
		}
		System.exit(0);
	}
	
	public void writeIn(String s) {
		if(console==null)
			System.out.println(s);
		else
			console.writeAnnonce(s);
	}
}
