package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import socketData.ChatCommand;
import socketData.PlayerScore;
import socketData.ValueCommand;

import java.awt.Color;
import java.awt.Component;
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
	private JList<PlayerScore> list;
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
	private JLabel lblTurns;
	private Timer timer;
	private int time;
	private boolean drawing;
	private boolean running;
	private StyledDocument chatDoc;
	private MutableAttributeSet chatMAS;
	private Font titleFont = new Font("Arial", Font.PLAIN, 26);
	private int turn;
	private int maxTurn;

	public ChatArea(boolean creator) {
		running = false;
		turn = 0;
		maxTurn = 0;

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

		list = new JList<PlayerScore>();

		list.setCellRenderer(new DefaultListCellRenderer(){
			
			private static final long serialVersionUID = 1L;

			public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean isSelected,boolean cellHasFocus)
			{
				if (value instanceof PlayerScore) 
				{
					PlayerScore player = (PlayerScore)value;
					String pos;
					
					switch(index)
					{
					case 0:
						pos = index+1+Main.texts.getString("first");
						break;
					case 1:
						pos = index+1+Main.texts.getString("second");
						break;
					case 2:
						pos = index+1+Main.texts.getString("third");
						break;
					default:
						pos = index+1+Main.texts.getString("no_podium");
						break;
					}
					
					setText(pos + " - " + player.toString());
					
					if(player.drawing)
						setForeground(new Color(51, 51, 255));
					else if(player.hasFound)
						setForeground(new Color(0, 153, 0));
					else if(player.isGhost)
						setForeground(Color.GRAY);
					else
						setForeground(Color.BLACK);
					
					setBackground(Color.WHITE);
				}     
				return this;
			}
		});
		scrollPaneScore = new JScrollPane(list);
		add(scrollPaneScore);

		chat = new JTextPane();
		chat.setEditable(false);
		chatDoc = chat.getStyledDocument();
		chatMAS = chat.getInputAttributes();
		try {
			StyleConstants.setItalic(chatMAS, true);
			chatDoc.insertString(0, Main.texts.getString("welcome_chat"), chatMAS);
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


		lblTimer = new JLabel(Main.texts.getString("waiting_launch"),SwingConstants.CENTER);
		if(creator)
			lblTimer.setText(Main.texts.getString("waiting_players"));
		lblTimer.setFont(titleFont);
		add(lblTimer);

		lblTurns = new JLabel(Main.texts.getString("waiting_turns"));
		Font italic = new Font("Arial",Font.ITALIC,14);
		lblTurns.setFont(italic);
		add(lblTurns);

		this.creator = creator;

		lblScore = new JLabel(Main.texts.getString("scores"));
		add(lblScore);
		lblScore.setFont(titleFont);
		lblChat = new JLabel(Main.texts.getString("chat"));
		add(lblChat);
		lblChat.setFont(titleFont);

		btnQuitGame = new JButton(Main.texts.getString("quit_game"));
		btnQuitGame.addActionListener(this);
		add(btnQuitGame);

		if(creator){
			btnStartGame = new JButton(Main.texts.getString("startgame"));
			btnStartGame.addActionListener(this);
			add(btnStartGame);
		}

	}

	public void enableStartButton(boolean enable) {
		if(btnStartGame != null && creator) {
			btnStartGame.setVisible(enable);
			lblTimer.setVisible(!enable);
		}
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		btnQuitGame.setBounds(10, 10, 380, 30);
		lblTimer.setBounds(10, 45, 380, 30);
		lblTimer.setAlignmentX(CENTER_ALIGNMENT);
		if(creator)
			btnStartGame.setBounds(10, 45, 380, 30);
		lblScore.setBounds(10, 85, 380, 30);
		lblTurns.setBounds(280, 88, 380, 30);
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
		if(e.getSource() == textAnswer) {
			if(!textAnswer.getText().equals("") && getParent() instanceof GameScreen && textAnswer.getText().length() > 2) {
				Main.getModel().sendAnswer(textAnswer.getText(), ((GameScreen)getParent()).getNbPixels());
				textAnswer.setText("");
			}
		}
		else if(e.getSource() == textChat) {
			if(!textChat.getText().equals("") && getParent() instanceof GameScreen && textChat.getText().length() > 0) {
				Main.getModel().sendChatMessage(textChat.getText());
				textChat.setText("");
			}
		}
		else if(e.getSource() == btnStartGame) {
			if(scores.length>1) {
				Main.getModel().sendCommand("startgame");
			}
		}
		else if(e.getSource() == btnQuitGame) {
			Main.getView().quitGame();
		}

	}

	public void launchTimer() {
		this.time = 60;
		if(timer != null)
			timer.cancel();
		timer = new Timer();
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

	public void cancelTimer() {
		time = 0;
		timer.cancel();
		timer = null;
		lblTimer.setText(Main.texts.getString("waiting_drawer"));
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Boolean) {
			drawing = (Boolean) arg;
			if(drawing == true) {
				textAnswer.setEditable(false);
				textAnswer.setText(Main.getModel().getWord());
			} else {
				textAnswer.setEditable(true);
				textAnswer.setText("");
			}
		}
		else if(arg instanceof ValueCommand)
		{
			ValueCommand cmd = (ValueCommand) arg;

			if(cmd.command.equals("turn"))
				turn = cmd.value;
			else
				maxTurn = cmd.value;

			lblTurns.setText(Main.texts.getString("turns") + turn + "/" + maxTurn);
		}
		else if(arg instanceof ChatCommand) {
			ChatCommand cmd = (ChatCommand)arg;
			try {
				if(cmd.author == null || cmd.author.equals("")) {
					StyleConstants.setItalic(chatMAS, true);
					chatDoc.insertString(chatDoc.getEndPosition().getOffset(), cmd.command, chatMAS);
					StyleConstants.setItalic(chatMAS, false);
					chatDoc.insertString(chatDoc.getEndPosition().getOffset(), "\n", chatMAS);
				} else {
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
		else if(arg instanceof String) {
			if(((String) arg).equals("endgame")) {
				running = false;
				enableStartButton(true);
			}
			else if(((String) arg).equals("startgame")) {
				running = true;
				enableStartButton(false);
			}
			else if(((String) arg).equals("goodword")) {
				textAnswer.setEditable(false);
				textAnswer.setText(Main.texts.getString("goodanswer"));
				lblAnswer.setForeground(new Color(0, 153, 0));
			}
			else if(((String) arg).equals("wrongword")) {
				lblAnswer.setForeground(new Color(204, 0, 0));
			}
		} else {
			//TODO delete si plus besoin
			//Collection<PlayerScore> temp = Main.getModel().getScores();
			//scores = new PlayerScore[temp.size()];
			scores = Main.getModel().getScores().toArray(new PlayerScore[Main.getModel().getScores().size()]);
			//scores = Main.getModel().getScores().values().toArray(new PlayerScore[Main.getModel().getScores().values().size()+1]);
			/*Iterator<PlayerScore> it = temp.iterator();
			int i = 0;
			while(it.hasNext()) {
				PlayerScore ps = it.next();
				scores[i] = ps;
				i++;
			}*/

			if(scores.length > 0) {
				list.setListData(scores);
			}

			if(scores.length > 1 && !running) {
				enableStartButton(true);
			} else {
				enableStartButton(false);
			}
		}
	}

	public void enableAnswer(boolean enable) {
		lblAnswer.setForeground(Color.BLACK);
		if(!drawing) {
			textAnswer.setEditable(enable);
		}
	}
}
