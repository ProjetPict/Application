package model;
import java.io.Serializable;

/**
 * Cette classe est un type de donn�es utilis�e pour le transfert d'informations sur le dessin
 * en cours.
 * @author Jerome
 *
 */
public class DrawingData implements Serializable{

	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public int size;
	public String color;

	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @param color
	 */
	public DrawingData(int x, int y, int size, String color)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = color;
	}
	
	public String toString(){
		String res = new String("Position x : " + String.valueOf(this.x)+"\n");
		res.concat("Position y : " + String.valueOf(this.y) + "\n");
		res.concat("Size : " + String.valueOf(this.size) + "\n");
		res.concat("Color : " + this.color);
		return res;
	}
}