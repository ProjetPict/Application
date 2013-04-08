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
 * Gère l'interface, la fenetre et la communication entre cette interface et le serveur
 *
 */
public class Window extends JFrame implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Console console;
	private Server serverInfos;
	private Monitor monitor = new Monitor();
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fichier = new JMenu("Fichier");

	private JMenuItem close = new JMenuItem("Fermer");
	
	private JSplitPane pan;
	
	public final int SCREEN_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	public final int SCREEN_HEIGHT = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

	public Window(final Server server){
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("DrawVS - Server app");
		this.setLocation(new Point(SCREEN_WIDTH/2-450,SCREEN_HEIGHT/2-350));
		this.setResizable(false);
		this.setSize(900, 700);
		this.setJMenuBar(menuBar);
		serverInfos = server;
		console = new Console();
		
		menuBar.add(fichier);
		fichier.add(close);
		
		pan = new JSplitPane();
		pan.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.add(pan);
		pan.setDividerLocation(430);
		pan.setTopComponent(monitor);
		pan.setBottomComponent(console);
		
		close.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				try {
					stopOrClose(true);
				} catch (InterruptedException e1) {}
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
		ShutDownEmergency cdTime = new ShutDownEmergency(10, console, this, serverInfos);
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
	
	public void switchPanel(JPanel newPan) {
		this.setContentPane(newPan);
		this.validate();
	}
	
	public void setLockElements(boolean b) {
		fichier.setEnabled(b);
	}
}