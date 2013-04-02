package view;

import java.awt.Point; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import server.Server;

/**
 * Gere l'interface, la fenetre et la communication entre cette interface et le serveur
 * @author Matthieu
 *
 */
public class Fenetre extends JFrame implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Server serverInfos;
	private Console console;
	private Monitor monitor = new Monitor();
	private ClipboardManager clipManage = new ClipboardManager();
	private boolean isDown;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fichier = new JMenu("Fichier");
	private JMenu edition = new JMenu("Edition");
	private JMenu maintenance = new JMenu("Maintenance");
	private JMenu aide = new JMenu("Aide");
	 
	private JMenuItem start = new JMenuItem("Lancer le serveur");
	private JMenuItem stop = new JMenuItem("Arr�ter le serveur");
	private JMenuItem close = new JMenuItem("Fermer");

	private JMenuItem history = new JMenuItem("Historique des commandes");
	private JMenuItem copy = new JMenuItem("Copier le contenu de la ligne de commande");
	private JMenuItem paste = new JMenuItem("Coller dans la ligne de commande");
	private JMenuItem cut = new JMenuItem("Couper le contenu de la ligne de commande");

	private JMenuItem annonce = new JMenuItem("Cr�er une annonce");
	private JMenuItem save = new JMenuItem("Sauvegarder vers MySQL");
	private JMenuItem savelogs = new JMenuItem("Exporter le journal de logs");
	private JMenuItem seelogs = new JMenuItem("Voir le journal de logs");
	
	private JMenuItem about = new JMenuItem("A propos");
	private JMenuItem commands = new JMenuItem("Liste des commandes disponibles");
	private JMenuItem infos = new JMenuItem("Obtenir les informations du serveur");
	
	private JSplitPane pan;
	private JPanel servDown;
	private JLabel servDownLbl;

	public Fenetre(final Server servInfo){
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("DrawVS - Server app");
		this.setLocation(new Point(100,100));
		this.setResizable(false);
		this.setSize(900, 700);
		this.setJMenuBar(menuBar);
		this.serverInfos = servInfo;
		console = new Console(servInfo);
		isDown = false;
		
		menuBar.add(fichier);
		menuBar.add(edition);
		menuBar.add(maintenance);
		menuBar.add(aide);
		
		fichier.add(start);
		fichier.add(stop);
		fichier.addSeparator();
		fichier.add(close);
		start.setEnabled(false);

		edition.add(history);
		edition.addSeparator();
		edition.add(copy);
		edition.add(cut);
		edition.add(paste);
		clipManage = new ClipboardManager();
		/*if(clipManage.getClipboardContents().length()==0)
			paste.setEnabled(false);*/

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
		
		pan = new JSplitPane();
		pan.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.add(pan);
		pan.setDividerLocation(430);
		pan.setTopComponent(monitor);
		pan.setBottomComponent(console);
		
		servDown = new JPanel();
		servDownLbl = new JLabel("Le serveur est actuellement �teint. Vous pouvez fermer la fen�tre ou le relancer.");
		servDown.add(servDownLbl);
		servDownLbl.setVisible(false);
		
		start.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				try {
					servInfo.startServer(true,true);
				} catch (Exception e1) {}
			}
		});
		stop.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				try {
					switchPanel(servDown);
					stopOrClose(false);
					stop.setEnabled(false);
					start.setEnabled(true);
					isDown = true;
				} catch (InterruptedException e1) {}
			}
		});
		close.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				try {
					stopOrClose(true);
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
				console.setTextField("annonce \"Annonce a transmettre aux clients\"");
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
	
	public void stopOrClose(boolean b) throws InterruptedException {
		if(!isDown) {
			ShutDownEmergency cdTime = new ShutDownEmergency(10,console,serverInfos,b,this);
			cdTime.start();
		} else {
			System.exit(0);
		}
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
	
	public void switchPanel(JPanel newPan) {
		this.setContentPane(newPan);
		this.validate();
	}
	
	public void downComplete() {
		servDownLbl.setVisible(true);
	}
	
	public void setLockElements(boolean b) {
		fichier.setEnabled(b);
		edition.setEnabled(b);
		maintenance.setEnabled(b);
		aide.setEnabled(b);
		console.setLockElements(b);
	}
}