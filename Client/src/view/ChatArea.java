package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import socketData.PlayerScore;
import javax.swing.JTextArea;
import java.awt.Color;

public class ChatArea extends JPanel implements ActionListener, Observer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private PlayerScore[] scores;
	private JTextField textField_1;
	
	public ChatArea() {
		setBackground(Color.WHITE);
		scores = new PlayerScore[]{};
		setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(132, 646, 186, 20);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblAnswer = new JLabel(Main.texts.getString("answer"));
		lblAnswer.setBounds(76, 649, 46, 14);
		add(lblAnswer);
		
		Main.getModel().addObserver(this);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(120, 11, 209, 22);
	
		add(textArea);
		
		textField_1 = new JTextField();
		textField_1.setBounds(182, 62, 86, 20);
		add(textField_1);
		textField_1.setColumns(10);
	}

	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == textField)
		{
			if(!textField.getText().equals(""))
			{
				Main.getModel().sendAnswer(textField.getText());
				textField.setText("");
			}
		}
		
	}



	@Override
	public void update(Observable o, Object arg) {
		
		
		scores = Main.getModel().getScores().values().toArray(new PlayerScore[Main.getModel().getScores().values().size()+1]);
		if(scores.length > 0)
		{
			textField_1.setText(scores[0].login + " : " + scores[0].score);
		}
	}
}
