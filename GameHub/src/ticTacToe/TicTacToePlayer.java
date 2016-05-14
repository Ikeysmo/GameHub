package ticTacToe;

/**
 * The player of the TicTacToe game
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 */

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import gameHub.Player;
import ticTacToe.BoardPanel;
import ticTacToe.TicTacToe;

public class TicTacToePlayer extends Player implements MouseListener{
	/* Is the mouse clicked? */
	public boolean mouseNotClicked = false;
	/* Is it ready?*/
	public boolean ready = false;
	/* The board being played on*/
	private BoardPanel panel = null;
	/* The TicTacToe game*/
	protected TicTacToe tic = null;
	/* X-cord of the move */
	protected int numx = -1;
	/* Y-cord of the move */
	protected int numy = -1;
	/* Piece of this player*/
	protected char piece;
	/* Name of the player*/
	protected String name;
	
	public TicTacToePlayer() {
		
	}
	
	/**
	 * Constructor of TicTacToePlayer
	 * 
	 * @param name The name of the player
	 */
	public TicTacToePlayer(String name){
		this.name = name;
	}
	
	/**
	 * Makes the move for the player
	 * 
	 * @return The point of the move made
	 */
	public synchronized Point makeMove() throws IOException {
		while(true){
			try {
				panel.addMouseListener(this);
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(tic.updateMove(numx, numy, this)) //if this is true, valid move!
				break;
		}
		return new Point(numx,numy);
	}
	
	/**
	 * Method for if a mouse is pressed
	 * 
	 * @param e The mouse being pressed
	 */
	@Override
	public synchronized void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		int widthFactor = panel.getWidth()/COLUMNSNUM;
		int heightFactor = panel.getHeight()/ROWSNUM;
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
	public String getName(){
		return name;
	}
	
	/**
	 * Assigns a piece to the player (Setter)
	 * 
	 * @param 'X' or 'O'
	 */
	public void assignPiece(char x){
		if(x == 'X' || x == 'O')
			this.piece = x; //assign piece if this piece is legal!
		else
			throw new IllegalArgumentException();
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
	public void getPanel(BoardPanel panel){
		this.panel = panel;
	}
	
	/**
	 * Getter method for the TicTacToe game
	 * 
	 * @return the TicTacToe game
	 */
	public void getTic(TicTacToe tic){
		this.tic = tic;
	}

}
