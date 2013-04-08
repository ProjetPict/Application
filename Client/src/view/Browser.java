package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.table.TableCellRenderer;
import socketData.*;

/**
 * Ecran de séléction/création de partie
 *
 */
public class Browser extends JPanel implements ActionListener {

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
	private JCheckBox cbShowFull;
	private JCheckBox cbShowPrivate;
	private JCheckBox cbShowActive;
	private JLabel lblSearch;
	private JTextField txtSearch;
	private JButton btnSearch;

	private JScrollPane scrlPane;
	private JTable table;
	private JPanel crtPane;

	private GameList gl;
	private BooleanCellRenderer boolRenderer;
	private IntCellRenderer intRenderer;

	private boolean displayFull;
	private boolean displayPrivate;
	private boolean displayActive;

	public Browser(Window win) {
		mainWindow = win;

		displayFull = true;
		displayPrivate = true;
		displayActive = true;

		btnJoin = new JButton(Main.texts.getString("join"));
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
		String[] difficulties = { Main.texts.getString("easy"), 
				Main.texts.getString("medium"), Main.texts.getString("hard") };
		jcbDifficulty = new JComboBox(difficulties);

		lblFilter = new JLabel(Main.texts.getString("filter"));
		lblFilter.setFont(new Font("Arial", Font.PLAIN, 26));
		cbShowFull = new JCheckBox(Main.texts.getString("show_full"),true);
		cbShowFull.setBackground(Color.white);
		cbShowPrivate = new JCheckBox(Main.texts.getString("show_private"),true);
		cbShowPrivate.setBackground(Color.white);
		cbShowActive = new JCheckBox(Main.texts.getString("show_active"),true);
		cbShowActive.setBackground(Color.white);
		lblSearch = new JLabel(Main.texts.getString("search"));
		lblSearch.setFont(new Font("Arial", Font.PLAIN, 26));
		txtSearch = new JTextField("");
		btnSearch = new JButton(Main.texts.getString("search"));

		crtPane = new JPanel();
		crtPane.setVisible(false);

		boolRenderer = new BooleanCellRenderer();
		intRenderer = new IntCellRenderer();

		table = new JTable() {

			private static final long serialVersionUID = 1L;

			public TableCellRenderer getCellRenderer(int row, int column) {
				if (column == 3 || column == 4) {
					return boolRenderer;
				} else if(column == 2) {
					return intRenderer;
				}

				return super.getCellRenderer(row, column);
			}
		};

		table.setShowGrid(false);
		table.setShowVerticalLines(true);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					joinGame();
				}
			}
		});

		refreshTable();
		scrlPane = new JScrollPane(table);

		fadeEffect = new JPanel() {
			private static final long serialVersionUID = 1L;
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
		this.add(cbShowFull);
		this.add(cbShowPrivate);
		this.add(cbShowActive);
		this.add(lblSearch);
		this.add(txtSearch);
		this.add(btnSearch);

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
		jcbDifficulty.setBackground(Color.white);

		SpringUtilities.makeGrid(createPane, 6, 2, 5, 5, 5, 5);

		newGameOptions = new JDialog();
		newGameOptions.setSize(400, 200);
		newGameOptions.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {}

			public void windowLostFocus(WindowEvent e) {
				newGameOptions.setVisible(false);
				toFade = false;
				repaint();
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
		btnSearch.addActionListener(this);
		txtSearch.addActionListener(this);
		cbShowFull.addActionListener(this);
		cbShowActive.addActionListener(this);
		cbShowPrivate.addActionListener(this);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		scrlPane.setBounds(50, 150, (int)Main.gameWidth-375, (int)Main.gameHeight-230);
		table.revalidate();
		btnJoin.setBounds((int)Main.gameWidth-310, 150, 250, 30);
		btnPnlCreate.setBounds((int)Main.gameWidth-310, 190, 250, 30);
		btnRefresh.setBounds((int)Main.gameWidth-310, 230, 250, 30);
		lblFilter.setLocation((int)Main.gameWidth-307, 290);
		cbShowFull.setLocation((int)Main.gameWidth-310, 320);
		cbShowPrivate.setLocation((int)Main.gameWidth-310, 340);
		cbShowActive.setLocation((int)Main.gameWidth-310, 360);
		lblSearch.setLocation((int)Main.gameWidth-310, 410);
		txtSearch.setBounds((int)Main.gameWidth-310, 440, 250, 30);
		btnSearch.setBounds((int)Main.gameWidth-310, 475, 250, 30);
		g2d.drawImage(imgBlank, 0, 0, (int)Main.gameWidth, (int)Main.gameHeight, this);
		g2d.drawImage(imgTop, 0, 0, (int)Main.gameWidth, 120, this);
		g2d.drawImage(imgLogo, 15, 8, 259, 87, this);
		if(toFade) {
			fadeEffect.setVisible(true);
		} else {
			fadeEffect.setVisible(false);
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btnJoin) {
			joinGame();
		} else if(arg0.getSource() == btnPnlCreate) {
			newGameOptions.setLocationRelativeTo(this);
			newGameOptions.setVisible(true);
			toFade = true;
			repaint();
		} else if(arg0.getSource() == btnCreate) {
			createGame();
		} else if(arg0.getSource() == btnRefresh) {
			refreshTable();
		} else if(arg0.getSource() == btnCreateClose) {
			newGameOptions.setVisible(false);
			toFade = false;
			repaint();
		} else if(arg0.getSource() == cbShowActive) {
			if(cbShowActive.isSelected())
				displayActive = true;
			else
				displayActive = false;
			applySearch(gl);
		} else if(arg0.getSource() == cbShowFull) {
			if(cbShowFull.isSelected())
				displayFull = true;
			else
				displayFull = false;
			applySearch(gl);
		} else if(arg0.getSource() == cbShowPrivate) {
			if(cbShowPrivate.isSelected())
				displayPrivate = true;
			else
				displayPrivate = false;

			applySearch(gl);
		} else if(arg0.getSource() == btnSearch || arg0.getSource() == txtSearch) {	
			applySearch(gl);
		}
	}

	/**
	 * On applique le filtre de recherche sur la liste puis on envoie le résultat pour appliquer
	 * les autres filtres
	 * @param gl
	 */
	private void applySearch(GameList gl) {
		if(gl != null) {
			ArrayList<GameInfo> games = new ArrayList<GameInfo>();

			for(GameInfo gi : gl.games) {
				String name = gi.name.toLowerCase();
				String search = txtSearch.getText().toLowerCase();
				if(name.contains(search) || search.equals("")) {
					games.add(gi);	
				}
			}

			GameList tempgl = new GameList(games);
			applyFilter(tempgl);
		}
	}

	private void joinGame() {
		if(table.getSelectedRow() >= 0) {

			String name = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
			String password = null;

			if(((Boolean)table.getModel().getValueAt(table.getSelectedRow(), 3)) == true) {
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
			else if(result.equals("fail")) {
				JOptionPane.showMessageDialog(this,Main.texts.getString("fail"), Main.texts.getString("error"), JOptionPane.ERROR_MESSAGE);
				refreshTable();
			} else
				Main.getView().setPanel("GameScreen", false);
		}
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

			res = Main.getModel().createGame(name, password, pmax, turns, 
					jcbDifficulty.getSelectedIndex()+1);

			if(res.equals("success")) {
				Main.getView().setPanel("GameScreen", true);
				toFade = false;
				fadeEffect.setVisible(false);
				this.repaint();
			} else
				JOptionPane.showMessageDialog(this, Main.texts.getString("fail"));
		} else {
			JOptionPane.showMessageDialog(this, Main.texts.getString("name_short"));
		}

	}

	private void refreshTable() {
		gl = Main.getModel().getGameList();
		applySearch(gl);
	}


	private void applyFilter(GameList gl) {
		if(gl != null) {
			ArrayList<GameInfo> games = new ArrayList<GameInfo>();

			for(GameInfo gi : gl.games) {
				boolean full = (gi.nbPlayers == gi.maxPlayers);
				boolean password = gi.password;
				boolean started = gi.started;
				boolean add = true;
				if(!displayPrivate && password) {
					add = false;
				}

				if(!displayFull && full) {
					add = false;
				}

				if(!displayActive && started) {
					add = false;
				}

				if(add)
					games.add(gi);
			}

			GameList tempgl = new GameList(games);
			GameTableModel gm = new GameTableModel(tempgl);
			table.setModel(gm);
		}
	}
}
