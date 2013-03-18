package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import socketData.PlayerScore;
import java.awt.Color;
import javax.swing.JList;

public class ChatArea extends JPanel implements ActionListener, Observer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private PlayerScore[] scores;
	private JList list;
	
	public ChatArea() {
		setBackground(Color.WHITE);
		scores = new PlayerScore[]{};
		setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(132, (int)(646*Main.ratioY), 186, 20);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblAnswer = new JLabel(Main.texts.getString("answer"));
		lblAnswer.setBounds(76, 649, 46, 14);
		add(lblAnswer);
		
		Main.getModel().addObserver(this);
		
		list = new JList();
		list.setBounds(83, 84, 284, 80);
		add(list);
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
			list.setListData(scores);
		}
	}
}
