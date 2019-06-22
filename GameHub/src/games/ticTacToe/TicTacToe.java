package games.ticTacToe;

/**
 * The heart of the TicTacToe game
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 * @version 1.0
 */


import games.Game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
	/* Local */
	private boolean isLocal;
	
	public static void main(String args[]) {
		new TicTacToe();
	}
	
	public TicTacToe() {
		super("Tic Tac Toe", "ticTacToeIcon.gif", new JFrame(), new JPanel(), 700, 700, 500, 500, 2);
		isLocal = true;
		TicTacToePlayer p1 = new TicTacToePlayer("Zachary");
		p1.setGame(this);
		p1.assignPiece(PIECE1);
		setPlayer(PLAYER_2, (Player)p1);
		TicTacToePlayer p2 = new TicTacToePlayer("Isaiah");
		p2.setGame(this);
		p2.assignPiece(PIECE2);
		setPlayer(PLAYER_1, (Player)p2);
		
		setGamePanel(new BoardPanel(p1, p2));
		
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
		isLocal = false;
		this.goFirst = gofirst;
		System.out.println("here we go");
		//player 1 represents this guy's version
		Socket s = new Socket(ipaddress, 2021);
		oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(localplayer);
		ois = new ObjectInputStream(s.getInputStream());
		if(!goFirst) {
			TicTacToePlayer p2 = new TicTacToePlayer(localplayer);
			p2.setGame(this);
			p2.assignPiece(PIECE1);
			setPlayer(PLAYER_2, (Player)p2);
			TicTacToePlayer p1 = new RemotePlayer(oos, ois, this, remoteplayer, PIECE2); //This guy goes first
			p1.setGame(this);
			p1.assignPiece(PIECE2);
			setPlayer(PLAYER_1, (Player)p1);
			
			setGamePanel(new BoardPanel((TicTacToePlayer)this.getPlayer(PLAYER_1),(TicTacToePlayer)this.getPlayer(PLAYER_2)));
			
			this.getGameFrame().setVisible(true);
			new Thread(this).start();
		} else {
			TicTacToePlayer p1 = new TicTacToePlayer(localplayer);
			p1.setGame(this);
			p1.assignPiece(PIECE2);
			setPlayer(PLAYER_1, (Player)p1);
			TicTacToePlayer p2 = new RemotePlayer(oos, ois, this, remoteplayer, PIECE1); //This guy goes first
			p2.setGame(this);
			p2.assignPiece(PIECE1);
			setPlayer(PLAYER_2, (Player)p2);
			
			setGamePanel(new BoardPanel((TicTacToePlayer)this.getPlayer(PLAYER_1), (TicTacToePlayer)this.getPlayer(PLAYER_2)));
			
			this.getGameFrame().setVisible(true);
			this.goFirst = goFirst;
			new Thread(this).start();
			s.close();
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
							System.out.println("Is a TicTacToePlayer's turn");
							Point d = ((TicTacToePlayer)this.getPlayer(turn)).makeMove(); //does different depending on what type of player
							TicTacToePlayer tempLocal;
							tempLocal = (TicTacToePlayer)this.getPlayer(turn); //this has to be instance of RP
							this.updateMove(d.x, d.y, tempLocal);
						}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
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
				if(numOfPieces == 9 && isWinner() == ' ') {
					gameOver = true;
				}
		}
		gameOver(findWinner());
	}
	
	/**
	 * This method starts a new thread
	 * @throws IOException 
	 */
	public void startNewThread() throws IOException{ //this is ONLY needed for gameOver method!
		new Thread(this);
		Thread.currentThread().interrupt();
		new TicTacToe();
	}
	
	private void gameOver(int winnerNum) {
		JFrame endWindow = new JFrame("GameOver");
		JPanel endPanel = new JPanel();
		endPanel.setBackground(Color.black);
		endPanel.setBorder(BorderFactory.createLineBorder(saddleBrown, 10));
		endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));
		
		JLabel didYouWin = new JLabel("You Lose!!!");
		if (winnerNum == PLAYER_1) {
			didYouWin = new JLabel(getPlayer(PLAYER_1).getName() + " Win!");
		} else if (winnerNum == PLAYER_2) {
			didYouWin = new JLabel(getPlayer(PLAYER_2).getName() + " Win!");
		} else {
			didYouWin = new JLabel("Draw!");
		}
		didYouWin.setFont(new Font("Comic Sans MS", Font.ITALIC, 25));
		didYouWin.setForeground(Color.white);
		didYouWin.setAlignmentX(Component.CENTER_ALIGNMENT);
		endPanel.add(didYouWin);
		endWindow.setSize(END_FRAME_WIDTH, END_FRAME_HEIGHT);
		JButton newGame = new JButton("Play Again?");
		newGame.setBackground(Color.black);
        newGame.setForeground(Color.WHITE);
        newGame.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		endPanel.add(newGame);
		endWindow.add(endPanel);
		endWindow.setUndecorated(true);
		endWindow.setAlwaysOnTop(true);
		newGame.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								getGamePanel().validate();
								getGameFrame().validate();
								getGameFrame().dispose();
								endWindow.dispose();
								
								
								try {
									startNewThread();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								
							}
						});
		endWindow.pack();
		endWindow.setVisible(true);
		endWindow.setLocationRelativeTo(getGameFrame());
	}

	private int findWinner() {
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
		return playerWon;
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
		} else { //Then do the move
			if(p instanceof RemotePlayer) {
				board[numx][numy] = ((RemotePlayer)p).getPiece();
			} else {
				board[numx][numy] = ((TicTacToePlayer)p).getPiece();
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
