package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

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
	private boolean allowDraw = true;
	private ColorBoard colorBoard;
	
	public DrawingArea(){
		setBackground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);

		this.setLayout(new BorderLayout());
		
		colorBoard = new ColorBoard();
		this.add(colorBoard, BorderLayout.SOUTH);
		
		bufPic = new Picture();
		bufPic.addObserver(this);
		bufPic.addLine(new Line());;
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);

		for (Line l : bufPic.getLines() )
        {
           for(int i=0; i < (l.getData().size()-2); i++){
        	   DrawingData p1 = l.getData().get(i);
        	   DrawingData p2 = l.getData().get(i+1);

        	   // TODO : régler l'épaisseur du trait
        	   g.setColor(p1.color);
        	   g.drawLine(p1.x, p1.y, p2.x, p2.y);
           }
        }

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(allowDraw){
			bufPic.addLine(new Line());
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(allowDraw){
			// TODO : Calculer dynamiquement l'épaisseur du trait en fonction de la taille de la zone en pixels
			bufPic.addPoint(arg0.getX(), arg0.getY(), 3, colorBoard.getSelectedColor());
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
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
		// DO NOTHING

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// DO NOTHING

	}

}
