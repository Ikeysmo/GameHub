package hangman;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JFrame;

public class Hangman {
	private JFrame mainWindow;

	public Hangman() throws FileNotFoundException {
		FileReader fr = new FileReader("dictonary_english_hangman.txt");
		BufferedReader br = new BufferedReader(fr); //loading the words
		//load the GUI just to let person know it's there
		mainWindow = new JFrame("Hangman!");
		mainWindow.setSize(600, 600);
		mainWindow.setVisible(true);
	}

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		new Hangman();
	}

}
