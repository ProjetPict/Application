package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Bonus implements KeyListener {
	
	private JFrame bonusFrame;
	private JPanel bonusPanel;
	private Image imgBonus;
	
	private int[] konamiCode = {38,38,40,40,37,39,37,39,66,65};
	private ArrayList<Integer> enterCode;
	
	public Bonus() {
		enterCode = new ArrayList<Integer>();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(enterCode.size()==0 && e.getKeyCode()==38) {
			enterCode.add(38);
		} else if(e.getKeyCode()==konamiCode[(enterCode.size())]) {
			enterCode.add(e.getKeyCode());
		} else {
			enterCode.clear();
		}
		if(enterCode.size()==10) {
			bonusFrame = new JFrame("Konami Code saisi !");
			bonusFrame.setSize(400,400);
			bonusFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/icon.png")));
			bonusFrame.setLocation(Main.SCREEN_WIDTH/2-200, Main.SCREEN_HEIGHT/2-200);
			bonusFrame.setResizable(false);
			bonusFrame.setVisible(true);
			bonusPanel = (JPanel) bonusFrame.getContentPane();
            ImageIcon ii = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/bonus.gif")));
            JLabel iLbl = new JLabel();
            iLbl.setIcon(ii);
            bonusPanel.add(iLbl);
			bonusFrame.validate();
			enterCode.clear();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

}
