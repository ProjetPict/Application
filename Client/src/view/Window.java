package view;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window extends JFrame implements WindowListener, ComponentListener{

	private static final long serialVersionUID = 5314555994004764198L;

	public Window() {
		//On initialise la JFrame		
		addWindowListener(this);
		addComponentListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("DrawVS");
		this.setMinimumSize(new Dimension(640,480));
		this.setResizable(true);
		this.setSize((int)Main.gameWidth, (int)Main.gameHeight);
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
	    this.setLocation(x, y);
	    this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/icon.png")));
	}
	
	public void setPanel(JPanel panel){
		this.setContentPane(panel);
	}

	private void onClose(){
		Main.getModel().disconnect();
		this.dispose();
	}

	public void windowClosing(WindowEvent e) {
		onClose();
	}


	public void windowClosed(WindowEvent e) {

	}

	public void windowOpened(WindowEvent e) {
		
	}

	public void windowIconified(WindowEvent e) {
		
	}

	public void windowDeiconified(WindowEvent e) {
		
	}

	public void windowActivated(WindowEvent e) {
		
	}

	public void windowDeactivated(WindowEvent e) {
		
	}

	public void windowGainedFocus(WindowEvent e) {
		
	}

	public void windowLostFocus(WindowEvent e) {
		
	}

	public void windowStateChanged(WindowEvent e) {
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Main.gameWidth = this.getWidth();
		Main.gameHeight = this.getHeight();
		Main.ratioX = this.getWidth()/1024.0;
		Main.ratioY = this.getHeight()/768.0;
		Main.offsetX = Main.gameWidth - 1024;
		this.getContentPane().repaint();
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
	}
}