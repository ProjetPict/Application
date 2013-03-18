package view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import server.Server;

/**
 * Gère l'interface, la fenetre et la communication entre cette interface et le serveur
 * @author Matthieu
 *
 */
public class Fenetre extends JFrame implements WindowListener {
	private Server serverInfos;
	private Console console;
	private Monitor monitor = new Monitor();
	private ClipboardManager clipManage = new ClipboardManager();
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fichier = new JMenu("Fichier");
	private JMenu edition = new JMenu("Edition");
	private JMenu maintenance = new JMenu("Maintenance");
	private JMenu aide = new JMenu("Aide");
	 
	private JMenuItem start = new JMenuItem("Lancer le serveur");
	private JMenuItem restart = new JMenuItem("Relancer le serveur");
	private JMenuItem stop = new JMenuItem("Arrêter le serveur");
	private JMenuItem close = new JMenuItem("Fermer");

	private JMenuItem history = new JMenuItem("Historique des commandes");
	private JMenuItem copy = new JMenuItem("Copier le contenu de la ligne de commande");
	private JMenuItem paste = new JMenuItem("Coller dans la ligne de commande");
	private JMenuItem cut = new JMenuItem("Couper le contenu de la ligne de commande");

	private JMenuItem annonce = new JMenuItem("Créer une annonce");
	private JMenuItem save = new JMenuItem("Sauvegarder vers MySQL");
	private JMenuItem savelogs = new JMenuItem("Exporter le journal de logs");
	private JMenuItem seelogs = new JMenuItem("Voir le journal de logs");
	
	private JMenuItem about = new JMenuItem("A propos");
	private JMenuItem commands = new JMenuItem("Liste des commandes disponibles");
	private JMenuItem infos = new JMenuItem("Obtenir les informations du serveur");

	public Fenetre(Server servInfo){
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("DrawVS - Server app");
		this.setLocation(new Point(100,100));
		this.setResizable(false);
		this.setSize(900, 700);
		this.setJMenuBar(menuBar);
		this.serverInfos = servInfo;
		console = new Console(servInfo);
		
		menuBar.add(fichier);
		menuBar.add(edition);
		menuBar.add(maintenance);
		menuBar.add(aide);
		
		fichier.add(start);
		fichier.add(restart);
		fichier.add(stop);
		fichier.addSeparator();
		fichier.add(close);
		start.setEnabled(false);

		edition.add(history);
		edition.addSeparator();
		edition.add(copy);
		edition.add(cut);
		edition.add(paste);
		if(clipManage.getClipboardContents().length()==0)
			paste.setEnabled(false);

		maintenance.add(annonce);
		maintenance.addSeparator();
		maintenance.add(save);
		maintenance.add(savelogs);
		maintenance.addSeparator();
		maintenance.add(seelogs);
		
		aide.add(about);
		aide.addSeparator();
		aide.add(commands);
		aide.add(infos);
		
		JSplitPane pan = new JSplitPane();
		pan.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.add(pan);
		pan.setDividerLocation(430);
		pan.setTopComponent(monitor);
		pan.setBottomComponent(console);
		
		close.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				try {
					closeServer();
				} catch (InterruptedException e1) {}
			}
		});
		history.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				console.directAsk("historique");
			}
		});
		copy.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				clipManage.setClipboardContents(console.getTextField());
				paste.setEnabled(true);
			}
		});
		cut.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				clipManage.setClipboardContents(console.getTextField());
				console.setTextField("");
				paste.setEnabled(true);
			}
		});
		paste.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				console.setTextField(clipManage.getClipboardContents());
			}
		});
		annonce.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				console.setTextField("annonce \"Annonce à transmettre aux clients\"");
			}
		});
		save.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				console.directAsk("save mysql");
			}
		});
		savelogs.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				console.directAsk("save logs");
			}
		});
		seelogs.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Logs l = new Logs();
				l.setVisible(true);
			}
		});
		about.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				About a = new About();
				a.setVisible(true);
			}
		});
	}
	
	public void updateGraph(int connected, long l, int games) {
		monitor.updateGraph(connected, l, games);
	}
	
	public void setTimes(long l, String s) {
		monitor.setTimes(l, s);
	}
	
	public void writeAnnonce(String s) {
		console.writeAnnonce(s);
	}
	
	public void closeServer() throws InterruptedException {
		ShutDownEmergency cdTime = new ShutDownEmergency(10,console,serverInfos);
		cdTime.start();
	}
	
	public void windowClosing(WindowEvent e) {
		//closeServer();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}