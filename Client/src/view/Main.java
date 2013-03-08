package view;
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
	
	public static Model getModel(){
		return model;
	}
	
	public static View getView(){
		return view;
	}
	
	public static void main(String[] argc)
	{
		model = new Model(host);
		
		view = new View();
		view.setPanel("Login");
	}
	
	
}
