package games.hangman;

/**
 * This is where the logic of Hangman happens
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import games.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Player;

public class Hangman extends Game implements ActionListener, Runnable{
	
	/* The list of words */
	private Vector<String> wordList = new Vector<String>();
	/* The Draw Panel */
	private DrawPanel drawPanel;
	/* The button panel */
	private JPanel buttonPanel;
	/* The word being used now */
	private String currentWord;
	/* The number of lifes you have */
	private int lifes = 0;
	/* The reader of the words in the dictionary */
	private FileReader fr;
	/* Used for loading words */
	private BufferedReader br;
	
	/**
	 * The constructor of Hangman
	 * 
	 * @throws IOException
	 */
	public Hangman() throws IOException {
		super("Hangman", "hangman.png", new JFrame(), new JPanel(), 800 + 10 + 20, 800 + 10 + 20, 500 + 10 + 20, 500 + 10 + 20, 2);
		this.fr = new FileReader("dictonary_english_hangman.txt");
		this.br = new BufferedReader(fr);
		//load the GUI just to let person know it's there
		
		populateWordList();
		
		new Thread(this).start();
	}
	
	public Hangman(String localplayer, String remoteplayer, boolean goFirst, String ipaddress, Boolean gofirst) throws UnknownHostException, IOException {
		super("Hangman", "hangman.png", new JFrame(), new JPanel(), 800 + 10, 800 + 10, 500 + 10, 500 + 10, 2);
	}
	
	private void populateWordList() {
		while(true){
			String temp;
			try {
				temp = br.readLine();
			if(temp == null)
				break; //break if null
			wordList.add(br.readLine()); //have the words I need
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * The main method
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new Hangman();
	}

	/**
	 * The method for any actions performed
	 * 
	 * @param e The action that triggers the method
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		ButtonLetter bl = (ButtonLetter) e.getSource();
		bl.setBackground(Color.white);
        bl.setForeground(Color.black);
		bl.setEnabled(false);
		String hitLetter = String.valueOf(bl.letter);
		if(currentWord.contains(hitLetter.toUpperCase()))
		{
			System.out.println("hitter up!");
			int temp = 0;
			while(temp < currentWord.length()){
				if (hitLetter.toUpperCase().equals(String.valueOf((currentWord.charAt(temp))).toUpperCase())) {
					drawPanel.addLetter(bl.letter, temp);
				}
				temp++;
			}
			
			if(drawPanel.gameOver() == true) {
				gameOver();
			}
		}
		else{
			if(drawPanel.gameOver() == false)
				lifes = drawPanel.getNextState();
			System.out.println(lifes);
			if(lifes == 5){
				gameOver();
			}
		}
	}

	/**
	 * This method starts a new thread
	 * @throws IOException 
	 */
	public void startNewThread() throws IOException{ //this is ONLY needed for gameOver method!
		new Thread(this);
		Thread.currentThread().interrupt();
		new Hangman();
	}
	
	/**
	 * The game over method
	 */
	private void gameOver() {
		JFrame endWindow = new JFrame("GameOver");
		JPanel endPanel = new JPanel();
		endPanel.setBackground(Color.black);
		endPanel.setBorder(BorderFactory.createLineBorder(saddleBrown, 10));
		endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));
		
		JLabel didYouWin = new JLabel("You Lose!!!");
		JLabel correctWord = new JLabel("The correct word was: "+ currentWord + "!");
		if (drawPanel.gameOver() == true) {
			didYouWin = new JLabel("You Win!!!");
			correctWord = new JLabel("The correct word was: "+ currentWord + "!");
		}
		didYouWin.setFont(new Font("Comic Sans MS", Font.ITALIC, 35));
		didYouWin.setForeground(Color.white);
		didYouWin.setAlignmentX(Component.CENTER_ALIGNMENT);
		correctWord.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		correctWord.setForeground(Color.white);
		correctWord.setAlignmentX(Component.CENTER_ALIGNMENT);
		endPanel.add(didYouWin);
		endPanel.add(correctWord);
		endWindow.setSize(END_FRAME_WIDTH, END_FRAME_HEIGHT);
		JButton newGame = new JButton("Play Again?");
		newGame.setBackground(Color.black);
        newGame.setForeground(Color.WHITE);
        newGame.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		endPanel.add(newGame);
		endWindow.add(endPanel);
		endWindow.setUndecorated(true);
		endWindow.setAlwaysOnTop(true);
		newGame.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								resetAllLetters();
								getGamePanel().validate();
								getGameFrame().validate();
								getGameFrame().dispose();
								endWindow.dispose();
								
								
								try {
									startNewThread();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
							}
						});
		endWindow.pack();
		endWindow.setVisible(true);
		endWindow.setLocationRelativeTo(getGameFrame());

	}
	
	private void resetAllLetters() {
		for (Component c : buttonPanel.getComponents()) {
			char temp = 'A';
			for(int i = 0; i < 26; i++){
				if (c instanceof JButton) {
					if (c.getName().equals(String.valueOf(temp))) {
						c.setEnabled(true);
					}
				}
				temp++;
			}
		}
	}

	/**
	 * This method has the game loop for the game
	 */
	@Override
	public void run() {
		this.getGameFrame().setVisible(true);
		JPanel ultimatePanel = new JPanel();
		try {
			drawPanel = new DrawPanel(new Dimension(getGameFrame().getWidth(),550));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		drawPanel.setBackground(Color.black);
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(getGameFrame().getWidth(), 200));
		buttonPanel.setBackground(Color.lightGray);
		buttonPanel.setBorder(BorderFactory.createLineBorder(saddleBrown, 10));
		setGamePanel(ultimatePanel);
		getGamePanel().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 0;
		getGamePanel().add(drawPanel, c);
		c.fill = GridBagConstraints.SOUTH;
		c.gridx = 0;
		c.gridy = 1;
		getGamePanel().add(buttonPanel, c);
		getGameFrame().pack();
		
		
		//get the word
		int d = (int) (Math.random()*wordList.size());
		currentWord = wordList.elementAt(d);
		currentWord = currentWord.toUpperCase();
		System.out.println("Current word is: " + currentWord);
		drawPanel.setWord(currentWord);//draw blanks for everything!
		char temp = 'A';
		for(int i = 0; i < 26; i++){
			ButtonLetter bl = new ButtonLetter(temp);
			bl.setName(String.valueOf(temp));
			bl.addActionListener(this);
			bl.setFont(new Font("Default", Font.BOLD, 15));
			buttonPanel.add(bl);
			temp ++;
		}
		getGamePanel().validate();
		getGameFrame().validate();
	}

	@Override
	public boolean updateMove(int numx, int numy, Player p1) {
		// TODO Auto-generated method stub
		return false;
	}

}
