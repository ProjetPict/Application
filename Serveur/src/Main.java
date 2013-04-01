import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import server.Server;

public class Main {
	
	private static Server server;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Initialisation preliminaire du serveur.");
		String param = null;
		boolean state = false;
		do {
			try {
				System.out.println("Voulez-vous le lancer en mode console ou fenetre ? [Console/Fenetre/Quitter]");
				param = input.readLine();
			} catch (IOException e) {}
		} while(!param.equals("Console") && !param.equals("Fenetre") && !param.equals("Quitter"));
		if(param.equals("Quitter"))
			System.exit(0);
		else if(param.equals("Fenetre"))
			state = true;
		else
			state = false;
		try {
			server = new Server(state);
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
