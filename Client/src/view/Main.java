package view;
import model.Model;

public class Main {
//Test
// TEST 2 COMMIT
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
		/*if(argc.length != 2){
			System.out.print("use : main <host> <username>\n");
			return;
		}
		
		host = argc[0];
		user = argc[1];
		System.out.print("host = "+host+" user = "+user+"\n");*/
		model = new Model(host);
		
		view = new View();
		view.display();
	}
	
	
}
