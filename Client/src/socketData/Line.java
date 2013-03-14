package socketData;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Une ligne est représentée par un ensemble de DrawingData
 * @author christopher
 *
 */
public class Line {
	private ArrayList<DrawingData> line;
	public Color color;
	public int size;
	
	public Line(Color color, int size){
		line  = new ArrayList<DrawingData>();
		this.color = color;
		this.size = size;
	}
	
	public ArrayList<DrawingData> getData(){
		return line;
	}
	
	public void addPoint(int x, int y, int size, Color color){
		line.add(new DrawingData(x,y,size,color));
	}
}
