import java.io.IOException;
import java.io.ObjectInputStream;


public class ReceiveDraw implements Runnable{
	
	private ObjectInputStream in;
	private DrawingData drwData;
	
	public ReceiveDraw(ObjectInputStream in){
		this.in = in;
	}
	
	public void run(){
		while(true){
			try{
				try{
					Object obj = in.readObject();
					if(obj instanceof DrawingData)
						drwData = new DrawingData(((DrawingData) obj).x, ((DrawingData) obj).y, ((DrawingData) obj).size, ((DrawingData) obj).color);
					else
						throw new ClassNotFoundException();
				}catch(IOException e){
					e.printStackTrace();
				}
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}

			System.out.print(drwData.toString());
			
		}
	}

}
