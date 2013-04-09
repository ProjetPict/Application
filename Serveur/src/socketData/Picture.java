package socketData;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Une image est un ensemble de lignes (cf Class Line)
 *
 */
public class Picture extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Line> pict;
	private long nbPixels;

	/**
	 * Constructeur de Picture
	 */
	public Picture() {
		pict  = new ArrayList<Line>();
		nbPixels = 0;
	}


	public ArrayList<Line> getLines() {
		return pict;
	}


	/**
	 * On ajoute une ligne au dessin et on nofitie les observeurs du changement
	 * @param l La Line à ajouter
	 */
	public void addLine(Line l) {
		pict.add(l);
		setChanged();
		notifyObservers();
	}


	/**
	 * On ajoute un point au dessin et on notifie les observeur du changement
	 * @param p Le Point à ajouter
	 */
	public void addPoint(Point p) {
		nbPixels++;
		int index = this.getLines().size() - 1;
		this.getLines().get(index).addPoint(p);
		setChanged();
		notifyObservers();
	}


	public long getNbPixels() {
		return nbPixels;
	}

	/**
	 * On remet à zéro le dessin (évite de refaire une instance et de remettre les observeurs)
	 */
	public void clear() {
		pict.clear();
		nbPixels = 0;
		setChanged();
		notifyObservers();
	}
}
