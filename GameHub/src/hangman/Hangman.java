package hangman;

/**
 * This is where the logic of Hangman happens
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Hangman implements ActionListener, Runnable{
	
	/* The Main Window */
	private JFrame mainWindow;
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
	
	/* The height of the frame */
	public final static int FRAME_HEIGHT = 800;
	/* The width of the frame */
	public final static int FRAME_WIDTH = 800;
	/* The height of the end frame */
	public final static int END_FRAME_HEIGHT = 150;
	/* The width of the end frame */
	public final static int END_FAME_WIDTH = 500;
	
	/**
	 * The constructor of Hangman
	 * 
	 * @throws IOException
	 */
	public Hangman() throws IOException {
		FileReader fr = new FileReader("dictonary_english_hangman.txt");
		BufferedReader br = new BufferedReader(fr);
		//load the GUI just to let person know it's there
		mainWindow = new JFrame("Hangman!");
		mainWindow.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		mainWindow.setResizable(false);
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		while(true){
			String temp = br.readLine();
			if(temp == null)
				break; //break if null
			wordList.add(br.readLine()); //have the words I need
		}
		System.out.println("Now beginning game!");
		new Thread(this).start();
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
		System.out.println(bl.letter); //The letter that was pressed
		bl.setEnabled(false);
		String hitLetter = String.valueOf(bl.letter);
		if(currentWord.contains(hitLetter.toUpperCase()))
		{
			System.out.println("hitter up!");
			int temp = currentWord.indexOf(bl.letter);
			while(temp != -1){
				System.out.println("adding letter!");
				drawPanel.addLetter(bl.letter, temp);
				temp = currentWord.indexOf(temp+1);
			}
		}
		else{
			lifes = drawPanel.getNextState();
			System.out.println(lifes);
			if(lifes == 5){
				drawPanel.gameOver();
				gameOver();
			}
		}
	}

	/**
	 * This method starts a new thread
	 */
	public void startNewThread(){ //this is ONLY needed for gameOver method!
		new Thread(this).start();
	}
	
	/**
	 * The game over method
	 */
	private void gameOver() {
		JFrame endWindow = new JFrame("GameOver");
		JLabel correctWord = new JLabel("The correct word was: "+ currentWord + "!");
		correctWord.setFont(new Font("Default", Font.BOLD, 25));
		correctWord.setForeground(Color.blue);
		endWindow.getContentPane().add(correctWord);
		endWindow.setSize(END_FRAME_WIDTH, END_FRAME_HEIGHT);
		JButton newGame = new JButton("Play Again?");
		endWindow.add(newGame, "South");
		newGame.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								mainWindow.dispose();
								endWindow.dispose();
								mainWindow = new JFrame("Hangman!");
								mainWindow.setSize(FRAME_WIDTH, FRAME_HEIGHT);
								mainWindow.setResizable(false);
								mainWindow.setVisible(true);
								startNewThread();
							}
						});
		endWindow.setVisible(true);
		endWindow.setLocationRelativeTo(mainWindow);

	}

	/**
	 * This method has the game loop for the game
	 */
	@Override
	public void run() {

		JPanel ultimatePanel = new JPanel();
		try {
			drawPanel = new DrawPanel();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		drawPanel.setPreferredSize(new Dimension(mainWindow.getWidth(),550));
		//drawPanel.setBackground(Color.BLACK);
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(mainWindow.getWidth(), 200));
		JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		buttonPanel.setBackground(Color.red);
		ultimatePanel.add(drawPanel);
		//ultimatePanel.add(jsp);
		ultimatePanel.add(buttonPanel);
		mainWindow.add(ultimatePanel);
		System.out.println("There!");
		
		//get the word
		int d = (int) (Math.random()*wordList.size());
		currentWord = wordList.elementAt(d);
		currentWord = currentWord.toUpperCase();
		System.out.println("Current word is: " + currentWord);
		drawPanel.setWord(currentWord);//draw blanks for everything!
		char temp = 'A';
		for(int i = 0; i < 26; i++){
			ButtonLetter bl = new ButtonLetter(temp);
			bl.addActionListener(this);
			bl.setFont(new Font("Default", Font.BOLD, 15));
			buttonPanel.add(bl);
			temp ++;
		}
		mainWindow.revalidate();
	}

}
