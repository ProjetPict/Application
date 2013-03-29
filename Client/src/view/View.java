package view;

import socketData.Picture;
import socketData.WordCommand;
import model.GameObserver;

/**
 * Transmet les différents écrans à la fenêtre principale.
 * @author christopher
 *
 */
public class View {


	private static Window window;
	private Login login;
	private Browser browser;
	private GameScreen gmScreen;
	private GameObserver go;

	public View(){
		window = new Window();
	}

	public void setPanel(String panelType, boolean gameCreator){
		window.setVisible(true);
		if(panelType.equals("Browser")){
			browser = new Browser(window);
			window.setPanel(browser);
		}
		else if (panelType.equals("Login")){
			login = new Login();
			window.setPanel(login);
		}
		else if (panelType.equals("GameScreen")){
			gmScreen = new GameScreen(gameCreator);
			window.setPanel(gmScreen);
		}
		
		window.validate();
		
	}
	
	public GameObserver getGameObserver()
	{
		return go;
	}
	
	public void setGameObserver(GameObserver go)
	{
		this.go = go;
	}

	public void startTurn() 
	{
		gmScreen.startTurn();
		
	}

	public void chooseWord(WordCommand command) {
		gmScreen.chooseWord(command);
		
	}
	
	public Window getWindow()
	{
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
}
