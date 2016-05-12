package hangman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawPanel extends JPanel {
/*this is must provide a function that draws the latest image*/
	private BufferedImage[] hangmanposes;
	private String currWord = null;
	private char[] testWord = null;
	private int pose = 0;
	public DrawPanel() throws IOException {
		// TODO Auto-generated constructor stub
		setBackground(Color.white);
		hangmanposes = new BufferedImage[6];
		for(int i = 0; i < 6; i++){
			hangmanposes[i] = ImageIO.read(new File("hangman" + (i+1) + ".png"));
		}
		System.out.println("done");
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		//where I draw the things that I need
		g.drawImage(hangmanposes[pose], 150, 0, null );
		if(currWord == null)
			return;
		int posx = 50;
		for(int i = 0; i < currWord.length(); i++){
			g.drawLine(posx, 540, posx+50, 540);
			posx += 75; 
		}
		//else draw blank lines!
		posx = 50; //again
		for(int i = 0; i < testWord.length; i++){
			if(testWord[i] == ' '){
				posx += 75;
				continue;
			}
			g.setFont(new Font("Default", Font.BOLD, 30));
			g.drawString(String.valueOf(testWord[i]), posx + 25, 535);
			posx += 75;
		}
	}

	public void addLetter(char letter, int temp){
		/*add letter to respective slot */
		testWord[temp] = letter;
		repaint();
	}
	public int getNextState(){
		//displays the latest state
		if(pose < 6)
			pose++;
		repaint();
		return pose;
	}
	
	public void resetState(){
		pose = 0;
		repaint();
	}

	public void setWord(String currentWord) {
		currWord = currentWord;
		testWord = new char[currWord.length()];
		for(int i = 0; i < testWord.length; i++){
			testWord[i] = ' ';
		}
		repaint();
	}

	public void gameOver() {
		// TODO Auto-generated method stub
		
	}
}
