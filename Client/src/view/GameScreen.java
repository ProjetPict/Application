package view;

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import socketData.WordCommand;

import model.GameObserver;

/**
 * Ecran de jeu
 * @author christopher
 *
 */
public class GameScreen extends JPanel{

	private static final long serialVersionUID = 1L;
	private DrawingArea drawingArea;
	private ChatArea chatArea;
	private GameObserver go;
	private JOptionPane pane;
	private JDialog wordDialog;
	private WordCommand wordCommand;
	private int time;

	public GameScreen(boolean creator){
		go = Main.getGameObserver();
		drawingArea = new DrawingArea(go);
		chatArea = new ChatArea(creator);
		this.setLayout(null);
		drawingArea.setBounds(0, 0, (int)(this.getWidth()*0.7), this.getHeight());
		chatArea.setBounds((int)(this.getWidth()*0.7), 0, (int)(this.getWidth() - this.getWidth()*0.7), this.getHeight());
		this.add(drawingArea);
		this.add(chatArea);
		go.start();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		drawingArea.setBounds(0, 0, (int)(this.getWidth()*0.7), this.getHeight());
		chatArea.setBounds((int)(this.getWidth()*0.7), 0, (int)(this.getWidth() - this.getWidth()*0.7), this.getHeight());
		
		
		g.fillRect(0, 0, (int)(Main.gameWidth), (int)(Main.gameHeight));
		
		
	}
	
	public void startTurn()
	{
		drawingArea.clearScreen();
		chatArea.launchTimer();
	}
	
	public long getNbPixels()
	{
		return drawingArea.getNbPixels();
	}

	public void chooseWord(WordCommand command) {
		
		String[] options = {command.word1, command.word2, command.word3};
		wordCommand = command;
		pane = new JOptionPane( 
	         Main.texts.getString("chooseword"), 
	         JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, 
	         null, options, null);
		
		time = 30;
		Timer timer = new Timer();
		
		
		wordDialog = pane.createDialog(getParent(), "30 " + Main.texts.getString("remainings"));
		wordDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		wordDialog.setModal(false);
		wordDialog.pack();
		wordDialog.setVisible(true);
		
		wordDialog.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent e) {
				wordCommand.command = (String) pane.getValue();
				
				if(wordCommand.command.equals("uninitializedValue"))
				{
					wordCommand.command = "";
				}
				
				Main.getModel().sendCommand(wordCommand);
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		timer.schedule(new TimerTask() {
			public void run() {
				time--;
				
				String key;
				if(time > 1)
					key = "remainings";
				else
					key = "remaining";
				wordDialog.setTitle(String.valueOf(time) + " " + Main.texts.getString(key));
				if(time <= 0)
					this.cancel();
			}
		}, 0, 1000);
		
	}

	public void closeDialog() {
		wordDialog.dispose();
	}
}
