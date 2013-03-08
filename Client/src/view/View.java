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
		window.setVisible(true);
	}
}
