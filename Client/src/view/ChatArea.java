package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import socketData.PlayerScore;

public class ChatArea extends JPanel implements ActionListener, Observer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private PlayerScore[] scores;
	
	public ChatArea() {
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
		
		Main.getModel().getScores().values().toArray(scores);
	}
	
}
