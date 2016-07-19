package ticTacToe;

/**
 * The heart of the TicTacToe game
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 * @version 1.0
 */


import games.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Player;

public class TicTacToe extends Game implements Runnable{
	
	/* The number of players*/
	final public static int PLAYERNUM = 2;
	/* The number of columns*/
	final public static int COLUMNNUM = 3;
	/* The number of rows*/
	final public static int ROWNUM = 3;
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
	
	/* Tells whose turn it is*/
	public int turn = 0;
	/*The board being used*/
	private char[][] board = new char[ROWNUM][COLUMNNUM];
	/* Object Output stream*/
	private ObjectOutputStream oos;
	/* Object Input Stream*/
	private ObjectInputStream ois;
	/* Is it remote play?*/
	boolean isRemote = false;
	/*Goes first? */
	boolean goFirst;
	
	public static void main(String args[]) {
		new TicTacToe();
	}
	
	public TicTacToe() {
		super("Tic Tac Toe", "ticTacToeIcon.gif", new JFrame(), new JPanel(), 700, 700, 500, 500, 2);
		TicTacToePlayer p1 = new TicTacToePlayer("Zachary");
		p1.setGame(this);
		p1.assignPiece(PIECE1);
		setPlayer(PLAYER_2, (Player)p1);
		TicTacToePlayer p2 = new TicTacToePlayer("Isaiah");
		p2.setGame(this);
		p2.assignPiece(PIECE2);
		setPlayer(PLAYER_1, (Player)p2);
		
		setGamePanel(new BoardPanel(p1, p2 , getGameFrame()));
		
		this.getGameFrame().setVisible(true);
		new Thread(this).start();
	}

	/**
	 * The constructor of Tic Tac Toe
	 * 
	 * @param localplayer Is the local player
	 * @param remoteplayer Is the remote player
	 * @param goFirst Who goes first
	 */
	public TicTacToe(String localplayer, String remoteplayer, boolean goFirst, String ipaddress, Boolean gofirst) throws UnknownHostException, IOException {
		super("Tic Tac Toe", "ticTacToeIcon.gif", new JFrame(), new JPanel(), 700, 700, 500, 500, 2);
		this.goFirst = gofirst;
		System.out.println("here we go");
		//player 1 represents this guy's version
		Socket s = new Socket(ipaddress, 2021);
		oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(localplayer);
		ois = new ObjectInputStream(s.getInputStream());
		if(!goFirst) {
			//setTurn(0);
			TicTacToePlayer p2 = new TicTacToePlayer(localplayer);
			p2.setGame(this);
			p2.assignPiece(PIECE1);
			setPlayer(PLAYER_2, (Player)p2);
			TicTacToePlayer p1 = new RemotePlayer(oos, ois, this, remoteplayer, PIECE2); //This guy goes first
			p1.setGame(this);
			p1.assignPiece(PIECE2);
			setPlayer(PLAYER_1, (Player)p1);
			
			setGamePanel(new BoardPanel((TicTacToePlayer)this.getPlayer(PLAYER_1),(TicTacToePlayer)this.getPlayer(PLAYER_2),getGameFrame()));
			
			this.getGameFrame().setVisible(true);
			new Thread(this).start();
		} else {
			//setTurn(1);
			TicTacToePlayer p1 = new TicTacToePlayer(localplayer);
			p1.setGame(this);
			p1.assignPiece(PIECE2);
			setPlayer(PLAYER_1, (Player)p1);
			TicTacToePlayer p2 = new RemotePlayer(oos, ois, this, remoteplayer, PIECE1); //This guy goes first
			p2.setGame(this);
			p2.assignPiece(PIECE1);
			setPlayer(PLAYER_2, (Player)p2);
			
			setGamePanel(new BoardPanel((TicTacToePlayer)this.getPlayer(PLAYER_1), (TicTacToePlayer)this.getPlayer(PLAYER_2) ,getGameFrame()));
			
			this.getGameFrame().setVisible(true);
			this.goFirst = goFirst;
			new Thread(this).start();
		}
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
		this.getGameFrame().setTitle("It is " + (this.getPlayer(turn)).getName() + "'s turn!");
		while(!gameOver){
			
			try {
				
				Point d = this.getPlayer(turn).makeMove();
				
					if(this.getPlayer(turn) instanceof RemotePlayer){
					}
					else{
						RemotePlayer temp;
						if(turn == 0)
							temp = (RemotePlayer) this.getPlayer(PLAYER_2); //this has to be instance of RP
						else
							temp = (RemotePlayer) this.getPlayer(PLAYER_1); //this has to be RP
						
						temp.updateAll(d); //send it out to everyone
					}
					
			} catch (IOException e) {
				e.printStackTrace();
			}
			if( isWinner() == PIECE1 || isWinner() == PIECE2) {
				gameOver = true; //let know if game is over
			}
			
			int numOfPieces = 0;
			
			if(turn == 1)
				turn = 0;
			else turn = 1;
			for(int i = 0; i < ROWNUM; i++) {
				for(int j = 0; j < COLUMNNUM; j++) {
					if (board[i][j] == 'O' || board[i][j] == 'X') {
						numOfPieces++;
					}
				}
			}
				if(numOfPieces == 9) {
					gameOver = true;
				}
		}
		findWinner();
	}
	
	private void findWinner() {
		int playerWon = -1;
		for(int i = 0; i < PLAYERNUM; i++){
			if(this.getPlayer(PLAYER_1) instanceof TicTacToePlayer){
				if(this.getPlayer(PLAYER_2) instanceof TicTacToePlayer){
					if(((TicTacToePlayer)getPlayer(PLAYER_1)).getPiece() == isWinner()) {
						playerWon = PLAYER_1;
					} else if (((TicTacToePlayer)getPlayer(PLAYER_2)).getPiece() == isWinner()) {
						playerWon = PLAYER_2;
					}
				} else if(this.getPlayer(PLAYER_2) instanceof RemotePlayer) {
					if(((TicTacToePlayer)getPlayer(PLAYER_1)).getPiece() == isWinner()) {
						playerWon = PLAYER_1;
					} else if (((RemotePlayer)getPlayer(PLAYER_2)).getPiece() == isWinner()) {
						playerWon = PLAYER_2;
					}
				}
			} else if(this.getPlayer(PLAYER_1) instanceof RemotePlayer) {
				if(this.getPlayer(PLAYER_2) instanceof TicTacToePlayer){
					if(((RemotePlayer)getPlayer(PLAYER_1)).getPiece() == isWinner()) {
						playerWon = PLAYER_1;
					} else if (((TicTacToePlayer)getPlayer(PLAYER_2)).getPiece() == isWinner()) {
						playerWon = PLAYER_2;
					}
				}
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
			label.setText(" And winner is " + (this.getPlayer(playerWon)).getName() + "!"); //TODO Fix this
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
	public boolean updateMove(int numx, int numy, Player p){
		if(board[numx][numy] == PIECE1 || board[numx][numy] == PIECE2){
				return false;
		} else{ //Then do the move
			if(p instanceof RemotePlayer) {
				board[numx][numy] = ((RemotePlayer)p).getPiece();
			} else {
				System.out.println("NEVER!!!!!!!!!");
				board[numx][numy] = ((TicTacToePlayer)p).getPiece();
			}
			((BoardPanel)getGamePanel()).updateBoard(board);
			getGameFrame().repaint();
			return true;
		}
			
		
	}
}
