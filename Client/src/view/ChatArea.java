package view;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class ChatArea extends JPanel implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	
	
	public ChatArea() {
		setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(132, 646, 186, 20);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblAnswer = new JLabel(Main.texts.getString("answer"));
		lblAnswer.setBounds(76, 649, 46, 14);
		add(lblAnswer);
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
	
}
