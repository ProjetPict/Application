package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

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
	
	private JLabel lblGameName;
	private JTextField txtGameName;
	private JLabel lblGamePassword;
	private JTextField txtGamePassword;
	private JLabel lblGamePmax;
	private JTextField txtGamePmax;
	
	private JScrollPane scrlPane;
	private JList list;
	private JPanel slctPane;
	private JPanel slctSubPane;
	private JPanel crtPane;
	private JPanel crtSubPane;
	
	private GameList gl;
	private DefaultListModel dlm;
	
	public Browser(){
		btnJoin = new JButton("Connexion");
		btnPnlCreate = new JButton("Creer une partie");
		btnCreate = new JButton("Envoyer");
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
		
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(15);
		list.setCellRenderer(new DefaultListCellRenderer());
		dlm = new DefaultListModel();
		
		fillList();
		
		//scrlPane.setPreferredSize(new Dimension(100, 400));
		scrlPane = new JScrollPane(list);
		
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
		
		this.setLayout(new BorderLayout());
		this.add(slctPane, BorderLayout.WEST);
		this.add(crtPane, BorderLayout.EAST);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btnJoin){
			//TODO : rejoindre la partie selectionnée
			Main.getView().setPanel("GameScreen");
		}
		if(arg0.getSource() == btnPnlCreate){
			crtPane.setVisible(true);
		} 
		if(arg0.getSource() == btnCreate){
			String res = Main.getModel().createGame(txtGameName.getText(), txtGamePassword.getText(), Integer.valueOf(txtGamePmax.getText()));
			javax.swing.JOptionPane.showMessageDialog(null,res);
			//TODO : rejoindre la partie créée à l'instant
			Main.getView().setPanel("GameScreen");
		}
	}
	
	private void fillList(){
		//On remplit la liste
		gl = Main.getModel().getGameList();

		while(!dlm.isEmpty())
			dlm.remove(0);
		
		for (GameInfo g : gl.games){
			dlm.addElement(g.name);
		}
		list.setModel(dlm);
	}
	
}
