package connectFour;

/**
 * The board for Connect4
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	/* Number of squares in a row*/
	public final static int ROWLENGTH = 10;
	/* Number of squares in a column*/
	public final static int COLUMNLENGTH = 10;
	/* Number of squares in a column*/
	/* The frame of the board */
	private JFrame frame;
	/* 2-D array for board */
	private char[][] board = new char[ROWLENGTH][COLUMNLENGTH];
	/* First Piece*/
	public final static char PIECE1 = 'X';
	/* Second Piece*/
	public final static char PIECE2 = 'O';
	
	/**
	 * Constructor of BoardPanel
	 * 
	 * @param p1 PLayer 1
	 * @param p2 PLayer 2
	 * @param currWindow the current window
	 */
	public BoardPanel(ConnectFourPlayer p1, ConnectFourPlayer p2, JFrame currWindow) {
		p1.setPanel(this);
		p2.setPanel(this);
		frame = currWindow;
	}
	
	public BoardPanel(ConnectFourPlayer p1, RemotePlayer p2, JFrame currWindow) {
		p1.setPanel(this);
		p2.setPanel(this);
		frame = currWindow;
	}
	
	public BoardPanel(RemotePlayer p1, RemotePlayer p2, JFrame currWindow) {
		p1.setPanel(this);
		p2.setPanel(this);
		frame = currWindow;
	}
	
	/**
	 * Paint method for board
	 * This allow the pieces and lines to be shown
	 * 
	 * @param g The graphics
	 */
	@Override
	public void paint(Graphics g) {
		getRootPane().setBackground(Color.black);
		drawBoard(g);
	}
	
	/**
	 * Setter method for the board array
	 * 
	 * @param board The 2-D board array
	 */ 
	public void updateBoard(char[][] board) {
		this.board = board;
	}
	
	/**
	 * Helper method to drawing the board
	 * 
	 * @param g The current graphics
	 */
	private void drawBoard(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		for(int i = 1; i < ROWLENGTH; i++) {
			g.drawLine((width/ROWLENGTH)*i, 0, (width/ROWLENGTH)*i, height);
		}
		
		//Draws the horizontal lines on the grid
		for(int i = 1; i < COLUMNLENGTH; i++) {
			g.drawLine(0, (height/COLUMNLENGTH)*i, width , (height/ROWLENGTH)*i);
		}
		
		//draw the pieces
		for(int i = 0; i < ROWLENGTH; i++){
			for(int j = 0; j < COLUMNLENGTH; j++){
				if(board[i][j] == PIECE1 || board[i][j] == PIECE2)
					if (board[i][j] == PIECE1)
						g.setColor(Color.blue);
				
					if (board[i][j] == PIECE2)
						g.setColor(Color.red);
					
					//TODO: Need to fix the offset on the ovals so
					//TODO: they will change with screen size
					int locationx = (width/20)*(2*i);
					int locationy = (height/20)*(2*j);
					
					//g.drawString(String.valueOf(board[i][j]), locationx, locationy);
					if( board[i][j] == PIECE1 || board[i][j] == PIECE2)
						g.fillOval(locationx + 10, locationy + 10, 30, 30); 
					
			}
		}
	}

}
