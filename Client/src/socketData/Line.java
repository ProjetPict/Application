package socketData;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Une ligne est représentée par un ensemble de DrawingData
 * @author christopher
 *
 */
public class Line {
	private ArrayList<Point> line;
	public Color color;
	public int size;
	
	public Line(Color color, int size){
		line  = new ArrayList<Point>();
		this.color = color;
		this.size = size;
	}
	
	public ArrayList<Point> getData(){
		return line;
	}
	
	public void addPoint(Point p){
		line.add(p);
	}
}
