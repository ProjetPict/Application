package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		btnConnec.setBounds((int)(490*Main.ratioX),(int)(430*Main.ratioY),210,90);
		btnConnec.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnConnec.setOpaque(false);
		btnConnec.setContentAreaFilled(false);
		btnConnec.setBorderPainted(false);
		btnConnec.setBorder(null);
		login.setOpaque(false);
		login.setBorder(null);
		login.setForeground(Color.white);
		login.setFont(fontBasic);
		login.setBounds((int)(390*Main.ratioX), (int)(320*Main.ratioY), 280, 25);
		password.setOpaque(false);
		password.setBorder(null);
		password.setForeground(Color.white);
		password.setFont(fontBasic);
		password.setBounds((int)(390*Main.ratioX), (int)(390*Main.ratioY), 280, 25);
		newAccount.setForeground(Color.white);
		newAccount.setFont(fontBasicLow);
		newAccount.setBounds((int)(348*Main.ratioX), (int)(530*Main.ratioY), 300, 30);
		passwordForget.setForeground(Color.white);
		passwordForget.setFont(fontBasicLow);
		passwordForget.setBounds((int)(348*Main.ratioX), (int)(560*Main.ratioY), 300, 30);
		
		btnConnec.addActionListener(this);
		login.addActionListener(this);
		password.addActionListener(this);
	}
	
	public void paintComponent(Graphics g) {
		btnConnec.setLocation((int)(490*Main.ratioX  + 188.*(Main.ratioX-1.0)),(int)(430*Main.ratioY));
		password.setLocation((int)(390*Main.ratioX), (int)(390*Main.ratioY));
		login.setLocation((int)(390*Main.ratioX), (int)(320*Main.ratioY));
		newAccount.setLocation((int)(348*Main.ratioX), (int)(530*Main.ratioY));
		passwordForget.setLocation((int)(348*Main.ratioX), (int)(560*Main.ratioY));
		
		
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(imgBackground, 0, 0, (int)Main.gameWidth, (int)Main.gameHeight, this);
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
