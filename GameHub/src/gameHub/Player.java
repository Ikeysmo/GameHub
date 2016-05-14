package gameHub;

/**
 * The abstract class for the Player
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 */

import java.awt.Point;
import java.io.IOException;

public class Player {
	/* The name of the player */
	public String name;
	
	/**
	 * Constructor of the Player class
	 */
	public Player() {
	}
	
	/**
	 * Make move method for the players
	 * 
	 * @return The point where the move was made
	 * @throws IOException
	 */
	public Point makeMove() throws IOException {
		return null;
	}
	
	/**
	 * Shows the score of the player
	 */
	public void showScore() {
		
	}
	
	/**
	 * Getter method for the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter method for the name
	 * 
	 * @param name The name of the player
	 */
	public void setName(String name) {
		this.name = name;
	}

}
