package model;

public class Model implements Runnable{
	ConnecToServer Connec;
	
	public Model(String host, String login){
		Connec = new ConnecToServer(host,login);
	}
	
	public void run(){
		Connec.run();
	}
	
	
	
}
