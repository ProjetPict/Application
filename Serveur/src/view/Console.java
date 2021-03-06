package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * G�n�re et g�re une console
 *
 */
public class Console extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextPane serverAnswer;
	
	/**
	 * Constructeur de Console
	 */
	public Console() {
		serverAnswer = new JTextPane();

		serverAnswer.setEditable(false);
		JScrollPane scroll = new JScrollPane(serverAnswer);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel send = new JPanel(new BorderLayout());
		this.setLayout(new BorderLayout());
		this.add(scroll,BorderLayout.CENTER);
		this.add(send,BorderLayout.SOUTH);
		this.setPreferredSize(new Dimension(890, 160));
	}
	
	
	/**
	 * Ecrit les reponses fournies par le serveur
	 * @param s Reponse a ecrire
	 */
	public void writeAnswer(String s) {
		serverAnswer.setText(serverAnswer.getText().concat("\nR�ponse : "+s));
	}
	
	
	/**
	 * Permet d'ecrire sans la mise en forme "Reponse"/"Commande"
	 * @param s Annonce a ecrire dans le terminal
	 */
	public void writeAnnonce(String s) {
		serverAnswer.setText(serverAnswer.getText().concat(s));
	}
	
	
	public String getConsoleContent() {
		return serverAnswer.getText();
	}
	
	public void rewriteConsoleContent(String s) {
		serverAnswer.setText(s);
	}
	
	public JTextPane getConsole() {
		return serverAnswer;
	}
}
