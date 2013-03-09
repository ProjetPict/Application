package view;

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
	
	public View(){
		window = new Window();
	}
	
	public void setPanel(String panelType){
		if(panelType.equals("Browser")){
			browser = new Browser();
			window.setPanel(browser);
		}
		else if (panelType.equals("Login")){
			login = new Login();
			window.setPanel(login);
		}
		else if (panelType.equals("GameScreen")){
			gmScreen = new GameScreen();
			window.setPanel(gmScreen);
		}
		window.setVisible(true);
	}
}
