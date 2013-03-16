package view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import server.Server;


public class Fenetre extends JFrame {
	private Server serverInfos;
	private Console console = new Console();
	private Monitor monitor = new Monitor();
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fichier = new JMenu("Fichier");
	private JMenu edition = new JMenu("Edition");
	private JMenu maintenance = new JMenu("Maintenance");
	private JMenu aide = new JMenu("Aide");
	 
	private JMenuItem start = new JMenuItem("Lancer le serveur");
	private JMenuItem restart = new JMenuItem("Relancer le serveur");
	private JMenuItem stop = new JMenuItem("Arr�ter le serveur");
	private JMenuItem close = new JMenuItem("Fermer");

	private JMenuItem history = new JMenuItem("Historique des commandes");
	private JMenuItem copy = new JMenuItem("Copier");
	private JMenuItem paste = new JMenuItem("Coller");
	private JMenuItem cut = new JMenuItem("Couper");

	private JMenuItem annonce = new JMenuItem("Cr�er une annonce");
	private JMenuItem save = new JMenuItem("Sauvegarder vers MySQL");
	private JMenuItem savelogs = new JMenuItem("Exporter le journal de logs");
	private JMenuItem seelogs = new JMenuItem("Voir le journal de logs");
	
	private JMenuItem about = new JMenuItem("A propos");
	private JMenuItem commands = new JMenuItem("Liste des commandes disponibles");
	private JMenuItem infos = new JMenuItem("Obtenir les informations du serveur");

	public Fenetre(Server servInfo){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("DrawVS - Server app");
		this.setLocation(new Point(100,100));
		this.setResizable(false);
		this.setSize(900, 700);
		this.setJMenuBar(menuBar);
		this.serverInfos = servInfo;
		
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
		
		about.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				About a = new About();
				a.setVisible(true);
			}
		});
		seelogs.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Logs l = new Logs();
				l.setVisible(true);
			}
		});
	}
	
	public void updateGraph(int connected, long l, int games) {
		monitor.updateGraph(connected, l, games);
	}
}