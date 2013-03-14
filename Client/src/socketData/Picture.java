package socketData;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Une image est un ensemble de lignes (cf Class Line)
 * @author christopher
 *
 */
public class Picture extends Observable{
	private ArrayList<Line> pict;
	
	public Picture(){
		pict  = new ArrayList<Line>();
	}
	
	public ArrayList<Line> getLines(){
		return pict;
	}
	
	public void addLine(Line l){
		pict.add(l);
		setChanged();
		notifyObservers();
	}
	
	public void addPoint(int x, int y, int size, Color color){
		int index = this.getLines().size() - 1;
		this.getLines().get(index).addPoint(x , y, size, color);
		setChanged();
		notifyObservers();
	}
	
}
