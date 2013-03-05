import java.io.ObjectOutputStream;


public class SendDraw implements Runnable{

	private ObjectOutputStream out;
	
	public SendDraw(ObjectOutputStream out){
		this.out = out;
	}
	
	public void run(){
		while(true){
			//à implémenter
		}
	}
	
}
