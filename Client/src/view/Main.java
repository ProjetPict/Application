package view;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
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
	private static String host;
	public static String player;
	public static ResourceBundle texts;
	public static final int SCREEN_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREEN_HEIGHT = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	public static double gameWidth;
	public static double gameHeight;
	public static double ratioX;
	public static double ratioY;
	public static double offsetX;
	public static double offsetY;
	
	
	public static void main(String[] argc)
	{
		//Locale locale = new Locale("fr");
		//Locale locale2 = new Locale("en");
		//texts = ResourceBundle.getBundle("TextBundle", locale2);
		
		gameWidth = (int)(Main.SCREEN_WIDTH * 0.8);
		gameHeight = (int)(Main.SCREEN_HEIGHT * 0.8);
		ratioX = gameWidth/1024.0;
		ratioY = gameHeight/768.0;
		offsetX = (gameWidth - 1024)/2;
		
		texts = ResourceBundle.getBundle("TextBundle", Locale.getDefault());
		
		//Get the host from the property file
		Properties hostProp = new Properties();
		FileReader fr;

		try {
			fr = new FileReader("host.conf");
			hostProp.load(fr);
			
			host = hostProp.getProperty("host");
			
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		model = new Model(host);
		view = new View();
		view.setPanel("Login", false);
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
