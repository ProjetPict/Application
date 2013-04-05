package view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Tutorial extends JFrame {

	private ArrayList<JPanel> stepPan;
	private ArrayList<JButton> stepNextToPan;
	private ArrayList<JButton> stepPrevToPan;
	private ArrayList<Image> stepImage;
	private ArrayList<JLabel> stepLbl;
	
	private int i;
	private int currentStep = 0;
	
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
		
		stepImage.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/logo.png")));
		stepImage.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/logo.png")));
		stepImage.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/logo.png")));
		stepImage.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/logo.png")));
		int size = stepImage.size();
		
		for(i=0; i<size; i++) {
			stepPan.add(new JPanel(new BorderLayout()) {
				public void paintComponents(Graphics g) {
					g.drawImage(getImage(),0,0,this);
				}
			});
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
			stepPan.get(i).add(tmpPan,BorderLayout.SOUTH);
		}
		setContentPane(stepPan.get(0));
		
	}

	public void changeStep(int i) {
		setContentPane(stepPan.get(i));
		repaint();
		validate();
	}
	
	public Image getImage() {
		return stepImage.get(i);
	}
	
}
