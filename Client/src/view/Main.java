package view;
import java.util.Locale;
import java.util.ResourceBundle;

import model.GameObserver;
import model.Model;

/**
 * Classe de démarrage : instancie le modèle et la vue
 * @author christopher
 *
 */
public class Main {

	private static Model model;
	private static View view;
	private static String host = "localhost";
	public static ResourceBundle texts;
	
	
	public static void main(String[] argc)
	{
		/*Locale locale = new Locale("fr");
		Locale locale2 = new Locale("en");*/
		
		texts = ResourceBundle.getBundle("TextBundle", Locale.getDefault());
		model = new Model(host);
		view = new View();
		view.setPanel("Login");
	}
	
	
	public static Model getModel(){
		return model;
	}
	
	public static View getView(){
		return view;
	}
	
	public static GameObserver getGameObserver()
	{
		return model.getGameObserver();
	}
}
