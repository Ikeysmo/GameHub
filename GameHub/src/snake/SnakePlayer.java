package snake;

/**
 * The player for the Snake game
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 * @version 1.0
 */

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import player.Player;

public class SnakePlayer extends Player implements MouseListener {
	/* Tell if the mouse is not clicked*/
	public boolean mouseNotClicked = false;
	/* Are you ready or not*/
	public boolean ready = false;
	/* The Panel for the board*/
	private BoardPanel panel = null;
	/* The snake game itself */
	protected Snake snake = null;
	/* x cord of the piece */
	protected int numx = -1;
	/* y cord of the piece*/
	protected int numy = -1;
	/* The piece the player is using */
	protected char piece;
	
	/* Number of columns */
	final public static int COLUMNSNUM = 10;
	/* Number of rows */
	final public static int ROWSNUM = 10;
	
	/*Piece 1 */
	final public static char X = 'X';
	/*Piece 2 */
	final public static char O = 'O';
	
	
	/**
	 * The constructor of the SnakePlayer
	 */
	public SnakePlayer() {
		
	}
	
	/**
	 * Method to make a move
	 * 
	 * @return the point where the move was made
	 */
	public synchronized Point makeMove() throws IOException {
		
		while(true) {
			try {
				panel.addMouseListener(this);
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(snake.updateMove(numx, numy, this)) // if this is true, valid move!
				break;
		}
		return new Point(numx, numy);
	}
	
	/**
	 * When the mouse is pressed, this method activates
	 * 
	 * @param e The event that triggered the method
	 */
	@Override
	public synchronized void mousePressed(MouseEvent e) {
		
		int widthFactor = panel.getWidth()/COLUMNSNUM;
		int heightFactor = panel.getHeight()/ROWSNUM;
		numx = e.getX()/widthFactor;
		numy = e.getY()/heightFactor;
		
		ready = false;
		notify();
		panel.removeMouseListener(this);
	}
	
	/**
	 * When the mouse is clicked, this method activates
	 * 
	 * @param e The event that triggered the method
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
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
	 * This method assigns a piece to a place on the board
	 * 
	 * @param x The piece
	 */
	public void assignPiece(char x) {
		if(x == X || x == O)
			this.piece = x;
		else
			throw new IllegalArgumentException();
	}
	
	/**
	 * Getter method for piece
	 * 
	 * @return piece
	 */
	public char getPiece() {
		return this.piece;
	}
	
	/**
	 * Constructor of SnakePlayer
	 * 
	 * @param name Is the name of the player
	 */
	public SnakePlayer(String name) {
		this.name = name;
	}
	
	/**
	 * Setter method for the panel
	 * 
	 * @param panel The BoardPanel
	 */
	 //TODO: The name does not match what the method is doing
	public void getPanel(BoardPanel panel) {
		this.panel = panel;
	}
	
	/**
	 * Setter method for the snake
	 * 
	 * @param snake The Snake
	 */
	 //TODO: The name does not match what the method is doing
	public void getSnake(Snake snake) {
		this.snake = snake;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
