package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Console extends JPanel {

	private JTextArea serverAnswer;
	private JTextField serverCommand;
	private JButton sendCommand;
	private String interprete;
	private ArrayList<String> historique;
	
	public Console() {
		serverAnswer = new JTextArea(10,50);
		serverCommand = new JTextField();
		sendCommand = new JButton("Ex�cuter");
		serverAnswer.setEditable(false);
		serverAnswer.setLineWrap(true);
		serverAnswer.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(serverAnswer);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		historique = new ArrayList<String>();
		
		JPanel send = new JPanel(new BorderLayout());
		send.add(serverCommand,BorderLayout.CENTER);
		send.add(sendCommand,BorderLayout.EAST);
		this.setLayout(new BorderLayout());
		this.add(scroll,BorderLayout.CENTER);
		this.add(send,BorderLayout.SOUTH);
		this.setPreferredSize(new Dimension(890, 160));
		
		sendCommand.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				interprete = serverCommand.getText();
				historique.add(interprete);
				serverCommand.setText("");
				if(historique.size()==1)
					serverAnswer.setText(serverAnswer.getText().concat("Commande : "+interprete));
				else
					serverAnswer.setText(serverAnswer.getText().concat("\nCommande : "+interprete));
			} 
		});
	}
}