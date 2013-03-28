package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import socketData.*;

/**
 * Ecran de selection/creation de partie
 * @author Christopher, Matthieu
 *
 */
public class BrowserVDeux extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;

	private Window mainWindow;
	
    private Image imgBlank = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/blank.png"));
    private Image imgLogo = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/logo_small.png"));
    private Image imgTop = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ressources/images/top.png"));

    private JDialog newGameOptions;
    private JPanel createPane;
    private boolean toFade = false;
    private JPanel fadeEffect;
    
	private JButton btnJoin;
	private JButton btnPnlCreate;
	private JButton btnCreate;
	private JButton btnCreateClose;
	private JButton btnRefresh;

	private JLabel lblGameName;
	private JTextField txtGameName;
	private JLabel lblGamePassword;
	private JTextField txtGamePassword;
	private JLabel lblGamePmax;
	private JTextField txtGamePmax;
	private JLabel lblTurn;
	private JTextField txtTurn;
	private JLabel lblDifficulty;
	private JComboBox jcbDifficulty;
	
	private JLabel lblFilter;
	private JLabel lblShowEmpty;
	private JComboBox jcbShowEmpty;
	private JLabel lblShowFull;
	private JComboBox jcbShowFull;
	private JLabel lblSearch;
	private JTextField txtSearch;
	private JButton btnApply;

	private JScrollPane scrlPane;
	private JTable table;
	private JPanel crtPane;

	private GameList gl;


	public BrowserVDeux(Window win) {
		mainWindow = win;
		
		btnJoin = new JButton(Main.texts.getString("connection"));
		btnPnlCreate = new JButton(Main.texts.getString("g_create"));
		btnCreate = new JButton(Main.texts.getString("send"));
		btnCreateClose = new JButton(Main.texts.getString("cancel"));
		btnRefresh = new JButton(Main.texts.getString("refresh"));
		lblGameName = new JLabel(Main.texts.getString("name"));
		txtGameName = new JTextField();
		lblGamePassword = new JLabel(Main.texts.getString("password"));
		txtGamePassword = new JTextField();
		lblGamePmax = new JLabel(Main.texts.getString("max_p"));
		txtGamePmax = new JTextField();
		lblTurn = new JLabel(Main.texts.getString("turns"));
		txtTurn = new JTextField();
		lblDifficulty = new JLabel(Main.texts.getString("difficulty_choice"));
		String[] difficulties = { Main.texts.getString("difficulty_easy"), Main.texts.getString("difficulty_medium"), Main.texts.getString("difficulty_hard") };
		String[] yesNo = { Main.texts.getString("yes"), Main.texts.getString("no") };
		jcbDifficulty = new JComboBox(difficulties);
		
		lblFilter = new JLabel(Main.texts.getString("filter"));
		lblFilter.setFont(new Font("Arial", Font.PLAIN, 26));
		lblShowEmpty = new JLabel(Main.texts.getString("show_empty"));
		jcbShowEmpty = new JComboBox(yesNo);
		lblShowFull = new JLabel(Main.texts.getString("show_empty"));
		jcbShowFull = new JComboBox(yesNo);
		lblSearch = new JLabel(Main.texts.getString("search"));
		txtSearch = new JTextField();
		btnApply = new JButton(Main.texts.getString("apply"));

		crtPane = new JPanel();
		crtPane.setVisible(false);

		table = new JTable();
		table.setShowGrid(false);
		table.setShowVerticalLines(true);
		table.setDefaultRenderer(Boolean.class, new BooleanCellRenderer());
		fillTable();
		scrlPane = new JScrollPane(table);
		
		fadeEffect = new JPanel() {  
			public void paintComponent(Graphics g) {  
				g.setColor(new Color(0, 0, 0, 0.7f));  
				g.fillRect(0, 0, (int)Main.gameWidth, (int)Main.gameHeight);  
			}  
		};
		fadeEffect.setOpaque(false);
		mainWindow.setGlassPane(fadeEffect);
		
		this.add(btnPnlCreate);
		this.add(btnJoin);
		this.add(btnRefresh);
		this.add(scrlPane);
		this.add(lblFilter);
		this.add(lblShowEmpty);
		this.add(jcbShowEmpty);
		this.add(lblShowFull);
		this.add(jcbShowFull);
		this.add(lblSearch);
		this.add(txtSearch);
		this.add(btnApply);

		createPane = new JPanel(new SpringLayout());
		createPane.add(lblGameName);
		createPane.add(txtGameName);
		createPane.add(lblGamePassword);
		createPane.add(txtGamePassword);
		createPane.add(lblGamePmax);
		createPane.add(txtGamePmax);
		createPane.add(lblTurn);
		createPane.add(txtTurn);
		createPane.add(lblDifficulty);
		createPane.add(jcbDifficulty);
		createPane.add(btnCreate);
		createPane.add(btnCreateClose);
		SpringUtilities.makeGrid(createPane, 6, 2, 5, 5, 5, 5);
		newGameOptions = new JDialog();
		newGameOptions.setSize(400, 200);
		newGameOptions.addWindowFocusListener(new WindowFocusListener() {
		    public void windowGainedFocus(WindowEvent e) {
		    }
		 
		    public void windowLostFocus(WindowEvent e) {
		    	newGameOptions.setVisible(false);
		    	toFade = false;
		    	callEventRepaint();
		    }
		});
		newGameOptions.setUndecorated(true);
		newGameOptions.add(createPane);
		crtPane.setVisible(true);

		btnJoin.addActionListener(this);
		btnPnlCreate.addActionListener(this);
		btnCreate.addActionListener(this);
		btnCreateClose.addActionListener(this);
		btnRefresh.addActionListener(this);
	}
	
	public void paintComponent(Graphics g) {
		scrlPane.setBounds(50, 150, (int)Main.gameWidth-375, (int)Main.gameHeight-230);
		btnJoin.setBounds((int)Main.gameWidth-310, 150, 250, 30);
		btnPnlCreate.setBounds((int)Main.gameWidth-310, 190, 250, 30);
		btnRefresh.setBounds((int)Main.gameWidth-310, 230, 250, 30);
		lblFilter.setLocation((int)Main.gameWidth-310, 290);
		lblShowEmpty.setLocation((int)Main.gameWidth-310, 325);
		jcbShowEmpty.setLocation((int)Main.gameWidth-310, 345);
		lblShowFull.setLocation((int)Main.gameWidth-310, 380);
		jcbShowFull.setLocation((int)Main.gameWidth-310, 400);
		lblSearch.setLocation((int)Main.gameWidth-310, 435);
		txtSearch.setBounds((int)Main.gameWidth-310, 465, 250, 30);
		btnApply.setBounds((int)Main.gameWidth-310, 500, 250, 30);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(imgBlank, 0, 0, (int)Main.gameWidth, (int)Main.gameHeight, this);
        g2d.drawImage(imgTop, 0, 0, (int)Main.gameWidth, 120, this);
        g2d.drawImage(imgLogo, 15, 8, 259, 87, this);
        if(toFade) {
        	fadeEffect.setVisible(true);
        	//g2d.setColor(new Color(1, 0, 0, 0.5f));
        	//g2d.drawImage(imgBlack, 0, 0, (int)Main.gameWidth, (int)Main.gameHeight, this);
        } else {
        	fadeEffect.setVisible(false);
        }
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btnJoin) {
			joinGame();
		}
		else if(arg0.getSource() == btnPnlCreate) {
			newGameOptions.setLocationRelativeTo(this);
			newGameOptions.setVisible(true);
			toFade = true;
			callEventRepaint();
		} 
		else if(arg0.getSource() == btnCreate) {
			createGame();
		}
		else if(arg0.getSource() == btnRefresh) {
			fillTable();
		}
		else if(arg0.getSource() == btnCreateClose) {
			newGameOptions.setVisible(false);
			toFade = false;
			callEventRepaint();
		}
	}

	private void joinGame() {
		String name = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
		String password = null;
		if(((Boolean)table.getModel().getValueAt(table.getSelectedRow(), 2)) == true)
		{
			password = JOptionPane.showInputDialog(this,
					Main.texts.getString("enter_pass"),
					Main.texts.getString("password"),
					JOptionPane.QUESTION_MESSAGE);
		}
		String result = Main.getModel().joinGame(name, password);
		if(result.equals("wrongpassword"))
			JOptionPane.showMessageDialog(this,Main.texts.getString("wrong_pass"), Main.texts.getString("error"), JOptionPane.ERROR_MESSAGE);
		else if(result.equals("gamefull"))
			JOptionPane.showMessageDialog(this,Main.texts.getString("game_full"), Main.texts.getString("error"), JOptionPane.ERROR_MESSAGE);
		else
			Main.getView().setPanel("GameScreen", false);
	}

	private void createGame() {
		String name = txtGameName.getText();
		String password = txtGamePassword.getText();
		String res = "";
		int pmax;
		int turns;

		try {
			pmax = Integer.valueOf(txtGamePmax.getText());
		} catch(Exception e) {
			pmax = 0;
		}

		try {
			turns = Integer.valueOf(txtTurn.getText());
		} catch(Exception e) {
			turns = 0;
		}

		if(name.length() >= 4) {
			if(password.equals(""))
				password = null;
			//TODO ajouter choix difficulté
			res = Main.getModel().createGame(name, password, pmax, turns, 1);
			JOptionPane.showMessageDialog(this,res);
			if(res.equals("success"))
				Main.getView().setPanel("GameScreen", true);
		}
		else {
			JOptionPane.showMessageDialog(this,Main.texts.getString("name_short"));
		}

		//TODO : rejoindre la partie cree a l'instant
	}

	private void fillTable() {
		gl = Main.getModel().getGameList();
		table.setModel(new GameTableModel(gl));
	}
	
	private void callEventRepaint() {
		this.repaint();
	}

}
