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
	private Server serverInfos;
	private boolean shutDown;
	private Fenetre window;
	private JPanel fadeEffect;

	public ShutDownEmergency(int i, Console c, Server s, boolean b, Fenetre f) {
		max_count = i;
		console = c;
		serverInfos = s;
		shutDown = b;
		window = f;
	}
	
	@Override
	public void run() {
		window.setLockElements(false);
		int count = max_count;
		int sub = 13;
		String s;
		fadeEffect = new JPanel() {
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g) {  
				g.setColor(new Color(0, 0, 0, 0.7f));  
				g.fillRect(0, 0, window.getSize().width, window.getSize().height);  
			}  
		};
		JFrame frame = new JFrame();
		if(!shutDown) {
			fadeEffect.setOpaque(false);
			window.setGlassPane(fadeEffect);
        	fadeEffect.setVisible(true);
        	JPanel pan = new JPanel();
			frame.setUndecorated(true);
			frame.setSize(350,50);
			frame.setLocationRelativeTo(window);
			pan.add(new JLabel("Le serveur est en train de s'éteindre."));
			pan.add(new JLabel("Merci de patienter le temps de la sauvegarde..."));
			frame.add(pan);
			frame.setVisible(true);
		}
		console.writeAnnonce("\n> Préparation à l'extinction du serveur. Veuillez patienter...\n> Sauvegarde dans la base de données...");
		serverInfos.getDbInfos().saveDatabase();
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
		if(shutDown)
			System.exit(0);
    	fadeEffect.setVisible(false);
		frame.dispose();
		window.setLockElements(true);
		window.downComplete();
	}
}
