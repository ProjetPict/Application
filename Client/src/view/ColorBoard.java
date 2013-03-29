package view;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * Palette de couleurs
 * @author christopher cacciatore
 *
 */
public class ColorBoard extends JPanel{

	private static final long serialVersionUID = 1L;

	private JRadioButton btnBlack;
	private JRadioButton btnBlue;
	private JRadioButton btnGreen;
	private JRadioButton btnYellow;
	private JRadioButton btnSmall;
	private JRadioButton btnMedium;
	private JRadioButton btnBig;
	private JRadioButton btnRed;
	
	private ButtonGroup btnGroup;
	private ButtonGroup btnGroupSizes;
	private JSeparator separator;
	private JComboBox sizeBox;
	
	public ColorBoard(){
		btnBlack = new JRadioButton(Main.texts.getString("black"));
		btnBlue = new JRadioButton(Main.texts.getString("blue"));
		btnGreen = new JRadioButton(Main.texts.getString("green"));
		btnYellow = new JRadioButton(Main.texts.getString("yellow"));
		btnRed = new JRadioButton(Main.texts.getString("red"));
		btnSmall = new JRadioButton(Main.texts.getString("small"));
		btnMedium = new JRadioButton(Main.texts.getString("medium"));
		btnBig = new JRadioButton(Main.texts.getString("big"));
		
		btnGroup = new ButtonGroup();
		btnGroup.add(btnBlack);
		btnGroup.add(btnBlue);
		btnGroup.add(btnGreen);
		btnGroup.add(btnYellow);
		btnGroup.add(btnRed);
		
		btnGroupSizes = new ButtonGroup();
		btnGroupSizes.add(btnSmall);
		btnGroupSizes.add(btnMedium);
		btnGroupSizes.add(btnBig);
		
		this.setLayout(new GridLayout(1,7));
		this.add(btnBlack);
		this.add(btnBlue);
		this.add(btnGreen);
		this.add(btnYellow);
		this.add(btnRed);
		
		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
	
		add(separator);
		
		JLabel textSizes = new JLabel(Main.texts.getString("size"));
		String[] strSizes = new String[] {Main.texts.getString("small"), 
				Main.texts.getString("medium"), Main.texts.getString("big")};
		sizeBox = new JComboBox(strSizes);
		
		this.add(textSizes);
		this.add(sizeBox);
		
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
	
	public int getSelectedSize(){
		int res = 3;
		int index = sizeBox.getSelectedIndex();
	
		switch(index)
		{
		default:
		case 0:
			res = 3;
			break;
		case 1:
			res = 6;
			break;
		case 2:
			res = 10;
			break;
		}
		
		return res;
	}
}
