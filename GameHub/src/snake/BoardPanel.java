package snake;

/**
 * The BoardPanel class is responsible for building the board
 * that will be used by Snake game in Gamehub. It draws
 * the boards in a JFrame.
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 * @version 1.0
 */
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	/* Number of squares in a row*/
	public final static int ROWLENGTH = 10;
	/* Number of squares in a column*/
	public final static int COLUMNLENGTH = 10;
	/* representative of Player vs. PLayer */
	public final static int PLAYERVPLAYER = 0;
	/* representative of PLayer vs. CPU */
	public final static int PLAYERVCPU = 1;
	/* representative of Remote PLayers */
	public final static int PLAYERREMOTE = 2;
	/* Did game over occur */
	private boolean gameOver = false;
	/* The frame of the board */
	private JFrame frame;
	/* 2-D array for board */
	private char[][] board = new char[ROWLENGTH][COLUMNLENGTH];
	/* Tell whose turn it is */
	private int turn = 0; //alternate between 0 and 1
	/* First Piece*/
	public final static char X = 'X';
	/* Second Piece*/
	public final static char O = 'O';
	
	/**
	 * Constructor of BoardPanel
	 * 
	 * @param p1 PLayer 1
	 * @param p2 PLayer 2
	 * @param currWindow the current window
	 */
	public BoardPanel(SnakePlayer p1, SnakePlayer p2, JFrame currWindow) {
		p1.getPanel(this);
		p2.getPanel(this);
		
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
		getRootPane().setBackground(Color.white);
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
		//Get the width of the frame
		int width = getWidth();
		//Get the height of the frame
		int height = getHeight();
		
		//Draws the vertical lines on the grid
		for(int i = 1; i < ROWLENGTH; i++) {
			g.drawLine((width/ROWLENGTH)*i, 0, (width/ROWLENGTH*COLUMNLENGTH)*i, height);
		}
		
		//Draws the horizontal lines on the grid
		for(int i = 1; i < COLUMNLENGTH; i++) {
			g.drawLine(0, (height/COLUMNLENGTH)*i, width , (height/ROWLENGTH*COLUMNLENGTH)*i);
		}
		
		//draw the pieces
		for(int i = 0; i < ROWLENGTH*COLUMNLENGTH; i++){
			for(int j = 0; j < ROWLENGTH*COLUMNLENGTH; j++){
				//If that spot has a piece in it
				if (board[i][j] == X)
					g.setColor(Color.blue);
				
				if (board[i][j] == O)
					g.setColor(Color.red);
					
				//TODO: Need to fix the offset on the ovals so
				//TODO: they will change with screen size
				int locationx = (width/X)*(2*i);
				int locationy = (height/20)*(2*j);
					
				//g.drawString(String.valueOf(board[i][j]), locationx, locationy);
				if( board[i][j] == X || board[i][j] == O)
					g.fillOval(locationx + ROWLENGTH, locationy + COLUMNLENGTH, ROWLENGTH*COLUMNLENGTH, ROWLENGTH*COLUMNLENGTH); 
					
			}
		}
	}

}
