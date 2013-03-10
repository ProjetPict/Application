package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import socketData.*;

/**
 * Ecran de selection/creation de partie
 * @author christopher
 *
 */
public class Browser extends JPanel implements ActionListener{

	private static final long serialVersionUID = 5874691608463169886L;

	private JButton btnJoin;
	private JButton btnPnlCreate;
	private JButton btnCreate;
	private JButton btnRefresh;

	private JLabel lblGameName;
	private JTextField txtGameName;
	private JLabel lblGamePassword;
	private JTextField txtGamePassword;
	private JLabel lblGamePmax;
	private JTextField txtGamePmax;

	private JScrollPane scrlPane;
	private JTable table;
	private JPanel slctPane;
	private JPanel slctSubPane;
	private JPanel crtPane;
	private JPanel crtSubPane;

	private GameList gl;


	public Browser(){
		btnJoin = new JButton("Connexion");
		btnPnlCreate = new JButton("Creer une partie");
		btnCreate = new JButton("Envoyer");
		btnRefresh = new JButton("Actualiser");
		lblGameName = new JLabel("Nom de la partie");
		txtGameName = new JTextField();
		lblGamePassword = new JLabel("Code d'accès à la partie");
		txtGamePassword = new JTextField();
		lblGamePmax = new JLabel("Nombre de joueurs autorisés");
		txtGamePmax = new JTextField();


		slctPane = new JPanel();
		slctSubPane = new JPanel();
		crtPane = new JPanel();
		crtPane.setVisible(false);
		crtSubPane = new JPanel();


		table = new JTable();
		table.setShowGrid(false);
		table.setShowVerticalLines(true);
		table.setDefaultRenderer(Boolean.class, new BooleanCellRenderer());


		fillTable();

		//scrlPane.setPreferredSize(new Dimension(100, 400));
		scrlPane = new JScrollPane(table);

		slctPane.setLayout(new BorderLayout());
		slctSubPane.setLayout(new BorderLayout());
		slctSubPane.add(scrlPane, BorderLayout.CENTER);

		slctSubPane.add(btnJoin, BorderLayout.SOUTH);
		slctPane.add(slctSubPane,  BorderLayout.CENTER);
		slctPane.add(btnPnlCreate, BorderLayout.SOUTH);

		crtPane.setLayout(new BorderLayout());
		crtSubPane.setLayout(new GridLayout(3,2));
		crtSubPane.add(lblGameName);
		crtSubPane.add(txtGameName);
		crtSubPane.add(lblGamePassword);
		crtSubPane.add(txtGamePassword);
		crtSubPane.add(lblGamePmax);
		crtSubPane.add(txtGamePmax);
		crtPane.add(crtSubPane);
		crtPane.add(btnCreate, BorderLayout.SOUTH);

		btnJoin.addActionListener(this);
		btnPnlCreate.addActionListener(this);
		btnCreate.addActionListener(this);
		btnRefresh.addActionListener(this);

		this.setLayout(new BorderLayout());
		this.add(slctPane, BorderLayout.WEST);
		this.add(crtPane, BorderLayout.EAST);
	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btnJoin){
			joinGame();
		}
		else if(arg0.getSource() == btnPnlCreate){
			crtPane.setVisible(true);
		} 
		else if(arg0.getSource() == btnCreate){
			createGame();
		}
		else if(arg0.getSource() == btnRefresh){
			fillTable();
		} 
	}

	private void joinGame()
	{
		String name = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
		String password = null;
		if(((Boolean)table.getModel().getValueAt(table.getSelectedRow(), 2)) == true)
		{
			password = JOptionPane.showInputDialog(this,
					"Entrez le mot de passe : ",
					"Mot de passe",
					JOptionPane.QUESTION_MESSAGE);
		}
		String result = Main.getModel().joinGame(name, password);
		if(result.equals("wrongpassword"))
			JOptionPane.showMessageDialog(this,"Mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
		else if(result.equals("gamefull"))
			JOptionPane.showMessageDialog(this,"La partie est pleine.", "Erreur", JOptionPane.ERROR_MESSAGE);
		else
			Main.getView().setPanel("GameScreen");
	}

	private void createGame()
	{
		String name = txtGameName.getText();
		String password = txtGamePassword.getText();
		String res = "";
		int pmax;
		try{
			pmax = Integer.valueOf(txtGamePmax.getText());
		}catch(Exception e)
		{
			pmax = 0;
		}

		if(name.length() >= 4)
		{
			if(password.equals(""))
				password = null;
			res = Main.getModel().createGame(name, password, pmax);
			JOptionPane.showMessageDialog(this,res);
			Main.getView().setPanel("GameScreen");
		}
		else
		{
			res = "Nom trop court";
			JOptionPane.showMessageDialog(this,res);
		}

		//TODO : rejoindre la partie cree a l'instant
	}

	private void fillTable()
	{
		gl = Main.getModel().getGameList();
		table.setModel(new GameTableModel(gl));
	}

}
