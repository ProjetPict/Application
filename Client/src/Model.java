import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class Model {
	ConnecDraw CDraw;
	
	public Model(String host, String login){
		CDraw = new ConnecDraw(host,login);
	}
	
	public void start(){
		CDraw.start();
	}
	
	
	
}
