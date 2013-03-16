package view;

import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Logs extends JFrame {
	public Logs(){
		this.setTitle("DrawVS - Journal de logs");
		this.setLocation(new Point(100,100));
		this.setResizable(false);
		this.setSize(600, 900);	
		this.add(new JPanel());
	}
}
