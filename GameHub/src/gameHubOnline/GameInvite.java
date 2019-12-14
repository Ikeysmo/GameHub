package gameHubOnline;

/**
 * This method is to send a game invite
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import java.io.Serializable;

public class GameInvite implements Serializable{
	private static final long serialVersionUID = 1L;
	/* Tic Tac Toe */
	public static final String tictactoe = "tictactoe";
	/* Connect Four */
	public static final String connect4 = "connect4";
	/* Snake */
	public static final String snake = "snake";
	/* Hangman */
	public static final String hangman = "hangman";
	/* Trivia Game */
	public static final String triviaGame = "triviaGame";
	/* Word Whomp */
	public static final String wordWhomp = "wordWhomp";
	/* Brick Breaker */
	public static final String brickBreaker = "brickBreaker";
	/* Pong */
	public static final String pong = "pong";
	/* The message from the other person */
	String from;
	/* The message to the other person */
	String to;
	/* The game that is being played */
	String game;
	/* Was the invite accepted */
	private boolean accepted;
	/* Was the invite checked */
	/* this goes true if it's been checked at any point! */
	private boolean checked = false;
	
	/**
	 * Was the invite accepted
	 */
	public void Accept(){
		accepted = true;
		checked = true;
	}
	
	/**
	 * Was the invite denied
	 */
	public void Deny(){
		accepted = false;
		checked = true;
	}
	
	/**
	 * If the invite was accepted or not
	 * 
	 * @return is accepted
	 */
	public boolean isAccepted(){
		return accepted;
	}
	
	/**
	 * If the invite was checked or not
	 * 
	 * @return is checked
	 */
	public boolean isChecked(){
		return checked;
	}
	
	/**
	 * The constructor of GameInvite
	 * 
	 * @param from Message from
	 * @param to Message to
	 * @param game Which game being played
	 */
	public GameInvite(String from, String to, String game) {
		this.from = from;
		this.to = to;
		this.game = game;
	}

}
