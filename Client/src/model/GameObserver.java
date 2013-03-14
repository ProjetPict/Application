package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import socketData.Command;
import socketData.DrawingData;
import socketData.Line;
import socketData.Picture;


/**
 * 
 * @author Nicolas
 *
 */

public class GameObserver extends Thread{
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Picture pict;

	public GameObserver( ObjectInputStream in, ObjectOutputStream out){
		this.in=in;
		this.out=out;
	}
	
	
	public void run() {
		while(true){
			try {
				Object obj=in.readObject();
				if(obj instanceof Command)
				{
					//if( ((Command)obj).command.equals("newline"))
						//pict.addLine(new Line());
				}
				else if(obj instanceof DrawingData)
				{
					pict.addPoint( ((DrawingData) obj).x, ((DrawingData) obj).y,((DrawingData)obj).size,((DrawingData)obj).color);
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

	public Boolean sendDrawingData(DrawingData d){
		try {
			out.writeObject(d);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
		return true;		
	}

	public Boolean sendNewLine(){
		Command cmd = new Command("newline");

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
	
	public Picture getPicture()
	{
		return pict;
	}

}
