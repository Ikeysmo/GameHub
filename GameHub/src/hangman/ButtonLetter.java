package hangman;

/**
 * This class is for the buttons that represents letters
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import javax.swing.JButton;

public class ButtonLetter extends JButton{
	/* The letter for the button */
	public char letter;
	
	/**
	 * The constructor for ButtonLetter
	 * 
	 * @param letter
	 */
	public ButtonLetter(char letter) {
		super(String.valueOf(letter));
		System.out.println(letter);
		this.letter = letter;
	}

}
