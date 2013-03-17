package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.GameObserver;

/**
 * Ecran de jeu
 * @author christopher
 *
 */
public class GameScreen extends JPanel{

	private static final long serialVersionUID = 1L;
	private DrawingArea drawingarea;
	private ChatArea chatArea;
	private JScrollPane scrlChat;
	private GameObserver go;

	public GameScreen(){
		go = Main.getGameObserver();
		drawingarea = new DrawingArea(go);
		chatArea = new ChatArea();
		
		this.setLayout(null);
		drawingarea.setBounds(0, 0, (int)(this.getWidth()*0.7), this.getHeight());
		chatArea.setBounds((int)(this.getWidth()*0.7), 0, (int)(this.getWidth() - this.getWidth()*0.7), this.getHeight());
		this.add(drawingarea);
		this.add(chatArea);
		go.start();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		drawingarea.setBounds(0, 0, (int)(this.getWidth()*0.7), this.getHeight());
		chatArea.setBounds((int)(this.getWidth()*0.7), 0, (int)(this.getWidth() - this.getWidth()*0.7), this.getHeight());
		
		
		g.fillRect(0, 0, (int)(Main.gameWidth), (int)(Main.gameHeight));
		
		
	}

}
