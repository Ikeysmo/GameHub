package hangman;

import javax.swing.JButton;

public class ButtonLetter extends JButton{
	public char letter;
	public ButtonLetter(char letter) {
		super(String.valueOf(letter));
		System.out.println(letter);
		this.letter = letter;
	}

}
