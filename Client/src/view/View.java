package view;

public class View {
	
	
	private static Window window;
	private Login login;
	
	public View(){
		window = new Window();
		login = new Login();
	}
	
	public void display(){
		window.setVisible(true);
		window.setPanel(login);
	}
	
	
}
