import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Connection extends Thread {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Connection(Socket socket)
	{
		this.socket = socket;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ObjectInputStream getInput()
	{
		return in;
	}

	public ObjectOutputStream getOutput(){
		return out;
	}

	public void run()
	{

		try {

			System.out.println("Quelqu'un s'est connecté");

			out.writeObject("Entrez votre login : ");
			out.flush();
			Object login;
			try {
				login = in.readObject();
				if(login instanceof String)
					Server.createPlayer((String)login, socket, this);
				else
					System.out.println("FUCK");
				out.writeObject("success");
				System.out.println(login +" vient de se connecter ");
				out.flush();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}