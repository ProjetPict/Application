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
	private Window window;
	private JFrame frame;
	private JPanel pan;
	private JPanel fadeEffect;
	private JLabel saveLbl = new JLabel("Sauvegarde du serveur en cours...");
	private JLabel countDownLbl = new JLabel();

	public ShutDownEmergency(int i, Console c, Server s, boolean b, Window f) {
		max_count = i;
		console = c;
		serverInfos = s;
		shutDown = b;
		window = f;
		countDownLbl.setText("Merci de patienter.");
	}
	
	public ShutDownEmergency(int i, Server s) {
		max_count = i;
		console = null;
		serverInfos = s;
		shutDown = true;
	}
	
	@Override
	public void run() {
		int count = max_count;
		if(console!=null) {
			window.setLockElements(false);
			fadeEffect = new JPanel() {
				private static final long serialVersionUID = 1L;
				public void paintComponent(Graphics g) {  
					g.setColor(new Color(0, 0, 0, 0.7f));  
					g.fillRect(0, 0, window.getSize().width, window.getSize().height);  
				}  
			};
			frame = new JFrame();
			if(!shutDown) {
				fadeEffect.setOpaque(false);
				pan = new JPanel();
				window.setGlassPane(fadeEffect);
	        	fadeEffect.setVisible(true);
				frame.setUndecorated(true);
				frame.setSize(350,75);
				frame.setLocationRelativeTo(window);
				pan.add(new JLabel("Le serveur est en train de s'éteindre."));
				pan.add(saveLbl);
				pan.add(countDownLbl);
				frame.add(pan);
				frame.setVisible(true);
			}
		}
		writeIn("\n> Préparation à l'extinction du serveur. Veuillez patienter...\n> Sauvegarde dans la base de données...");
		serverInfos.getDbInfos().saveDatabase();
		saveLbl.setText("Sauvegarde du serveur effectuée !");
		writeIn("Terminé !\n> Annonce aux joueurs de l'interruption serveur...");
		writeIn("Terminé !\n> Arrêt du serveur dans 9 secondes...");
		while(count>0) {
			if(!shutDown) {
				countDownLbl.setText("Arrêt du serveur dans "+count+" seconde(s).");
			}
			try {
				this.sleep(TIMER_PERIOD);
			} catch (InterruptedException e) {}
			count--;
		}
		if(shutDown)
			System.exit(0);
		if(console!=null) {
			fadeEffect.setVisible(false);
			frame.dispose();
			window.setLockElements(true);
			window.downComplete();
		}
	}
	
	public void writeIn(String s) {
		if(console==null)
			System.out.println(s);
		else
			console.writeAnnonce(s);
	}
}
