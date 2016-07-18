package player;

/**
 * The abstract class for the Player
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 */

import games.Game;

import java.awt.Point;
import java.io.IOException;

import ticTacToe.BoardPanel;

public abstract class Player {
	/* The name of the player */
	public String name;
	/* Current Game */
	public Game game;
	/* high scores */
	public HighScores highScores;
	/* Friends */
	public String[] friends;
	/*Trophies */
	public Trophies trophies;
	/* Awards */
	public Awards awards;
	/* Data */
	public Data data;
	
	/**
 	 * Constructor of the Player class
	 */
	public Player() {
	}
	
	public Player(String name) {
		this.name = name;
	}
	
	public Game getCurrentGame() {
		return game;
	}
	
	public void setCurrentGame(Game game) {
		this.game = game;
	}
	
	/**
	 * Make move method for the players
	 * 
	 * @return The point where the move was made
	 * @throws IOException
	 */
	public abstract Point makeMove(BoardPanel boardPanel) throws IOException;
	
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
	
	public Awards getAwards() {
		return awards;
	}
	
	public Data getData() {
		return data;
	}
	
	public Trophies getTrophies() {
		return trophies;
	}
	
	public HighScores getHighScores() {
		return highScores;
	}
	
	public void setAwards(Awards awards) {
		this.awards = awards;
	}
	
	public void setData(Data data) {
		this.data = data;
	}
	
	public void setTrophies(Trophies trophies) {
		this.trophies = trophies;
	}
	
	public void setHighScores(HighScores highScores) {
		this.highScores = highScores;
	}
	
	public void showAwards() {
		awards.toString();
	}
	
	public void showData() {
		data.toString();
	}
	
	public void showTrophies() {
		trophies.toString();
	}
	
	public void showHighScores() {
		highScores.toString();
	}
	
	

}
