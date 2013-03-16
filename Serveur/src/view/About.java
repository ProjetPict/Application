package view;

import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class About extends JFrame {
	
	public About(){
		this.setTitle("DrawVS - A propos");
		this.setLocation(new Point(100,100));
		this.setResizable(false);
		this.setSize(400, 200);	
		this.add(new JPanel());
	}
}
