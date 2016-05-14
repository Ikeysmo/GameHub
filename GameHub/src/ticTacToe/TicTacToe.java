package ticTacToe;

/**
 * The heart of the TicTacToe game
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class TicTacToe implements Runnable{
	
	/* The number of players*/
	final public static int PLAYERNUM = 2;
	/* The number of columns*/
	final public static int COLUMNNUM = 10;
	/* The number of rows*/
	final public static int ROWNUM = 10;
	/* Four winning pieces */
	final public static int FOURPIECES = 4;
	/* Bottom of the column */
	final public static int BOTTOM_OF_COLUMN = 9;
	
	/* Piece One */
	final public static char PIECE1 = 'X';
	/* Piece Two */
	final public static char PIECE2 = 'O';
	/* Player 1 */ 
	final public static int PLAYER_1 = 0;
	/* Player 2 */
	final public static int PLAYER_2 = 1;
	
	/*Frame Height*/
	final public static int FRAMEHEIGHT = 160;
	/*Frame Width*/
	final public static int FRAMEWIDTH = 600;
	
	/*Min Frame Height*/
	final public static int MINFRAMEHEIGHT = 180;
	/*Min Frame Width*/
	final public static int MINFRAMEWIDTH = 500;
	
	/*Winning Screen Height */
	final public static int WINFRAMEHEIGHT = 100;
	/*Winning Screen Width */
	final public static int WINFRAMEWIDTH = 350;
	
	/* Main JFrame */
	JFrame gameMenu = null;
	/* Main Panel */
	JPanel mainPanel = new JPanel();
	/* Menu Bar Panel*/
	JPanel menubar = new JPanel();
	/* Error Message Label*/
	JLabel errorMsg = new JLabel();
	/* Second Panel*/
	JPanel secondPanel = new JPanel();
	//TODO: What does mudda mean?
	JFrame mudda;
	//TODO: What does foo mean?
	BoardPanel foo;
	/* Tells whose turn it is*/
	public int turn = 0;
	/*Players in the game*/
	private TicTacToePlayer[] players = new TicTacToePlayer[PLAYERNUM];
	/*The board being used*/
	private char[][] board = new char[ROWNUM][COLUMNNUM];
	/* Object Output stream*/
	private ObjectOutputStream oos;
	/* Object Input Stream*/
	private ObjectInputStream ois;

	/**
	 * The constructor of Tic Tac Toe
	 * 
	 * @param localplayer Is the local player
	 * @param remoteplayer Is the remote player
	 * @param goFirst Who goes first
	 */
	public TicTacToe(String localplayer, String remoteplayer, Boolean goFirst, String ipaddress) throws UnknownHostException, IOException {
		//player 1 represents this guy's version
		Socket s = new Socket(ipaddress, 2021);
		oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(localplayer);
		ois = new ObjectInputStream(s.getInputStream());
		//System.out.println("First p: "+localplayer + ", Second p: " + remoteplayer);
		if(!goFirst){
			players[PLAYER_2] = new TicTacToePlayer(localplayer); //you become player num 2
			players[PLAYER_2].getTic(this);
			players[PLAYER_2].assignPiece(PIECE1);
			players[PLAYER_1] = new RemotePlayer(oos, ois, this, remoteplayer, PIECE2); //this guys go first
		}else {
			players[PLAYER_1] = new TicTacToePlayer(localplayer);
			players[PLAYER_1].getTic(this);
			players[PLAYER_1].assignPiece(PIECE2);
			players[PLAYER_2] = new RemotePlayer(oos, ois, this, remoteplayer, PIECE1);
		}
		if(mudda != null)
			mudda.dispose();
		mudda = new JFrame(remoteplayer + " is Online!");
		mudda.setSize(500, 500);
		mudda.setVisible(true);
		System.out.println("Should be another checkpoint");		
		foo = new BoardPanel(players[PLAYER_1], players[PLAYER_2] , mudda);
		mudda.add(foo);
		mudda.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //what i ideally want is to have server end match when done
		new Thread(this).start();
	}

	/**
	 * Getter method for the board
	 * 
	 * @return the board
	 */
	public char[][] getBoard(){
		return board;	
	}
	
	/**
	 * Where the game is ran
	 */
	public void run() {
		board = new char[ROWNUM][COLUMNNUM];
		boolean gameOver = false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(!gameOver){
			mudda.setTitle("It is " + players[turn].getName() + "'s turn!");
			
			try {
				Point d = players[turn].makeMove(); //does different depending on what type of player
				
				if(players[turn] instanceof RemotePlayer){ 
				}
				else{
					RemotePlayer temp;
					if(turn == 0)
						temp = (RemotePlayer) players[PLAYER_2]; //this has to be instance of RP
					else
						temp = (RemotePlayer) players[PLAYER_1]; //this has to be RP
					
					temp.updateAll(d); //send it out to everyone
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if( isWinner() == PIECE1 || isWinner() == PIECE2) {
				gameOver = true; //let know if game is over
			}
			
			if(turn == 1)
				turn = 0;
			else turn = 1;
			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++) {
					System.out.println(String.valueOf(board[i][j]));
				}
				System.out.println();
			System.out.println("turn is now: " + turn);
		}
		findWinner();
	}
	
	private void findWinner() {
		int playerWon = -1;
		for(int i = 0; i < PLAYERNUM; i++){
			if(players[PLAYER_1].getPiece() == isWinner()) {
				playerWon = PLAYER_1;
			} else if (players[PLAYER_2].getPiece() == isWinner()) {
				playerWon = PLAYER_2;
			}
		}
		JFrame finished = new JFrame("Game!");
		finished.setSize(350, 100);
		JLabel label = new JLabel();
		label.setFont(new Font("Default", Font.BOLD, 20));
		label.setForeground(Color.red);
		if(playerWon == -1)
			label.setText("It is a draw!");
		else
			label.setText(" And winner is " + players[playerWon].getName() + "!");
		finished.add(label, "Center");
		finished.setVisible(true);
	
		
	}


	/**
	 * Helper method to figure out if there is a winner
	 * 
	 * @return 'X', 'O', ''
	 */
	private char isWinner() {
		//vertical
		char piece = PIECE1;
		for(int i = 0; i < 3; i++){
			if(board[i][0] == piece &&board[i][1] == piece && board[i][2] == piece)
				return PIECE1;
		}
		
		//horizontal
		for(int i = 0; i < 3; i++){
			if(board[0][i] == piece &&board[1][i] == piece && board[2][i] == piece)
				return PIECE1;
		}
		
		//diagonals
		if(board[0][0] == piece && board[1][1] == piece && board[2][2] == piece)
			return PIECE1;
		if(board[0][2] == piece && board[1][1] == piece && board[2][0] == piece)
			return PIECE1;
		
		
		//now try for '0'
		piece = PIECE2;
		for(int i = 0; i < 3; i++){
			if(board[i][0] == piece &&board[i][1] == piece && board[i][2] == piece)
				return PIECE2;
		}
		//horizontal
		for(int i = 0; i < 3; i++){
			if(board[0][i] == piece &&board[1][i] == piece && board[2][i] == piece)
				return PIECE2;
		}
		//diagonals
		if(board[0][0] == piece && board[1][1] == piece && board[2][2] == piece)
			return PIECE2;
		if(board[0][2] == piece && board[1][1] == piece && board[2][0] == piece)
			return PIECE2;
		//else...
		return ' ';
	}
	
	/**
	 * This method updates everything after the moves
	 * 
	 * @param numx x-cord of move
	 * @param numy y-cord of move
	 * @param p1 The player
	 * @return Was the move possible
	 */
	public boolean updateMove(int numx, int numy, TicTacToePlayer p1){
		if(board[numx][numy] == PIECE1 || board[numx][numy] == PIECE2){
				return false;
		} else{ //Then do the move
			System.out.println("Updating board at " + (numx+1) + "," + (numy+1));
			System.out.println("This is the piece " + p1.getPiece());
			board[numx][numy] = p1.getPiece();
			foo.updateBoard(board);
			foo.repaint();
			//printBoard(); //For debuging purposes
			return true;
		}
			
		
	}
	/**
	 * This helper method is for printing out the board to the console
	 * 
	 */
	private void printBoard() {
		for(int j = 0; j < COLUMNNUM; j++) {
			for(int i = 0; i < ROWNUM; i++)
				System.out.print(String.valueOf(board[i][j]));
			System.out.println("");
		}
	}
}
