package games.hangman;

/**
 * This class handles the dictionary of words that can
 * be used in Hangman
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DictionaryHandler {

	/**
	 * Constructor of Dictionary Handler
	 */
	public DictionaryHandler() {
	}

	/**
	 * The main method of Dictionary Handler
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String newline = System.lineSeparator();
		FileReader fr = new FileReader("dictonary_english_special.txt");
		BufferedReader br = new BufferedReader(fr);
		String holder = "";
		String temp;
		while(true){
			try {
				temp = br.readLine();
				if(temp == null)
					break;
				if(temp.length() < 4)
					continue;
				//System.out.println(temp);
				holder += temp + newline;
			} catch (IOException e) {break;}
		}
			FileWriter fw;
			try {
				fw = new FileWriter("dictonary_english_hangman.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Done!");
	}

}
