package connectFour;

/**
 * This is the logic for the Connect Four game
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
import wordWhomp.WordWhomp;

public class ConnectFour extends Game implements Runnable {
	
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
	
	/*Tells you whose turn it is*/
	public int turn = 0;
	/*The board being used */
	private char[][] board = new char[ROWNUM][COLUMNNUM];
	/* Object Output Stream */
	private ObjectOutputStream oos;
	/* Object Input Stream */
	private ObjectInputStream ois;
	/* Is it remote play?*/
	boolean isRemote = false;
	/*Goes first? */
	boolean goFirst;
	/* Local */
	private boolean isLocal;
	
	public static void main(String args[]) {
		new ConnectFour();
	}
	
	/**
	 * The constructor of Connect Four
	 */
	public ConnectFour() {
		super("Connect-Four", "connectFour.png", new JFrame(), new JPanel(), 500, 500, 200, 200, 2);
		isLocal = true;
		ConnectFourPlayer p1 = new ConnectFourPlayer("Zachary");
		p1.setGame(this);
		p1.assignPiece(PIECE1);
		setPlayer(PLAYER_2, (Player)p1);
		ConnectFourPlayer p2 = new ConnectFourPlayer("Isaiah");
		p2.setGame(this);
		p2.assignPiece(PIECE2);
		setPlayer(PLAYER_1, (Player)p2);
		
		setGamePanel(new BoardPanel(p1, p2 , getGameFrame()));
		
		this.getGameFrame().setVisible(true);
		new Thread(this).start();
	}
	
	/**
	 * Second constructor of Connect Four
	 * 
	 * @param localPlayer The local player
	 * @param remotePlayer The remote player
	 * @param goFirst Who goes first
	 */
	public ConnectFour(String localplayer, String remoteplayer, boolean goFirst, String ipaddress, Boolean gofirst) throws UnknownHostException, IOException {
		super("Connect-Four", "connectFour.png", new JFrame(), new JPanel(), 160, 600, 180, 500, 2);
		isLocal = false;
		this.goFirst = gofirst;
		System.out.println("here we go");
		//player 1 represents this guy's version
		Socket s = new Socket(ipaddress, 2021);
		oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(localplayer);
		ois = new ObjectInputStream(s.getInputStream());
		if(!goFirst) {
			ConnectFourPlayer p2 = new ConnectFourPlayer(localplayer);
			p2.setGame(this);
			p2.assignPiece(PIECE1);
			setPlayer(PLAYER_2, (Player)p2);
			ConnectFourPlayer p1 = new RemotePlayer(oos, ois, this, remoteplayer, PIECE2); //This guy goes first
			p1.setGame(this);
			p1.assignPiece(PIECE2);
			setPlayer(PLAYER_1, (Player)p1);
			
			setGamePanel(new BoardPanel((ConnectFourPlayer)this.getPlayer(PLAYER_1),(ConnectFourPlayer)this.getPlayer(PLAYER_2),getGameFrame()));
			
			this.getGameFrame().setVisible(true);
			new Thread(this).start();
		} else {
			ConnectFourPlayer p1 = new ConnectFourPlayer(localplayer);
			p1.setGame(this);
			p1.assignPiece(PIECE2);
			setPlayer(PLAYER_1, (Player)p1);
			ConnectFourPlayer p2 = new RemotePlayer(oos, ois, this, remoteplayer, PIECE1); //This guy goes first
			p2.setGame(this);
			p2.assignPiece(PIECE1);
			setPlayer(PLAYER_2, (Player)p2);
			
			setGamePanel(new BoardPanel((ConnectFourPlayer)this.getPlayer(PLAYER_1), (ConnectFourPlayer)this.getPlayer(PLAYER_2) ,getGameFrame()));
			
			this.getGameFrame().setVisible(true);
			this.goFirst = goFirst;
			new Thread(this).start();
			s.close();
		}
	}
	
	/**
	 * Getter method for the board
	 * 
	 * @return The board
	 */
	public char[][] getBoard() {
		return board;
	}

	/**
	 * The method that runs the Connect Four game
	 * 
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
			this.getGameFrame().setTitle("It is " + (this.getPlayer(turn)).getName() + "'s turn!");
			if (!isLocal) {
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
			} else {
				try {		
					if(this.getPlayer(turn) instanceof RemotePlayer){
						}
						else{
							Point d = ((ConnectFourPlayer)this.getPlayer(turn)).makeMove(); //does different depending on what type of player
							ConnectFourPlayer tempLocal;
							tempLocal = (ConnectFourPlayer)this.getPlayer(turn); //this has to be instance of RP
							this.updateMove(d.x, d.y, tempLocal);
						}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
			//gameOver = detectGame(); //let know if game is over
			//The game is never over
			if(findWinner() != 0)
				gameOver = true;
			
			if(turn == 1)
				turn = 0;
			else turn = 1;
			System.out.println("turn is now: " + turn);
		}
		//now find out winner
		winningScreen(findWinner());
		
	}
	
	/**
	 * A helper method that displays a window telling who the winner is
	 * The rules here are not
	 * 0 = no has one yet
	 * 1 = player 1 wins
	 * 2 = player 2 wins
	 * @param winnderNum Who won
	 */
	private void winningScreen(int winnerNum) {
		JFrame finished = new JFrame("Game!");
		finished.setSize(350, 100);
		JLabel label = new JLabel();
		label.setFont(new Font("Default", Font.BOLD, 20));
		label.setForeground(Color.red);
		if(winnerNum == 0)
			label.setText("It is a draw!");
		else
			label.setText(" And winner is " + getPlayer(winnerNum - 1).getName() + "!");
		finished.add(label, "Center");
		finished.setVisible(true);
	}
	
	/**
	 * A Helper method to figure out who the winner is
	 * The rules here are not
	 * 0 = no has one yet
	 * 1 = player 1 wins
	 * 2 = player 2 wins
	 * 
	 * @return num of the winner
	 */
	private int findWinner() {
		int winnerNum = 0;
		if (horizontalWinner() != 0) {
			winnerNum = horizontalWinner();
		}
		if (verticalWinner() != 0) {
			winnerNum = verticalWinner();
		}
		if (diagonalWinner() != 0) {
			winnerNum = diagonalWinner();
		}
		
		return winnerNum;
	}
	
	/**
	 * Helper method that checks for Horizontal winners
	 * The rules here are not
	 * 0 = no has one yet
	 * 1 = player 1 wins
	 * 2 = player 2 wins
	 * 
	 * @return num of the winner
	 */
	private int horizontalWinner() {
		//i is rows
		//j is columns
		//Check all of the columns
		for(int j = 0; j < COLUMNNUM; j++) {
			//Check all of the rows
			for(int i = 0; i < ROWNUM; i++) {
				if (board[i][j] == PIECE1 || board[i][j] == PIECE2 ) { //The point is not empty
					try { //handles i <= 9 || j <= 9
						int pieces = 1;
						int tempx = i;
						int tempy = j;
						char tempChar = board[i][j];
						
						while (true) {
							//All of the logic
							//All of the logic
							tempx++; //To the right 1
							if (board[tempx][tempy] == tempChar) {
								pieces++;
							} else {
								break;
							}
							//Did you win?
							if (pieces == FOURPIECES) {
								//yes
								//you win
								if(tempChar == ((ConnectFourPlayer)getPlayer(PLAYER_2)).getPiece()) {
									return 2; //Player 2 winner
								} else {
									return 1; //Player 1 winner
								}
							}
						}
				} catch(ArrayIndexOutOfBoundsException e) {
					//Nothing should happen
				}
				}
			}
		}
		return 0; //No Winner
	}
	
	/**
	 * Helper method that checks for Vertical winners
	 * The rules here are not
	 * 0 = no has one yet
	 * 1 = player 1 wins
	 * 2 = player 2 wins
	 * 
	 * @return num of the winner
	 */
	private int verticalWinner() {
		//i is rows
		//j is columns
		//Check all of the columns
		for(int j = 0; j < COLUMNNUM; j++) {
			//Check all of the rows
			for(int i = 0; i < ROWNUM; i++) {
				if (board[i][j] == PIECE1 || board[i][j] == PIECE2 ) { //The point is not empty
					try { //handles i <= 9 || j <= 9
						int pieces = 1;
						int tempx = i;
						int tempy = j;
						char tempChar = board[i][j];
						while (true) {
							//All of the logic
							tempy++; //Down 1 on the columns
							if (board[tempx][tempy] == tempChar) {
								pieces++;
							} else {
								break;
							}
							//Did you win?
							if (pieces == FOURPIECES) {
								//yes
								//you win
								if(tempChar == ((ConnectFourPlayer)getPlayer(PLAYER_2)).getPiece()) {
									return 2; //Player 2 winner
								} else {
									return 1; //Player 1 winner
								}
							}
						}
					} catch(ArrayIndexOutOfBoundsException e) {
						//Nothing should happen
					}
				}
			}
		}
		return 0; //No Winner
	}
	
	/**
	 * Helper method that checks for Diagonal winners
	 * The rules here are not
	 * 0 = no has one yet
	 * 1 = player 1 wins
	 * 2 = player 2 wins
	 * 
	 * @return num of the winner
	 */
	private int diagonalWinner() {
		if (checkForwardDiagonal() != 0) {
			return checkForwardDiagonal();
		}
		if (checkBackwardDiagonal() != 0) {
			return checkBackwardDiagonal();
		}
		return 0; //No Winner
	}
	
	/**
	 * Helper method that checks for Forward Diagonal winners
	 * The rules here are not
	 * 0 = no has one yet
	 * 1 = player 1 wins
	 * 2 = player 2 wins
	 * 
	 * @return num of the winner
	 */
	private int checkForwardDiagonal() {
		
		//i is rows
		//j is columns
		//Check all of the columns
		for(int j = 0; j < COLUMNNUM; j++) {
			//Check all of the rows
			for(int i = 0; i < ROWNUM; i++) {
				if (board[i][j] == PIECE1 || board[i][j] == PIECE2 ) { //The point is not empty
					try { //handles i <= 9 || j <= 9
					int pieces = 1;
					int tempx = i;
					int tempy = j;
					char tempChar = board[i][j];
					while (true) {
						tempx++; //To the right 1 on row
						tempy++; //Down 1 on the columns
						if (board[tempx][tempy] == tempChar) {
							pieces++;
						} else {
							break;
						}
						
						//Did you win?
						if (pieces == FOURPIECES) {
							//yes
							//you win
							if(tempChar == ((ConnectFourPlayer)getPlayer(PLAYER_2)).getPiece()) {
								return 2; //Player 2 winner
							} else {
								return 1; //Player 1 winner
							}
						}
					}
					} catch(ArrayIndexOutOfBoundsException e) {
						//Nothing should happen
					}
				}
			}
		}
		return 0; //No Winner
	}
	
	/**
	 * Helper method that checks for Backward Diagonal winners
	 * The rules here are not
	 * 0 = no has one yet
	 * 1 = player 1 wins
	 * 2 = player 2 wins
	 * 
	 * @return num of the winner
	 */
	private int checkBackwardDiagonal() {
		//i is rows
		//j is columns
		//Check all of the columns
		for(int j = 0; j < COLUMNNUM; j++) {
			//Check all of the rows
			for(int i = 0; i < ROWNUM; i++) {
				if (board[i][j] == PIECE1 || board[i][j] == PIECE2) { //The point is not empty
					try { //handles i <= 9 || j <= 9
					int pieces = 1;
					int tempx = i;
					int tempy = j;
					char tempChar = board[i][j];
					while (true) {
						tempx--; //To the left 1 on row
						tempy++; //Down 1 on the columns
						if (board[tempx][tempy] == tempChar) {
							pieces++;
						} else {
							break;
						}
						
						//Did you win?
						if (pieces == FOURPIECES) {
							//yes
							//you win
							if(tempChar == ((ConnectFourPlayer)getPlayer(PLAYER_2)).getPiece()) {
								return 2; //Player 2 winner
							} else {
								return 1; //Player 1 winner
							}
						}
					}
					} catch(ArrayIndexOutOfBoundsException e) {
						//Nothing should happen
					}
				}
			}
		}
		return 0; //No Winner
	}
	
	/**
	 * Method to update Moves
	 * 
	 * @param numx Move x
	 * @param numy Move y
	 * @param p1 Player 1
	 * @return Is the move possible?
	 */
	public boolean updateMove(int numx, int numy, Player p){
		if(board[numx][numy] == PIECE1 || board[numx][numy] == PIECE2){
				return false;
		} else { //Then do the move
			int tempy = BOTTOM_OF_COLUMN;
			while(board[numx][tempy] == PIECE1 || board[numx][tempy] == PIECE2) {
				tempy--;
			}
			if(p instanceof RemotePlayer) {
				board[numx][tempy] = ((RemotePlayer)p).getPiece();
			} else {
				board[numx][tempy] = ((ConnectFourPlayer)p).getPiece();
			}
			((BoardPanel)getGamePanel()).updateBoard(board);
			getGameFrame().repaint();
			return true;
		}
			
		
	}
	
	public boolean validMove(int numx, int numy, Player p) {
		if(board[numx][numy] == PIECE1 || board[numx][numy] == PIECE2){
			return false;
		} else {
			return true;
		}
	}
}
