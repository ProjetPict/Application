package view;

import java.awt.BorderLayout; 
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import server.Server;

public class Console extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextArea serverAnswer;
	private JTextField serverCommand;
	private JButton sendCommand;
	private String interprete;
	private ArrayList<String> historique;
	private Commander cmd;
	private Server serv;
	
	public Console(Server infos) {
		cmd = new Commander(this,infos);
		serv = infos;
		
		serverAnswer = new JTextArea(10,50);
		serverCommand = new JTextField();
		sendCommand = new JButton("Exécuter");
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
		if(historique.size()==1)
			serverAnswer.setText(serverAnswer.getText().concat("Commande : "+interprete));
		else
			serverAnswer.setText(serverAnswer.getText().concat("\nCommande : "+interprete));
		cmd.askCmd(interprete);
	}
	
	public void writeAnswer(String s) {
		serverAnswer.setText(serverAnswer.getText().concat("\nRéponse : "+s));
		
	}
}
