package model;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import socketData.Command;
import socketData.Line;
import socketData.Picture;
import socketData.PlayerScore;
import view.Main;


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
		boolean running = true;
		Command cmd = new Command("getscores");
		
		try {
			out.writeObject(cmd);
			out.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(running){
			try {
				Object obj=in.readObject();
				if(obj instanceof Point)
				{
					pict.addPoint((Point)obj);
				}
				else if(obj instanceof Line)
				{
					
					pict.addLine((Line) obj);
				}
				else if(obj instanceof PlayerScore)
				{
					
					Main.getModel().addPlayerScore((PlayerScore) obj);
				}
				
			} catch (IOException e) {
				running = false;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				running = false;
			}

		}


	}

	public Boolean sendDrawingData(Point p){
		try {
			out.writeObject(p);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
		return true;		
	}

	public Boolean sendNewLine(Line line){
		try {
			out.writeObject(line);
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
	
	public void setPicture(Picture pict)
	{
		this.pict = pict;
	}

}
