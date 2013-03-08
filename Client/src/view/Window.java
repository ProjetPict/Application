package view;


import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window extends JFrame implements WindowListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5314555994004764198L;

	public Window(){
		//On initialise la JFrame
		addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(new Point(100,100));
		this.setResizable(false);
		this.setSize(800, 600);
	}

	public void setPanel(JPanel panel){
		this.setContentPane(panel);
		//this.validate();
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

}