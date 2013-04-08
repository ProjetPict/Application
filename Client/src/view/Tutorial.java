package view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * JFrame customisé qui contient une série d'images qui sont affichées lors du premier lancement
 * et qui servent de didacticiel
 * @author Jerome
 *
 */
public class Tutorial extends JFrame {

	private static final long serialVersionUID = 1L;
	private ArrayList<JPanel> stepPan;
	private ArrayList<JButton> stepNextToPan;
	private ArrayList<JButton> stepPrevToPan;
	private ArrayList<Image> stepImage;
	private ArrayList<JLabel> stepLbl;

	private int i;
	private int currentStep = 0;

	/**
	 * Constructeur de Tutorial
	 */
	public Tutorial() {

		this.setSize(600, 600);
		this.setTitle(Main.texts.getString("welcome"));
		this.setLocation(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width/2-300, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-300);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/icon.png")));
		this.setResizable(false);

		stepPan = new ArrayList<JPanel>();
		stepNextToPan = new ArrayList<JButton>();
		stepPrevToPan = new ArrayList<JButton>();
		stepImage = new ArrayList<Image>();
		stepLbl = new ArrayList<JLabel>();

		for(int j=1; j<7; j++)
			stepImage.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/rules/slide_"+j+"_fr.png")));

		int size = stepImage.size();

		for(i=0; i<size; i++) {
			stepPan.add(new JPanel(new BorderLayout()));
			JPanel tmpPan = new JPanel(new BorderLayout());
			stepLbl.add(new JLabel(Main.texts.getString("step")+" : "+(i+1)+"/"+size,SwingConstants.CENTER));
			tmpPan.add(stepLbl.get(i),BorderLayout.CENTER);
			stepPrevToPan.add(new JButton("< "+Main.texts.getString("previous")));
			stepNextToPan.add(new JButton(Main.texts.getString("next")+" >"));

			if(i!=0) {
				stepPrevToPan.get(i).addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						currentStep--;
						changeStep(currentStep);
					}
				});
			} else {
				stepPrevToPan.get(i).setEnabled(false);
			}

			if(i!=size-1) {
				stepNextToPan.get(i).addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						currentStep++;
						changeStep(currentStep);
					}
				});
			} else {
				stepNextToPan.get(i).setEnabled(false);
			}

			tmpPan.add(stepPrevToPan.get(i),BorderLayout.WEST);
			tmpPan.add(stepNextToPan.get(i),BorderLayout.EAST);
			ImageIcon tmpImg = new ImageIcon(stepImage.get(i));
			JLabel tmpImgLbl = new JLabel();
			tmpImgLbl.setIcon(tmpImg);
			stepPan.get(i).add(tmpImgLbl,BorderLayout.CENTER);
			stepPan.get(i).add(tmpPan,BorderLayout.SOUTH);
		}

		setContentPane(stepPan.get(0));
	}

	public void changeStep(int i) {
		setContentPane(stepPan.get(i));
		repaint();
		validate();
	}
}
