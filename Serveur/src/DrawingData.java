import java.io.Serializable;

/**
 * Cette classe est un type de données utilisée pour le transfert d'informations sur le dessin
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

	public DrawingData(int x, int y, int size, String color)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = color;
	}
}