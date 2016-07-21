package hangman;

/**
 * This is the Draw Panel for hangman
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

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
	/*Background image*/
	private Image backgroundImage;
	/* All of the poses of the Hang Man */
	private BufferedImage[] hangmanposes;
	/* The current word being done */
	private String currWord = null;
	/* The test word being done */
	private char[] testWord = null;
	/* The state of the poses right now */
	private int pose = 0;
	/* The number of poses in the hangman */
	public final static int NUM_OF_POSES = 6;
	
	/**
	 * The constructor of the Draw Panel
	 * 
	 * @throws IOException
	 */
	public DrawPanel() throws IOException {
		//backgroundImage = ImageIO.read(new File("hangman_background.png"));
		//setBackground(Color.white);
		hangmanposes = new BufferedImage[NUM_OF_POSES];
		for(int i = 0; i < NUM_OF_POSES; i++){
			hangmanposes[i] = ImageIO.read(new File("hangman" + (i+1) + ".png"));
		}
	}
	
	/**
	 * The paint method for the Draw Panel
	 * 
	 * @param g The Graphics
	 */
	@Override
	public void paint(Graphics g){
		super.paint(g);
		//int offset = this.getBorder().getBorderInsets(getRootPane()).top;
		//g.drawImage(backgroundImage,  offset,  offset,  this);
		//where I draw the things that I need
			g.drawImage(hangmanposes[pose], 0 , 0, null );
			g.setColor(Color.orange);
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

	/**
	 * This method is for adding a letter to the respective slot
	 */
	public void addLetter(char letter, int temp){
		testWord[temp] = letter;
		repaint();
	}
	
	/**
	 * Method to get the next state of the Hang Man
	 * 
	 * @return The new pose
	 */
	public int getNextState(){
		//displays the latest state
		if(pose < NUM_OF_POSES)
			pose++;
		repaint();
		return pose;
	}
	
	/**
	 * Method to reset the state of the Hang Man to the
	 * first one
	 */
	public void resetState(){
		pose = 0;
		repaint();
	}

	/**
	 * Set the new word to test word
	 * 
	 * @param currentWord
	 */
	public void setWord(String currentWord) {
		currWord = currentWord;
		testWord = new char[currWord.length()];
		for(int i = 0; i < testWord.length; i++){
			testWord[i] = ' ';
		}
		repaint();
	}

	/**
	 * The game over method
	 * 
	 * Not sure what it does...
	 */
	public boolean gameOver() {
		if (String.valueOf(testWord).equals(currWord)) {
			return true;
		} else {
			return false;
		}
	}
}
