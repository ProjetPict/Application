package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import socketData.PlayerScore;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JButton;

public class ChatArea extends JPanel implements ActionListener, Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private PlayerScore[] scores;
	private JList list;
	private JLabel lblAnswer;
	private JButton btnStartGame;
	private boolean creator;
	private JLabel lblTimer;
	private Timer timer;
	private int time;
	private boolean drawing;

	public ChatArea(boolean creator) {
		setBackground(Color.WHITE);
		scores = new PlayerScore[]{};
		setLayout(null);
		drawing = true;
		textField = new JTextField();
		textField.setBounds((int)(132*Main.ratioX), (int)(646*Main.ratioY), 186, 20);
		add(textField);
		textField.setColumns(10);
		textField.addActionListener(this);

		lblAnswer = new JLabel(Main.texts.getString("answer"));
		lblAnswer.setBounds((int)(76*Main.ratioX), (int)(649*Main.ratioY), 46, 14);
		add(lblAnswer);

		Main.getModel().addObserver(this);

		list = new JList();
		list.setBounds(83, 84, 284, 80);
		add(list);

		lblTimer = new JLabel();
		lblTimer.setBounds((int)(50*Main.ratioX), (int)(20*Main.ratioY), 46, 14);
		add(lblTimer);
		
		if(creator){
			btnStartGame = new JButton(Main.texts.getString("startgame"));
			btnStartGame.setBounds((int)(83*Main.ratioX), (int)(296*Main.ratioY), 89, 23);
			btnStartGame.addActionListener(this);
			add(btnStartGame);
		}
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		textField.setBounds((int)(132*Main.ratioX), (int)(646*Main.ratioY), 186, 20);
		lblAnswer.setBounds((int)(76*Main.ratioX), (int)(649*Main.ratioY), 46, 14);
		if(creator)
			btnStartGame.setBounds((int)(83*Main.ratioX), (int)(296*Main.ratioY), 89, 23);

		g.drawLine(0, 0, 0, this.getHeight());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == textField)
		{
			if(!textField.getText().equals("") && getParent() instanceof GameScreen && textField.getText().length() > 2)
			{
				Main.getModel().sendAnswer(textField.getText(), ((GameScreen)getParent()).getNbPixels());
				textField.setText("");
			}
		}
		else if(e.getSource() == btnStartGame)
		{
			Main.getModel().sendCommand("startgame");
			btnStartGame.setVisible(false);
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
				textField.setEnabled(false);
			else
				textField.setEnabled(true);
			
		}
		else{
			scores = Main.getModel().getScores().values().toArray(new PlayerScore[Main.getModel().getScores().values().size()+1]);
			if(scores.length > 0)
			{
				list.setListData(scores);
			}
		}
	}
	
}
