package view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Connection extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton connection;
	private JLabel login;
	private JLabel password;
	private JTextField login_t;
	private JTextField password_t;
	
	public Connection()
	{
		connection = new JButton("Connexion");
		
	}

}
