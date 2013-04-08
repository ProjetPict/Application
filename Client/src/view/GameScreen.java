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

import model.GameObserver;
import socketData.Picture;
import socketData.WordCommand;

/**
 * Ecran de jeu, contient les différents panels nécéssaires à l'affichage complet de la zone
 * de jeu
 *
 */
public class GameScreen extends JPanel {

	private static final long serialVersionUID = 1L;
	private DrawingArea drawingArea;
	private ChatArea chatArea;
	private GameObserver gameObs;
	private JOptionPane pane;
	private JDialog wordDialog;
	private WordCommand wordCommand;
	private int time;

	/**
	 * Constructeur de GameScreen
	 * @param creator True si le joueur est le créateur de la partie, false sinon
	 */
	public GameScreen(boolean creator) {
		gameObs = Main.getGameObserver();
		drawingArea = new DrawingArea(gameObs);
		chatArea = new ChatArea(creator);
		this.setLayout(null);
		drawingArea.setBounds(0, 0, (int)(this.getWidth()-400), this.getHeight());
		chatArea.setBounds((int)(this.getWidth()-400), 0, 400, this.getHeight());
		this.add(drawingArea);
		this.add(chatArea);
		gameObs.start();
		Main.getModel().sendCommand("getscores");
	}

	@Override
	protected void paintComponent(Graphics g) {
		drawingArea.setBounds(0, 0, (int)(this.getWidth()-400), this.getHeight());
		chatArea.setBounds((int)(this.getWidth()-400), 0, 400, this.getHeight());
		drawingArea.validate();
		g.fillRect(0, 0, (int)(Main.gameWidth), (int)(Main.gameHeight));
	}

	/**
	 * On appelle différentes fonctions de nos panels pour préparer le client
	 * pour un nouveau tour
	 */
	public void startTurn() {
		drawingArea.clearScreen();
		chatArea.launchTimer();
		chatArea.enableStartButton(false);
		chatArea.enableAnswer(true);
	}

	/**
	 * 
	 * @return Le nombre de pixels dans le dessin
	 */
	public long getNbPixels() {
		return drawingArea.getNbPixels();
	}

	/**
	 * On ouvre une OptionPane pour choisir le mot à dessiner
	 * @param command La liste des mots disponibles
	 */
	public void chooseWord(WordCommand command) {

		String[] options = {command.word1, command.word2, command.word3};
		wordCommand = command;

		pane = new JOptionPane( Main.texts.getString("chooseword"), 
				JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, 
				null, options, null);

		time = 15;
		Timer timer = new Timer();

		wordDialog = pane.createDialog(getParent(), "30 " + Main.texts.getString("remainings"));
		wordDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		wordDialog.setModal(false);
		wordDialog.pack();
		wordDialog.setVisible(true);
		wordDialog.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {
				wordCommand.command = (String) pane.getValue();

				if(wordCommand.command.equals("uninitializedValue")) {
					wordCommand.command = "";
				}

				Main.getModel().sendCommand(wordCommand);
			}

			@Override
			public void componentMoved(ComponentEvent e) {}

			@Override
			public void componentResized(ComponentEvent e) {}

			@Override
			public void componentShown(ComponentEvent e) {}

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

	/**
	 * On ferme l'optionPane
	 */
	public void closeDialog() {
		wordDialog.dispose();
	}

	public void setPicture(Picture pict) {
		drawingArea.setPicture(pict);
	}

	/**
	 * On termine le tour côté client
	 */
	public void endTurn() {
		drawingArea.clearScreen();
		chatArea.enableAnswer(false);
		chatArea.cancelTimer();
	}
}
