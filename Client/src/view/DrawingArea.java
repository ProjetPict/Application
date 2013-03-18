package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.GameObserver;
import socketData.*;

/**
 * La zone où le joueur peut dessiner/voir le dessin se former.
 * @author christopher
 *
 */

public class DrawingArea extends JPanel implements MouseListener, MouseMotionListener, Observer {

	private static final long serialVersionUID = 1L;

	//Picture hérite de Observable
	private Picture bufPic;
	private boolean drawing = false;
	private ColorBoard colorBoard;
	private GameObserver go;
	
	private Graphics2D buffer;
	   // image m�moire correspondante au buffer
	private Image image; 
	
	public DrawingArea(GameObserver go){
		setBackground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);

		this.setLayout(new BorderLayout());
		
		colorBoard = new ColorBoard();
		this.add(colorBoard, BorderLayout.SOUTH);
		bufPic = new Picture();
		bufPic.addObserver(this);
		
		Main.getModel().addObserver(this);
		this.go = go;
		
		go.setPicture(bufPic);
		
		
		//bufPic.addLine(new Line());;
	}
	
	public void clearScreen()
	{
		image = createImage((int)(Main.gameWidth),(int)(Main.gameHeight));
        buffer = (Graphics2D) image.getGraphics();
        buffer.setColor( Color.white );
        buffer.fillRect( 0, 0, (int)(Main.gameWidth),(int)(Main.gameHeight));
		bufPic.clear();
		
	}

	
	public long getNbPixels()
	{
		return bufPic.getNbPixels();
	}
	
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		

		 if(buffer==null){
		        image = createImage((int)(Main.gameWidth),(int)(Main.gameHeight));
		        buffer = (Graphics2D) image.getGraphics();
		        buffer.setColor( Color.white );
		        buffer.fillRect( 0, 0, (int)(Main.gameWidth),(int)(Main.gameHeight));
		      }
		for (Line l : bufPic.getLines() )
        {
		   buffer.setColor(l.color);
           for(int i=0; i < (l.getData().size()-2); i++){
        	   Point p1 = l.getData().get(i);
        	   Point p2 = l.getData().get(i+1);

        	   // TODO : régler l'épaisseur du trait
        	   
        	   /*buffer.setStroke(new BasicStroke(p1.size,                     // Line width
                       BasicStroke.CAP_ROUND,    // End-cap style
                       BasicStroke.JOIN_ROUND));*/
        	   buffer.drawLine(p1.x, p1.y, p2.x, p2.y);
           }
        }
		
		 g.drawImage(image, 0, 0, this);

	}
	

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(drawing){
			// TODO : Calculer dynamiquement l'épaisseur du trait en fonction de la taille de la zone en pixels
			Point p = new Point(arg0.getX(), arg0.getY());
			bufPic.addPoint(p);
			go.sendDrawingData(p);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof Boolean)
			drawing = (Boolean) arg1;
		
		repaint();
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Changer la forme du curseur.
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Remettre le curseur en mode "normal"
		
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// DO NOTHING
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		if(drawing){
			Line line = new Line(colorBoard.getSelectedColor(), 3);
			bufPic.addLine(line);
			go.sendNewLine(line);
		}

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// DO NOTHING

	}

}
