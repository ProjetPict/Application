package view;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Palette de couleurs
 * @author christopher
 *
 */
public class ColorBoard extends JPanel{

	private static final long serialVersionUID = 1L;

	private JRadioButton btnBlack;
	private JRadioButton btnBlue;
	private JRadioButton btnGreen;
	private JRadioButton btnYellow;
	private JRadioButton btnRed;
	
	private ButtonGroup btnGroup;
	
	public ColorBoard(){
		btnBlack = new JRadioButton("Noir");
		btnBlue = new JRadioButton("Bleu");
		btnGreen = new JRadioButton("Vert");
		btnYellow = new JRadioButton("Jaune");
		btnRed = new JRadioButton("Rouge");
		
		btnGroup = new ButtonGroup();
		btnGroup.add(btnBlack);
		btnGroup.add(btnBlue);
		btnGroup.add(btnGreen);
		btnGroup.add(btnYellow);
		btnGroup.add(btnRed);
		
		this.setLayout(new GridLayout(1,5));
		this.add(btnBlack);
		this.add(btnBlue);
		this.add(btnGreen);
		this.add(btnYellow);
		this.add(btnRed);
		
		btnBlack.setSelected(true);
	}
	
	public Color getSelectedColor(){
		Color res = Color.black;
		
		if(btnBlue.isSelected())
			res = Color.blue;
		else if(btnGreen.isSelected())
			res = Color.green;
		else if(btnYellow.isSelected())
			res = Color.yellow;
		else if(btnRed.isSelected())
			res = Color.red;
		
		return res;
	}
}
