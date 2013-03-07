package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Model{
	private ConnecToServer Connec;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public Model(String host){
		Connec = new ConnecToServer(host);
		out = Connec.getOutput();
		in = Connec.getInput();

	}
	
	public String connect(String login, String password){
		return Connec.connect(login, password);
	}
	
	public void disconnect(){
		Connec.disconnect();
	}
	
	public ObjectOutputStream getOutput(){
		return out;
	}
	
	public ObjectInputStream getInput(){
		return in;
	}
	
}
