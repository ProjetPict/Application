package view;

import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import server.Server;

public class Console extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextPane serverAnswer;
	private JTextField serverCommand;
	private JButton sendCommand;
	private String interprete;
	private ArrayList<String> historique;
	private Commander cmd;
	private Server serv;
	
	public Console(Server infos) {
		cmd = new Commander(this,infos);
		serv = infos;
		
		serverAnswer = new JTextPane();
		serverCommand = new JTextField();
		sendCommand = new JButton("Exécuter");

		serverAnswer.setEditable(false);
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
				sendCmd();
			} 
		});
		serverCommand.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				sendCmd();
			} 
		});
	}
	
	public void sendCmd() {
		interprete = serverCommand.getText();
		historique.add(interprete);
		serverCommand.setText("");
		serverAnswer.setText(serverAnswer.getText().concat("\nCommande : "+interprete));
		cmd.askCmd(interprete);
	}
	
	public ArrayList<String> getHistory() {
		return historique;
	}
	
	public void resetHistory() {
		historique = new ArrayList<String>();
	}
	
	public void writeAnswer(String s) {
		serverAnswer.setText(serverAnswer.getText().concat("\nRéponse : "+s));
	}
	
	public void writeAnnonce(String s) {
		serverAnswer.setText(serverAnswer.getText().concat(s));
	}
}
