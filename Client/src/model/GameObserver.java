package model;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import socketData.ChatCommand;
import socketData.Command;
import socketData.Line;
import socketData.Picture;
import socketData.PlayerScore;
import socketData.WordCommand;
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
		/*Command cmd = new Command("getscores");
		
		try {
			out.writeObject(cmd);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		
		while(running){
			try {
				Object obj=in.readObject();
				
				if(obj instanceof Point){
					pict.addPoint((Point)obj);
				}
				else if(obj instanceof Line){
					pict.addLine((Line) obj);
				}
				else if(obj instanceof PlayerScore){
					
					Main.getModel().addPlayerScore((PlayerScore) obj);
				}
				else if(obj instanceof WordCommand){
					Main.getModel().processWordCommand((WordCommand) obj);
				}
				else if(obj instanceof ChatCommand){
					Main.getModel().processChatMsg((ChatCommand) obj);
				}
				else if(obj instanceof Command){
					Main.getModel().processCommand((Command) obj);
				}
				else
					obj = null;
				
			} catch (IOException e) {
				running = false;
			} catch (ClassNotFoundException e) {
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
