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
	private boolean creator;
	private JLabel lblTimer;
	private Timer timer;
	private int time;
	private boolean drawing;
	private StyledDocument chatDoc;
	private MutableAttributeSet chatMAS;

	public ChatArea(boolean creator) {

		setBackground(Color.WHITE);
		scores = new PlayerScore[]{};
		setLayout(null);
		drawing = true;
		textAnswer = new JTextField();
		textAnswer.setBounds((int)(132*Main.ratioX), (int)(646*Main.ratioY), 186, 20);
		add(textAnswer);
		textAnswer.setColumns(10);
		textAnswer.addActionListener(this);

		lblAnswer = new JLabel(Main.texts.getString("answer"));
		lblAnswer.setBounds((int)(42*Main.ratioX), (int)(650*Main.ratioY), 46, 30);
		add(lblAnswer);

		Main.getModel().addObserver(this);

		list = new JList();
		list.setBounds((int)(42*Main.ratioX), (int)(84*Main.ratioY), (int)(200*Main.ratioX), (int)(150*Main.ratioY));
		scrollPaneScore = new JScrollPane(list);
		scrollPaneScore.setBounds((int)(42*Main.ratioX), (int)(84*Main.ratioY),  (int)(200*Main.ratioX), (int)(150*Main.ratioY));
		add(scrollPaneScore);

		chat = new JTextPane();
		chat.setEditable(false);
		chat.setBounds((int)(42*Main.ratioX), (int)(300*Main.ratioY), (int)(200*Main.ratioX), (int)(300*Main.ratioY));
		chatDoc = chat.getStyledDocument();
		chatMAS = chat.getInputAttributes();
		/*chat.setLineWrap(true);
		chat.setWrapStyleWord(true);*/

		scrollPaneChat = new JScrollPane(chat);

		scrollPaneChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneChat.setBounds((int)(42*Main.ratioX), (int)(300*Main.ratioY), (int)(200*Main.ratioX), (int)(300*Main.ratioY));
		add(scrollPaneChat);

		textChat = new JTextField();
		textChat.addActionListener(this);
		textChat.setBounds((int)(42*Main.ratioX), (int)(600*Main.ratioY), (int)(200*Main.ratioX), 20);
		add(textChat);


		lblTimer = new JLabel();
		lblTimer.setBounds((int)(50*Main.ratioX), (int)(20*Main.ratioY), 46, 14);
		add(lblTimer);

		if(creator){
			btnStartGame = new JButton(Main.texts.getString("startgame"));
			btnStartGame.setBounds((int)(83*Main.ratioX), (int)(50*Main.ratioY), 89, 23);
			btnStartGame.addActionListener(this);
			add(btnStartGame);
		}
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		textAnswer.setBounds((int)(100*Main.ratioX), (int)(650*Main.ratioY), 178, 20);
		//textChat.setBounds((int)(42*Main.ratioX), (int)(600*Main.ratioY), (int)(200*Main.ratioX), 20);
		lblAnswer.setBounds((int)(42*Main.ratioX), (int)(650*Main.ratioY), 100, 20);
		scrollPaneChat.setBounds((int)(42*Main.ratioX), (int)(300*Main.ratioY), (int)(200*Main.ratioX), (int)(300*Main.ratioY));
		textChat.repaint();
		scrollPaneChat.repaint();
		scrollPaneScore.setBounds((int)(42*Main.ratioX), (int)(84*Main.ratioY),  (int)(200*Main.ratioX), (int)(150*Main.ratioY));
		if(creator)
			btnStartGame.setBounds((int)(83*Main.ratioX), (int)(296*Main.ratioY), 89, 23);

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
			btnStartGame.setVisible(false);
			}
		}

	}

	public void launchTimer()
	{
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



	@Override
	public void update(Observable o, Object arg) {

		if(arg instanceof Boolean)
		{
			drawing = (Boolean) arg;

			if(drawing == true)
				textAnswer.setEnabled(false);
			else
				textAnswer.setEnabled(true);

		}
		else if(arg instanceof ChatCommand){

			ChatCommand cmd = (ChatCommand)arg;
			try {

				if(cmd.author == null || cmd.author.equals(""))
				{
					StyleConstants.setItalic(chatMAS, true);
					chatDoc.insertString(chatDoc.getEndPosition().getOffset(), cmd.command + "\n", chatMAS);
				}
				else{
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
		}
	}

}
