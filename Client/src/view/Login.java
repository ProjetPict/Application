package view;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Ecran de Login
 * @author christopher
 *
 */
public class Login extends JPanel implements ActionListener{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton btnConnec;
	private JTextField login;
	private JTextField password;
	private JLabel lblLogin;
	private JLabel lblPassword;
	private JPanel pnlCenter;
	
	public Login(){
		btnConnec = new JButton(Main.texts.getString("connection"));
		lblLogin = new JLabel(Main.texts.getString("login"));
		lblPassword = new JLabel(Main.texts.getString("password"));
		pnlCenter = new JPanel();
		login = new JTextField("login");
		password = new JTextField("password");
		
		pnlCenter.setLayout(new GridLayout(3,2));
		pnlCenter.setMaximumSize(new Dimension(150,150));
		pnlCenter.add(lblLogin);
		pnlCenter.add(login);
		pnlCenter.add(lblPassword);
		pnlCenter.add(password);
		pnlCenter.add(btnConnec);
		
		btnConnec.addActionListener(this);
		login.addActionListener(this);
		password.addActionListener(this);
		
		this.setLayout(new GridBagLayout());
		this.add(pnlCenter);//, BorderLayout.CENTER);
		this.setMaximumSize(new Dimension(100,100));	
	}
	
	public void actionPerformed(ActionEvent arg0) {
		  if(arg0.getSource() == btnConnec || arg0.getSource() == password || arg0.getSource() == login){
			  boolean res = Main.getModel().connect(login.getText(),password.getText());
			 
			  if(res){
				  javax.swing.JOptionPane.showMessageDialog(this,Main.texts.getString("co_succes")); //on affiche le résultat dans un pop-up
				  Main.getView().setPanel("Browser"); //si la connexion réussit, on passe à la selection d'une partie
				  
			  }
			  else
				  javax.swing.JOptionPane.showMessageDialog(this,Main.texts.getString("co_fail"));
		  } 
	}
}
