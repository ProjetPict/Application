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
public class Window extends JFrame implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Server serverInfos;
	private Console console;
	private Monitor monitor = new Monitor();
	private boolean isDown;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fichier = new JMenu("Fichier");
	private JMenu maintenance = new JMenu("Maintenance");
	private JMenu aide = new JMenu("Aide");
	 
	private JMenuItem start = new JMenuItem("Lancer le serveur");
	private JMenuItem stop = new JMenuItem("Arrêter le serveur");
	private JMenuItem close = new JMenuItem("Fermer");

	private JMenuItem save = new JMenuItem("Sauvegarder vers MySQL");
	private JMenuItem savelogs = new JMenuItem("Exporter le journal de logs");
	private JMenuItem seelogs = new JMenuItem("Voir le journal de logs");
	
	private JMenuItem about = new JMenuItem("A propos");
	
	private JSplitPane pan;
	private JPanel servDown;
	private JLabel servDownLbl;
	
	public final int SCREEN_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	public final int SCREEN_HEIGHT = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

	public Window(final Server servInfo){
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("DrawVS - Server app");
		this.setLocation(new Point(SCREEN_WIDTH/2-450,SCREEN_HEIGHT/2-350));
		this.setResizable(false);
		this.setSize(900, 700);
		this.setJMenuBar(menuBar);
		this.serverInfos = servInfo;
		console = new Console();
		isDown = false;
		
		menuBar.add(fichier);
		menuBar.add(maintenance);
		menuBar.add(aide);
		
		fichier.add(start);
		fichier.add(stop);
		fichier.addSeparator();
		fichier.add(close);
		start.setEnabled(false);

		maintenance.add(save);
		maintenance.add(savelogs);
		maintenance.addSeparator();
		maintenance.add(seelogs);
		
		aide.add(about);
		
		pan = new JSplitPane();
		pan.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.add(pan);
		pan.setDividerLocation(430);
		pan.setTopComponent(monitor);
		pan.setBottomComponent(console);
		
		servDown = new JPanel();
		servDownLbl = new JLabel("Le serveur est actuellement éteint. Vous pouvez fermer la fenêtre ou le relancer.");
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
		console.getConsole().selectAll();
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
		maintenance.setEnabled(b);
		aide.setEnabled(b);
	}
}