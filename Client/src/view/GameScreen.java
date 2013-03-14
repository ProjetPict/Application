package view;

import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import socketData.DrawingData;
import socketData.Line;

import model.GameObserver;

/**
 * Ecran de jeu
 * @author christopher
 *
 */
public class GameScreen extends JPanel{

	private static final long serialVersionUID = 1L;
	private DrawingArea drawingarea;
	private JList histoChat;
	private JScrollPane scrlChat;

	public GameScreen(){
		drawingarea = new DrawingArea();
		histoChat = new JList();
		scrlChat = new JScrollPane(histoChat);
		this.setLayout(new BorderLayout());
		this.add(drawingarea, BorderLayout.CENTER);
		this.add(scrlChat, BorderLayout.EAST);

	}

}
