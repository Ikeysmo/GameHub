package hangman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DictionaryHandler {

	public DictionaryHandler() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Done!");
	}

}
