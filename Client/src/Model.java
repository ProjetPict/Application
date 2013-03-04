
public class Model {
	ConnecDraw CDraw;
	
	public Model(String host, String login){
		CDraw = new ConnecDraw(host,login);
	}
	
	public void start(){
		CDraw.start();
	}
	
	
	
}
