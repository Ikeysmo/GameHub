package connectFour;

/**
 * The player for Connect Four
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

public class ConnectFourPlayer extends Player implements MouseListener {
	/* Is the mouse clicked? */
	public boolean mouseNotClicked = false;
	/* Is it ready?*/
	public boolean ready = false;
	/* The board being played on*/
	private BoardPanel panel = null;
	/* The TicTacToe game*/
	protected ConnectFour connectFour = null;
	/* X-cord of the move */
	protected int numx = -1;
	/* Y-cord of the move */
	protected int numy = -1;
	/* Piece of this player*/
	protected char piece;
	/* Number of Columns */
	public final static int COLUMNNUM = 10;
	/* Number of Rows */
	public final static int ROWNUM = 10;
	
	public ConnectFourPlayer() {
		
	}
	
	/**
	 * Makes the move for the player
	 * 
	 * @return The point of the move made
	 */
	public synchronized Point makeMove() throws IOException {
		
		while(true) {
			try {
				panel.addMouseListener(this);
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(connectFour.updateMove(numx, numy, this)) // if this is true, valid move!
				break;
		}
		return new Point(numx, numy);
	}
		//wait for event...
	
	/**
	 * Method for if a mouse is pressed
	 * 
	 * @param e The mouse being pressed
	 */
	@Override
	public synchronized void mousePressed(MouseEvent e) {
		
		int widthFactor = panel.getWidth()/COLUMNNUM;
		int heightFactor = panel.getHeight()/ROWNUM;
		numx = e.getX()/widthFactor;
		numy = e.getY()/heightFactor;
		
		ready = false;
		notify();
		panel.removeMouseListener(this);
	}
	
	/**
	 * Getter method for the name of the player
	 * 
	 * @return the name of the player
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	/**
	 * Getter method for the name of the player
	 * 
	 * @return the name of the player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Assigns a piece to the player (Setter)
	 * 
	 * @param 'X' or 'O'
	 */
	public void assignPiece(char x) {
		if(x == 'X' || x == 'O')
			this.piece = x;
		else
			throw new IllegalArgumentException();
	}
	
	/**
	 * Getter method for the piece
	 * 
	 * @return the piece of the player
	 */
	public char getPiece() {
		return this.piece;
	}
	
	/**
	 * Getter method for the name of the player
	 * 
	 * @param name The name of the player
	 */
	public ConnectFourPlayer(String name) {
		this.name = name;
	}
	
	/**
	 * Getter method for the board
	 * 
	 * @return the board
	 */
	public void getPanel(BoardPanel panel) {
		this.panel = panel;
	}
	
	/**
	 * Getter method for the Connect Four game
	 * 
	 * @return the Connect Four game
	 */
	public void getConnectFour(ConnectFour connectFour) {
		this.connectFour = connectFour;
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
