package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Ecran de Login
 * @author Christopher, Matthieu
 *
 */
public class Login extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private JButton btnConnec;
	private JTextField login;
	private JPasswordField password;
	private JLabel newAccount;
	private JLabel passwordForget;
    private Image imgBackground = new ImageIcon(getClass().getResource("../ressources/images/login.jpg")).getImage();
    private Font fontBasic = new Font("Arial", Font.PLAIN, 24);
    private Font fontBasicLow = new Font("Arial", Font.PLAIN, 16);
	
	public Login(){
		this.setLayout(null);
		btnConnec = new JButton(new ImageIcon(getClass().getResource("../ressources/images/btn_login.png")));
		login = new JTextField(Main.texts.getString("login"));
		password = new JPasswordField("**********");
		newAccount = new JLabel(Main.texts.getString("new_account"));
		passwordForget = new JLabel(Main.texts.getString("password_forget"));
		
		this.add(login);
		this.add(password);
		this.add(btnConnec);
		this.add(newAccount);
		this.add(passwordForget);
		
		btnConnec.setBounds(490,430,210,90);
		btnConnec.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnConnec.setOpaque(false);
		btnConnec.setContentAreaFilled(false);
		btnConnec.setBorderPainted(false);
		btnConnec.setBorder(null);
		login.setOpaque(false);
		login.setBorder(null);
		login.setForeground(Color.white);
		login.setFont(fontBasic);
		login.setBounds(390, 320, 280, 25);
		password.setOpaque(false);
		password.setBorder(null);
		password.setForeground(Color.white);
		password.setFont(fontBasic);
		password.setBounds(390, 390, 280, 25);
		newAccount.setForeground(Color.white);
		newAccount.setFont(fontBasicLow);
		newAccount.setBounds(348, 530, 300, 30);
		passwordForget.setForeground(Color.white);
		passwordForget.setFont(fontBasicLow);
		passwordForget.setBounds(348, 560, 300, 30);
		
		btnConnec.addActionListener(this);
		login.addActionListener(this);
		password.addActionListener(this);
	}
	
	public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(imgBackground, 0, 0, this);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		  if(arg0.getSource() == btnConnec || arg0.getSource() == password || arg0.getSource() == login){
			  boolean res = Main.getModel().connect(login.getText(),password.getText());
			  if(res){
				  javax.swing.JOptionPane.showMessageDialog(this,Main.texts.getString("co_succes"));
				  Main.getView().setPanel("Browser");
			  }
			  else
				  javax.swing.JOptionPane.showMessageDialog(this,Main.texts.getString("co_fail"));
		  } 
	}
}
