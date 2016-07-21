package games.connectFour;

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
	/* The ConnectFour game*/
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
		super();
	}
	
	/**
	 * Constructor of ConnectFourPlayer
	 * 
	 * @param name The name of the player
	 */
	public ConnectFourPlayer(String name){
		super();
		this.name = name;
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
			if(connectFour.validMove(numx, numy, this)) // if this is true, valid move!
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
	 * Getter method for the piece
	 * 
	 * @return the piece of the player
	 */
	public char getPiece(){
		return this.piece;
	}
	
	/**
	 * Getter method for the board
	 * 
	 * @return the board
	 */
	public void setPanel(BoardPanel panel){
		this.panel = panel;
	}
	
	/**
	 * Getter method for the ConnectFour game
	 * 
	 * @return the ConnectFour game
	 */
	public void setGame(ConnectFour connectFour){
		this.connectFour = connectFour;
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
