package hangman;

/**
 * This class is for the buttons that represents letters
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Font;

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
		setBackground(Color.black);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setFont(new Font("Tahoma", Font.BOLD, 12));
		
		this.letter = letter;
	}

}
