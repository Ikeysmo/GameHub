package ticTacToe;

/**
 * This is the CPU Class for TicTacToe
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 * @version 1.0
 */

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class CPUPlayer extends TicTacToePlayer {
	/* Hard CPU */
	public static final int EASY = 0;
	/* Easy CPU */
	public static final int HARD = 1;
	/* The game being played */
	private TicTacToe tic;
	/* Easy or Hard CPU*/
	private int mode;
	
	/*The number of rows*/
	public static final int ROWSLENGTH = 3;
	/*The number of columns*/
	public static final int COLUMNSLENGTH = 3;
	/*Piece 1*/
	public static final char PIECE1 = 'X';
	/*Piece 2*/
	public static final char PIECE2 = 'O';
	
	/**
	 * Constructor of the CPUPLayer
	 * 
	 * @param mode Easy or Hard CPU
	 * @param tic The game being played
	 */
	public CPUPlayer(int mode, TicTacToe tic) {
		name = "AI";
		this.mode = mode;
		this.tic = tic;
		tic.getBoard();
	}
	
	/**
	 * This method makes the move for the CPU
	 * 
	 * @return The point where the move is made
	 */
	@Override
	public Point makeMove() {
		int index = 0;
		Vector <Point> moves = null;
		char[][] board = tic.getBoard();
		moves = new Vector<Point>();
		
		for(int i = 0; i < ROWSLENGTH; i++){
			for(int j = 0; j < COLUMNSLENGTH; j++){
				if(board[i][j] != PIECE1 && board[i][j] != PIECE2)
					moves.add(new Point(i,j));
			}
		}
		//For Easy, just make a random move
		if(mode == EASY){
			index = (int) (Math.random()*moves.size());
			System.out.println("Index is:" + index);
			tic.updateMove(moves.elementAt(index).x, moves.elementAt(index).y, this);
		}
		//For Hard, make guarentee that the person loses
		else if(mode == HARD){
			//TODO: Does this code work?
			//all the code for harder mode!
			//prevent opponent from winning!
			int count = 0;
			Point po = null;
			
			for(int j = 0; j < COLUMNSLENGTH; j++) {
				for(int i = 0; i < ROWSLENGTH; i++){
					if(board[i][j] == piece)
						count++;
					if(board[i][j] != PIECE1 && board[i][j] != PIECE2)
						po = new Point(i,j);
				}
				if(!(count > 1))
					po = null;
				count = 0;
			}
		}
		return new Point(moves.elementAt(index).x, moves.elementAt(index).y);
	
	}
}


