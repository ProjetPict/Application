package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import socketData.ChatCommand;
import socketData.PlayerScore;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatArea extends JPanel implements ActionListener, Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textAnswer;
	private PlayerScore[] scores;
	private JList list;
	private JTextPane chat;
	private JScrollPane scrollPaneChat;
	private JScrollPane scrollPaneScore;
	private JTextField textChat;
	private JLabel lblAnswer;
	private JButton btnStartGame;
	private JButton btnQuitGame;
	private boolean creator;
	private JLabel lblTimer;
	private JLabel lblScore;
	private JLabel lblChat;
	private Timer timer;
	private int time;
	private boolean drawing;
	private boolean running;
	private StyledDocument chatDoc;
	private MutableAttributeSet chatMAS;
	private Font titleFont = new Font("Arial", Font.PLAIN, 26);
	
	public ChatArea(boolean creator) {
		
		running = false;
		
		
		setBackground(Color.WHITE);
		scores = new PlayerScore[]{};
		setLayout(null);
		drawing = false;
		textAnswer = new JTextField();
		add(textAnswer);
		textAnswer.setColumns(10);
		textAnswer.addActionListener(this);
		textAnswer.setEditable(false);
		
		lblAnswer = new JLabel(Main.texts.getString("answer"));
		add(lblAnswer);
		lblAnswer.setFont(titleFont);

		Main.getModel().addObserver(this);

		list = new JList();
		scrollPaneScore = new JScrollPane(list);
		add(scrollPaneScore);

		chat = new JTextPane();
		chat.setEditable(false);
		chatDoc = chat.getStyledDocument();
		chatMAS = chat.getInputAttributes();
		try {
			StyleConstants.setItalic(chatMAS, true);
			chatDoc.insertString(0, "Bienvenue dans le chat. Ne communiquez aucune information personnelle ici (mot de passe, adresse, nom).", chatMAS);
			StyleConstants.setItalic(chatMAS, false);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		scrollPaneChat = new JScrollPane(chat);
		scrollPaneChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPaneChat);

		textChat = new JTextField();
		textChat.addActionListener(this);
		add(textChat);


		lblTimer = new JLabel();
		add(lblTimer);
		
		this.creator = creator;

		lblScore = new JLabel(Main.texts.getString("scores"));
		add(lblScore);
		lblScore.setFont(titleFont);
		lblChat = new JLabel(Main.texts.getString("chat"));
		add(lblChat);
		lblChat.setFont(titleFont);
		
		btnQuitGame = new JButton("Quitter la partie");
		add(btnQuitGame);

		if(creator){
			btnStartGame = new JButton(Main.texts.getString("startgame"));
			btnStartGame.addActionListener(this);
			add(btnStartGame);
		}
		
	}

	public void enableStartButton(boolean enable)
	{
		if(btnStartGame != null && creator)
		{

			btnStartGame.setVisible(enable);
		}
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		btnQuitGame.setBounds(10, 10, 380, 30);
		if(creator)
			btnStartGame.setBounds(10, 45, 380, 30);
		lblTimer.setBounds(10, 45, 380, 30);
		lblScore.setBounds(10, 85, 380, 30);
		scrollPaneScore.setBounds(10, 120, 380, 220);
		lblAnswer.setBounds(10, 350, 380, 30);
		textAnswer.setBounds(10, 385, 380, 30);
		lblChat.setBounds(10, 425, 380, 30);
		scrollPaneChat.setBounds(10, 460, 380, 120);
		textChat.setBounds(10, 585, 380, 30);
		scrollPaneScore.setBackground(Color.white);
		scrollPaneChat.setBackground(Color.white);
		textChat.repaint();
		scrollPaneChat.repaint();
		g.drawLine(0, 0, 0, this.getHeight());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == textAnswer)
		{
			if(!textAnswer.getText().equals("") && getParent() instanceof GameScreen && textAnswer.getText().length() > 2)
			{
				Main.getModel().sendAnswer(textAnswer.getText(), ((GameScreen)getParent()).getNbPixels());
				textAnswer.setText("");
			}
		}
		else if(e.getSource() == textChat)
		{
			if(!textChat.getText().equals("") && getParent() instanceof GameScreen && textChat.getText().length() > 0)
			{
				Main.getModel().sendChatMessage(textChat.getText());
				textChat.setText("");
			}
		}
		else if(e.getSource() == btnStartGame)
		{
			if(scores.length>1)
			{
				Main.getModel().sendCommand("startgame");
			}
		}

	}

	public void launchTimer()
	{
		this.time = 60;
		if(timer != null)
			timer.cancel();
		timer = new Timer();
		lblTimer.setVisible(true);

		lblTimer.setText(String.valueOf(time));
		timer.schedule(new TimerTask() {
			public void run() {
				time--;

				lblTimer.setText(String.valueOf(time));
				if(time <= 0)
					this.cancel();
			}
		}, 0, 1000);

	}


	public void cancelTimer()
	{
		time = 0;
		timer.cancel();
		timer = null;
		lblTimer.setVisible(false);
	}



	@Override
	public void update(Observable o, Object arg) {

		if(arg instanceof Boolean)
		{
			drawing = (Boolean) arg;

			if(drawing == true)
			{
				textAnswer.setEditable(false);
				textAnswer.setText(Main.getModel().getWord());
			}
			else
			{
				textAnswer.setEditable(true);
				textAnswer.setText("");
			}

		}
		else if(arg instanceof ChatCommand){

			ChatCommand cmd = (ChatCommand)arg;
			try {

				if(cmd.author == null || cmd.author.equals(""))
				{
					StyleConstants.setItalic(chatMAS, true);
					chatDoc.insertString(chatDoc.getEndPosition().getOffset(), cmd.command, chatMAS);
					StyleConstants.setItalic(chatMAS, false);
					chatDoc.insertString(chatDoc.getEndPosition().getOffset(), "\n", chatMAS);
					
				}
				else{
					StyleConstants.setItalic(chatMAS, false);
					StyleConstants.setBold(chatMAS, true);
					chatDoc.insertString(chatDoc.getEndPosition().getOffset(), cmd.author, chatMAS);
					StyleConstants.setBold(chatMAS, false);
					chatDoc.insertString(chatDoc.getEndPosition().getOffset(), " : " + cmd.command + "\n", chatMAS);
				}
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			chat.setCaretPosition(chat.getDocument().getLength());
		}
		else if(arg instanceof String)
		{
			if(((String) arg).equals("endgame"))
			{
				running = false;
				enableStartButton(true);
			}
			else if(((String) arg).equals("startgame"))
			{
				running = true;
				enableStartButton(false);
			}
			else if(((String) arg).equals("goodword"))
			{
				textAnswer.setEditable(false);
				textAnswer.setText(Main.texts.getString("goodanwser"));
				lblAnswer.setForeground(Color.GREEN);
			}
			else if(((String) arg).equals("wrongword"))
			{
				lblAnswer.setForeground(Color.RED);
			}
			
		}
		else{
			Collection<PlayerScore> temp = Main.getModel().getScores().values();
			scores = new PlayerScore[temp.size()];
			//scores = Main.getModel().getScores().values().toArray(new PlayerScore[Main.getModel().getScores().values().size()+1]);

			Iterator<PlayerScore> it = temp.iterator();

			int i = 0;
			while(it.hasNext())
			{
				PlayerScore ps = it.next();
				scores[i] = ps;
				i++;
			}

			if(scores.length > 0)
			{
				list.setListData(scores);
			}
			if(scores.length > 1 && !running)
			{
				enableStartButton(true);
			}
			else
				enableStartButton(false);
		}
	}
	
	public void enableAnswer(boolean enable) {
		lblAnswer.setForeground(Color.BLACK);
		if(!drawing)
		{
			textAnswer.setEditable(enable);
		}
	}
}
