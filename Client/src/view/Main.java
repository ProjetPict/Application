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
	public static GameObserver go;
	
	public static void main(String[] argc)
	{
		/*Locale locale = new Locale("fr");
		Locale locale2 = new Locale("en");*/
		
		texts = ResourceBundle.getBundle("TextBundle", Locale.getDefault());
		model = new Model(host);
		view = new View();
		view.setPanel("Login");
		go = new GameObserver(model.getInput(),model.getOutput());
		go.start();
		view.go=go;
	}
	
	
	public static Model getModel(){
		return model;
	}
	
	public static View getView(){
		return view;
	}
}
