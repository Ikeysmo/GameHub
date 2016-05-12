package hangman;

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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Hangman implements ActionListener, Runnable{
	private JFrame mainWindow;
	private Vector<String> wordList = new Vector<String>();
	private DrawPanel drawPanel;
	private JPanel buttonPanel;
	private String currentWord;
	private int lifes = 0;
	public Hangman() throws IOException {
		FileReader fr = new FileReader("dictonary_english_hangman.txt");
		BufferedReader br = new BufferedReader(fr); //loading the words
		//load the GUI just to let person know it's there
		mainWindow = new JFrame("Hangman!");
		mainWindow.setSize(800, 800);
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

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new Hangman();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		ButtonLetter bl = (ButtonLetter) e.getSource();
		System.out.println(bl.letter);
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


	private void gameOver() {
		// TODO Auto-generated method stub
		JFrame endWindow = new JFrame("GameOver");
		JLabel correctWord = new JLabel("The correct word was: "+ currentWord + "!");
		correctWord.setFont(new Font("Default", Font.BOLD, 25));
		correctWord.setForeground(Color.blue);
		endWindow.getContentPane().add(correctWord);
		endWindow.setSize(500, 100);
		endWindow.setVisible(true);
		endWindow.setLocationRelativeTo(mainWindow);
	}

	@Override
	public void run() {

		JPanel ultimatePanel = new JPanel();
		try {
			drawPanel = new DrawPanel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			buttonPanel.add(bl);
			temp ++;
		}
		mainWindow.revalidate();
	}

}
