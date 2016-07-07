package gameHub;

/**
 * This class is for the Player Account
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import java.net.ConnectException;

import player.Player;

public class PlayerAccount {
	/* The username of the player*/
	String username;
	/* The password of the player*/
	String password;
	/* Hold reference to all the different type of players for instance games.*/
	Player temp;
	
	/**
	 * Constructor of Player Account
	 * 
	 * @param username The username of the player
	 * @param password The password of the player
	 */
	public PlayerAccount(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * This method trys to get online
	 * Throw a connection if fails... then sign offline!
	 * 
	 * @throws ConnectException
	 */
	public void getOnline() throws ConnectException{
	}
	
	/**
	 * Getter method for the score
	 * 
	 * @return score
	 */
	public int getScore(){
		return 0;
		
	}
	
	/**
	 * Getter method for the username
	 * 
	 * @return the username
	 */
	public String getUsername(){
		return username;
	}
}
