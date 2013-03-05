
public class Model implements Runnable{
	ConnecDraw CDraw;
	
	public Model(String host, String login){
		CDraw = new ConnecDraw(host,login);
	}
	
	public void run(){
		CDraw.run();
	}
	
	
	
}
