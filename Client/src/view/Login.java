package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Ecran de Login
 *
 */
public class Login extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton btnConnec;
	private JTextField login;
	public JPasswordField password;
	private Image imgLogo = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/logo.png"));
	private Image imgBackground = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/login.jpg"));
	private Image imgFieldUser = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/field_login_username.png"));
	private Image imgFieldPass = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/field_login_password.png"));
	private Font fontBasic = new Font("Arial", Font.PLAIN, 24);
	
	
	/**
	 * Constructeur de Login
	 */
	public Login() {

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				requestFocus();
			}
		});

		this.setLayout(null);

		String location;
		if(Locale.getDefault().getLanguage().equals("fr"))
			location = "/ressources/images/btn_login_fr.png";
		else
			location = "/ressources/images/btn_login_en.png";
		btnConnec = new JButton(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(getClass().getResource(location))));
		String strLogin = Main.settingsProp.getProperty("username");

		if(!strLogin.equals(""))
			login = new JTextField(strLogin);
		else
			login = new JTextField(Main.texts.getString("login"));
		
		login.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(login.getText().equals(Main.texts.getString("login"))) {
					login.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if(login.getText().equals("")) {
					login.setText(Main.texts.getString("login"));
				}
			}
		});

		if(!strLogin.equals(""))
			password = new JPasswordField("");
		else
			password = new JPasswordField("**********");


		this.add(login);
		this.add(password);
		this.add(btnConnec);

		int calcWidthUser = (int)(Main.gameWidth-330)/2;
		btnConnec.setBounds(calcWidthUser+140,(int)(430*Main.ratioY),210,90);
		btnConnec.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnConnec.setOpaque(false);
		btnConnec.setContentAreaFilled(false);
		btnConnec.setBorderPainted(false);
		btnConnec.setBorder(null);
		login.setOpaque(false);
		login.setBorder(null);
		login.setForeground(Color.white);
		login.setFont(fontBasic);
		login.setBounds(calcWidthUser+35, (int)(320*Main.ratioY), 280, 25);
		password.setOpaque(false);
		password.setBorder(null);
		password.setForeground(Color.white);
		password.setFont(fontBasic);
		password.setBounds(calcWidthUser+35, (int)(390*Main.ratioY), 280, 25);

		btnConnec.addActionListener(this);
		login.addActionListener(this);
		password.addActionListener(this);
	}

	public void paintComponent(Graphics g) {
		int calcWidthUser = (int)(Main.gameWidth-330)/2;
		btnConnec.setLocation(calcWidthUser+140,(int)(430*Main.ratioY));
		password.setLocation(calcWidthUser+35, (int)(390*Main.ratioY));
		login.setLocation(calcWidthUser+35, (int)(320*Main.ratioY));

		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(imgBackground, 0, 0, (int)Main.gameWidth, (int)Main.gameHeight, this);
		g2d.drawImage(imgLogo, (int)(Main.gameWidth-346)/2, (int)(50*Main.ratioX), 346, 116, this);
		g2d.drawImage(imgFieldUser, calcWidthUser, (int)(320*Main.ratioY), 330, 30, this);
		g2d.drawImage(imgFieldPass, calcWidthUser, (int)(390*Main.ratioY), 330, 30, this);
	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btnConnec || arg0.getSource() == password || arg0.getSource() == login) {
			boolean res = Main.getModel().connect(login.getText(), new String(password.getPassword()));

			if(res) {
				Main.settingsProp.setProperty("username", login.getText());
				try {
					Main.settingsProp.store(new FileOutputStream("files/settings.conf"), null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Main.player = login.getText();
				Main.getView().setPanel("Browser", false);
			} else
				javax.swing.JOptionPane.showMessageDialog(this,Main.texts.getString("co_fail"));
		} 
	}
}
