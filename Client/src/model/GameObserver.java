package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import socketData.DrawingData;
import socketData.Line;
import socketData.Picture;
import view.GameScreen;


/**
 * 
 * @author Nicolas
 *
 */

public class GameObserver extends Thread{
	private ObjectInputStream in;
	private ObjectOutputStream out;
	public Picture p;

	public GameObserver( ObjectInputStream in, ObjectOutputStream out){
		this.in=in;
		this.out=out;

	}
	
	
	

	public void run() {
		while(true){
			try {
				Object obj=in.readObject();
				if(obj instanceof String)
				{
					if( obj.equals("newline"))
						p.addLine(new Line());
				}
				if(obj instanceof DrawingData)
				{
					p.addPoint( ((DrawingData) obj).x, ((DrawingData) obj).y,((DrawingData)obj).size,((DrawingData)obj).color);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	}

	public Boolean envoiDrawingData(DrawingData p1){
		try {
			out.writeObject(p1);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
		return true;		
	}

	public Boolean envoiNewLine(){
		String cmd="newline";

		try {
			out.writeObject(cmd);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

	}

}
