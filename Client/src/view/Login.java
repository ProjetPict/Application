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

public class Login extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3026788736669539483L;

	private JButton btnConnec;
	private JTextField login;
	private JTextField password;
	private JLabel lblLogin;
	private JLabel lblPassword;
	private JPanel pnlCenter;
	
	public Login(){
		btnConnec = new JButton("Connexion");
		lblLogin = new JLabel("Login");
		lblPassword = new JLabel("Mot de passe");
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
		
		this.setLayout(new GridBagLayout());
		this.add(pnlCenter);//, BorderLayout.CENTER);
		this.setMaximumSize(new Dimension(100,100));	
	}
	
	public void actionPerformed(ActionEvent arg0) {
		  if(arg0.getSource() == btnConnec){
			  String res = Main.getModel().connect(login.getText(),password.getText());
			  javax.swing.JOptionPane.showMessageDialog(null,res); 
			  //TODO : Main.getView().setPanel(Browser);
		  } 
	}
}
