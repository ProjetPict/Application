import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Connection extends Thread {
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private Server server;
	
	public Connection(Server server, Socket socket)
	{
		this.server = server;
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run()
	{
		
		try {

			System.out.println("Quelqu'un s'est connecté");
			
            out.println("Entrez votre login : ");
            out.flush();
            String login = in.readLine();
			server.createPlayer(login, socket);
			out.println("success");
            System.out.println(login +" vient de se connecter ");
            out.flush();

		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
