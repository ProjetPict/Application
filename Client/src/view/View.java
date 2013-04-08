package view;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import socketData.Picture;
import socketData.WordCommand;
import model.GameObserver;

/**
 * Transmet les différents écrans à la fenêtre principale.
 *
 */
public class View {


	private static Window window;
	private Login login;
	private Browser browser;
	private GameScreen gmScreen;
	private GameObserver go;
	private Bonus bonus = new Bonus();

	public View(){
		window = new Window();
		window.addKeyListener(bonus);
	}

	public void setPanel(String panelType, boolean gameCreator) {
		window.setVisible(true);
		
		if(panelType.equals("Browser")) {
			browser = new Browser(window);
			window.setPanel(browser);
			if(Main.settingsProp.getProperty("firstLaunch").equals("0")) {
				Tutorial t = new Tutorial();
				t.setVisible(true);
				Main.settingsProp.setProperty("firstLaunch", "1");
				try {
					Main.settingsProp.store(new FileOutputStream("files/settings.conf"), null);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if (panelType.equals("Login")) {
			login = new Login();
			login.addKeyListener(bonus);
			window.setPanel(login);
		}
		else if (panelType.equals("GameScreen")) {
			gmScreen = new GameScreen(gameCreator);
			window.setPanel(gmScreen);
		}
		
		
		window.validate();
		
	}
	
	public GameObserver getGameObserver() {
		return go;
	}
	
	public void setGameObserver(GameObserver go) {
		this.go = go;
	}

	public void startTurn() {
		gmScreen.startTurn();
		
	}

	public void chooseWord(WordCommand command) {
		gmScreen.chooseWord(command);
	}
	
	public Window getWindow() {
		return window;
	}

	public void closeDialog() {
		gmScreen.closeDialog();
	}

	public void setPicture(Picture pict) {
		gmScreen.setPicture(pict);
	}

	public void endTurn() {
		gmScreen.endTurn();
	}

	public void quitGame() {
		Main.getModel().quitGame();
		setPanel("Browser", false);
		gmScreen = null;
	}
}
