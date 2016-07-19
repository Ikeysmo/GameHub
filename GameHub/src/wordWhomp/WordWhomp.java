package wordWhomp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;

import org.omg.PortableInterceptor.USER_EXCEPTION;

public class WordWhomp implements Runnable{
	private JFrame mainWindow;
	
	public WordWhomp() throws IOException {
		
		FileReader fr = new FileReader("dictonary_english_hangman.txt");
		BufferedReader br = new BufferedReader(fr); //loading the words
		//load the GUI just to let person know it's there
		mainWindow = new JFrame("Word Whomp!");
		mainWindow.setSize(800, 800);
		mainWindow.setResizable(false);
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws IOException {
		File whereAmI = new File(System.getProperty("user.dir"));
		System.out.println(whereAmI);
		new WordWhomp();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
