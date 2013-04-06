import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import server.Server;
import view.ShutDownEmergency;

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
				System.out.println("Voulez-vous le lancer en mode console ou fenetre ? [console/fenetre/quitter]");
				param = input.readLine();
			} catch (IOException e) {}
		} while(!param.equals("console") && !param.equals("fenetre") && !param.equals("quitter"));
		if(param.equals("Quitter"))
			System.exit(0);
		else if(param.equals("Fenetre"))
			state = true;
		else
			state = false;
		try {
			server = new Server(state);
			server.start();
			if(!state) {
				System.out.println("> Rentrez la commande \"Quitter\" afin d'éteindre le serveur.");
				do {
					try {
						param = input.readLine();
					} catch (IOException e) {}
				} while(param.equals("Quitter"));
				ShutDownEmergency cdTime = new ShutDownEmergency(10,server);
				cdTime.start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage()+".\nArret du serveur.");
		}
		
	}
}
