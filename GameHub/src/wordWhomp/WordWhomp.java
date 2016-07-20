package wordWhomp;

import games.Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.omg.PortableInterceptor.USER_EXCEPTION;

import player.Player;

public class WordWhomp extends Game implements Runnable{
	private JFrame mainWindow;
	
	public WordWhomp() throws IOException {
		super("Word Whomp", "wordWhomp.png", new JFrame(), new JPanel(), 500, 500, 200, 200, 2);
		
		
		FileReader fr = new FileReader("dictonary_english_hangman.txt");
		BufferedReader br = new BufferedReader(fr); //loading the words
		getGameFrame().setVisible(true);
	}
	
	public WordWhomp(String localplayer, String remoteplayer, boolean goFirst, String ipaddress, Boolean gofirst) throws UnknownHostException, IOException {
		super("Word Whomp", "wordWhomp.png", new JFrame(), new JPanel(), 500, 500, 200, 200, 2);
	}

	public static void main(String[] args) throws IOException {
		new WordWhomp();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateMove(int numx, int numy, Player p1) {
		// TODO Auto-generated method stub
		return false;
	}

}
