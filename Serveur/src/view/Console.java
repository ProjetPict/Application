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

/**
 * Génère et gère une console
 * @author Matthieu
 *
 */
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
	
	/**
	 * Envoi d'une commande a l'interprete
	 */
	public void sendCmd() {
		interprete = serverCommand.getText();
		serverCommand.setText("");
		serverAnswer.setText(serverAnswer.getText().concat("\nCommande : "+interprete));
		cmd.askCmd(interprete);
		historique.add(interprete);
	}
	
	/**
	 * Permet d'interroger directement le serveur avec une commande sans passer par la zone de saisie
	 * Utile si le script a besoin d'effectuer une commande sans intervention utilisateur
	 * @param s Commande a envoyer
	 */
	public void directAsk(String s) {
		cmd.askCmd(s);
	}
	
	/**
	 * Renvoi l'historique des commandes rentrees
	 * @return
	 */
	public ArrayList<String> getHistory() {
		return historique;
	}
	
	/**
	 * Remet a 0 l'historique
	 */
	public void resetHistory() {
		historique = new ArrayList<String>();
	}
	
	/**
	 * Rentre une valeur dans la zone de saisie utilisateur de la console
	 * @param s Valeur a mettre dans le JTextfield
	 */
	public void setTextField(String s) {
		serverCommand.setText(s);
	}
	
	/**
	 * Retourne la valeur dans la zone de saisie utilisateur de la console
	 * @return
	 */
	public String getTextField() {
		return serverCommand.getText();
	}
	
	/**
	 * Ecrit les reponses fournies par le serveur
	 * @param s Reponse a ecrire
	 */
	public void writeAnswer(String s) {
		serverAnswer.setText(serverAnswer.getText().concat("\nRéponse : "+s));
	}
	
	/**
	 * Permet d'ecrire sans la mise en forme "Reponse"/"Commande"
	 * @param s Annonce a ecrire dans le terminal
	 */
	public void writeAnnonce(String s) {
		serverAnswer.setText(serverAnswer.getText().concat(s));
	}
}
