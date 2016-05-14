package connectFour;

/**
 * This is the logic for the Connect Four game
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

public class ConnectFour implements ActionListener, Runnable {
	
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
	
	/** Main JFrame */
	JFrame gameMenu = null;
	/* Local PVP Button*/
	JRadioButton pvp = new JRadioButton("Local PVP");
	/* Player vs CPU Easy Button*/
	JRadioButton playerCPUE = new JRadioButton("Player v CPU(easy)");
	/* Player vs CPU Hard Button*/
	JRadioButton playerCPUH = new JRadioButton("Player v CPU(hard)");
	/* Remote Player vs PLayer Button*/
	JRadioButton pvpOnline = new JRadioButton("Remote PVP");
	/* New Game Button*/
	JButton newGame = new JButton("New Game");
	/* Main Panel */
	JPanel mainPanel = new JPanel();
	/* Menu Bar Panel*/
	JPanel menubar = new JPanel();
	/* Remote Player IP Address Label*/
	JLabel instr1 = new JLabel("Remote Player IP ADDRESS:");
	/* Error Message Label*/
	JLabel errorMsg = new JLabel();
	/* IP Address text field*/
	JTextField ip_field = new JTextField();
	/* Player 1 Name text field*/
	JTextField player1name = new JTextField("Player 1 Name");
	/* Player 2 Name text field*/
	JTextField player2name = new JTextField("Player 2 Name");
	/* Second Panel*/
	JPanel secondPanel = new JPanel();
	
	//TODO: What does mudda mean?
	JFrame mudda;
	//TODO: What does foo mean?
	BoardPanel foo;
	
	/*Tells you whose turn it is*/
	public int turn = 0;
	/*The players in the game*/
	private ConnectFourPlayer[] players = new ConnectFourPlayer[PLAYERNUM];
	/*The board being used */
	private char[][] board = new char[ROWNUM][COLUMNNUM];
	/* Object Output Stream */
	private ObjectOutputStream oos;
	/* Object Input Stream */
	private ObjectInputStream ois;
	
	/**
	 * The constructor of Connect Four
	 */
	public ConnectFour() {
		gameMenu = new JFrame("Connect-Four!");
		gameMenu.setSize(FRAMEWIDTH, FRAMEHEIGHT);
		gameMenu.setMinimumSize(new Dimension(MINFRAMEWIDTH, MINFRAMEHEIGHT));
		gameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pvp.addActionListener(this);
		playerCPUE.addActionListener(this);
		playerCPUH.addActionListener(this);
		pvpOnline.addActionListener(this);
		newGame.addActionListener(this);
		ButtonGroup group = new ButtonGroup();
		group.add(pvp);
		group.add(playerCPUE);
		group.add(playerCPUH);
		group.add(pvpOnline);
		menubar.add(pvp);
		menubar.add(playerCPUE);
		menubar.add(playerCPUH);
		menubar.add(pvpOnline);
		menubar.add(newGame);
		mainPanel.add(menubar);
		errorMsg.setFont(new Font("Default", Font.BOLD, 15));
		errorMsg.setForeground(Color.red);
		instr1.setFont(new Font("Default", Font.BOLD, 15));
		ip_field.setFont(new Font("Default", Font.BOLD, 15));
		ip_field.setPreferredSize(new Dimension(150,20));
		ip_field.setEditable(false);
		player1name.setPreferredSize(new Dimension(150,20));
		player1name.setFont(new Font("Default", Font.BOLD, 15));
		pvp.setSelected(true);
		player2name.setPreferredSize(new Dimension(150,20));
		player2name.setFont(new Font("Default", Font.BOLD, 15));
		secondPanel.add(instr1);
		secondPanel.add(ip_field);
		secondPanel.add(Box.createRigidArea(new Dimension(100,0)));
		secondPanel.add(player1name);
		secondPanel.add(player2name);
		gameMenu.getContentPane().add(mainPanel,"North");
		gameMenu.getContentPane().add(secondPanel, "Center");
		gameMenu.getContentPane().add(errorMsg, "South");
		//add this to something else //gameWindow.getContentPane().add(new BoardPanel(),"Center");
		
		//Keeps the whole GUI from shutting down
		gameMenu.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		gameMenu.setVisible(true);
	}
	
	/**
	 * Second constructor of Connect Four
	 * 
	 * @param localPlayer The local player
	 * @param remotePlayer The remote player
	 * @param goFirst Who goes first
	 */
	public ConnectFour(String localPlayer, String remotePlayer, boolean goFirst, String ipaddress) throws UnknownHostException, IOException {
		Socket s = new Socket(ipaddress, 2021);
		oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(localPlayer);
		ois = new ObjectInputStream(s.getInputStream());
	
		//end
		if(!goFirst){
			players[PLAYER_1] = new RemotePlayer(oos, ois, this, remotePlayer, PIECE1);
			players[PLAYER_2] = new ConnectFourPlayer(localPlayer);
			players[PLAYER_2].getConnectFour(this);
		}
		else{
			players[PLAYER_1] = new ConnectFourPlayer(localPlayer);
			players[PLAYER_1].getConnectFour(this);
			players[PLAYER_2] = new RemotePlayer(oos, ois, this, remotePlayer, PIECE2);
		}
		
		mudda = new JFrame("Player Online");
		mudda.setSize(500, 500);
		mudda.setVisible(true);
		foo = new BoardPanel(players[PLAYER_1], players[PLAYER_2], mudda);
		mudda.add(foo);
		new Thread(this).start();
	}

	/**
	 * The main method
	 * 
	 * @param args Command line Arguments
	 */
	public static void main(String[] args) {
		new ConnectFour();
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
	 * This method activates when the action is performed
	 * 
	 * @param e The action that caused the method to activate
	 */
	public void actionPerformed(ActionEvent e) {
		//Player vs Player Online button is pressed
		if(e.getSource() == pvpOnline){
			//ungray text field
			ip_field.setEditable(true);
			player1name.setEditable(true);
			player2name.setEditable(false);
		}
		//PLayer vs PLayer button is pressed
		if(e.getSource() == pvp){
			ip_field.setEditable(false);
			player1name.setEditable(true);
			player2name.setEditable(true);
		}
		//PLayer vs CPU(Easy) button is presed
		if(e.getSource() == playerCPUE){
			ip_field.setEditable(false);
			player1name.setEditable(true);
			player2name.setEditable(false);
		}
		//PLayer vs CPU(Hard) button is pressed
		if(e.getSource() == playerCPUH){
			ip_field.setEditable(false);
			player1name.setEditable(true);
			player2name.setEditable(false);
		}
		//New Game Button is pressed
		if(e.getSource() == newGame) { //TODO: How does new game work?
			if(pvp.isSelected()) {
				errorMsg.setText("");
			if(mudda != null)
				mudda.dispose();
			mudda = new JFrame("Player v Player (Local)");
			mudda.setSize(500, 500);
			mudda.setVisible(true);
			players[PLAYER_1] = new ConnectFourPlayer(player1name.getText());
			players[PLAYER_2] = new ConnectFourPlayer(player2name.getText());
			players[PLAYER_1].getConnectFour(this);
			players[PLAYER_2].getConnectFour(this);
			foo = new BoardPanel(players[PLAYER_1], players[PLAYER_2], mudda);
			mudda.add(foo);
			
			//show two fields...
		} else if (pvpOnline.isSelected()) { //TODO: How does pvpOnline work?
			errorMsg.setText("");
			if(player1name.getText().isEmpty() || player1name.getText().equals("Player 1 Name")) {
				errorMsg.setText("Error: Must enter a name for player 1 and player 2!");
				return;
			}
			if(mudda != null)
				mudda.dispose();
			mudda = new JFrame("Player Online");
			mudda.setSize(500, 500);
			mudda.setVisible(true);
			players[PLAYER_1] = new ConnectFourPlayer(player1name.getText());
			players[PLAYER_2] = new RemotePlayer(ip_field.getText(), this, players[0]);
			players[PLAYER_1].getConnectFour(this);
			foo = new BoardPanel(players[PLAYER_1], players[PLAYER_2], mudda);
			mudda.add(foo);
			//show one field
		} //TODO: How does Easy CPU work?
		//} else if(playerCPUE.isSelected()) {
		//	errorMsg.setText("");
		//	if(player1name.getText().isEmpty() || player1name.getText().equals("Player 1 Name")){
		//		errorMsg.setText("Error: Must enter a name for player 1 and player 2!");
		//		return;
		//	}
		//	if(mudda != null)
		//		mudda.dispose();
		//	mudda = new JFrame("Player vs AI");
		//	mudda.setSize(500, 500);
		//	mudda.setVisible(true);
		//	players[0] = new SnakePlayer(player1name.getText());
		//	players[1] = new CPUPlayer(CPUPlayer.EASY, this);
		//	players[0].getSnake(this);
		//	foo = new BoardPanel(players[0], players[1], mudda);
		//	mudda.add(foo);
		//	//get field
		//} 
		//TODO: How does Hard CPU work?
		//else if(playerCPUH.isSelected()) {
		///	errorMsg.setText("");
		//	if(player1name.getText().isEmpty() || player1name.getText().equals("Player 1 Name")){
		//		errorMsg.setText("Error: Must enter a name for player 1 and player 2!");
		//		return;
		//	}
		//	if(mudda != null)
		//		mudda.dispose();
		//	mudda = new JFrame("Player vs AI");
		//	mudda.setSize(500, 500);
		//	mudda.setVisible(true);
		//	players[0] = new SnakePlayer(player1name.getText());
		//	players[1] = new CPUPlayer(CPUPlayer.HARD,this);
		//	players[0].getSnake(this);
		//	foo = new BoardPanel(players[0], players[1] , mudda);
		//	mudda.add(foo);
		//	//get field
		//}
			
		//Starts the game
		new Thread(this).start();
		//handle the business
		}
	}

	/**
	 * The method that runs the Connect Four game
	 * 
	 */
	public void run() {
		board = new char[ROWNUM][COLUMNNUM];
		boolean gameOver = false;
		players[PLAYER_1].assignPiece(PIECE1);
		players[PLAYER_2].assignPiece(PIECE2);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(!gameOver){
			mudda.setTitle("It is " + players[turn].getName() + "'s turn!");
			
			try {
				Point d = players[turn].makeMove();
				
				if(players[turn] instanceof connectFour.RemotePlayer){
					
				}
				else{
					connectFour.RemotePlayer temp;
					if(turn == 0)
						temp = (connectFour.RemotePlayer) players[PLAYER_2]; //this has to be instance of RP
					else
						temp = (connectFour.RemotePlayer) players[PLAYER_1]; //this has to be RP
					
					temp.updateAll(d); //send it out to everyone
				}
			} catch (IOException e) {
				e.printStackTrace();
			} //I want this to happen, and then wait until this is done
			
			
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
		finished.setSize(WINFRAMEWIDTH, WINFRAMEHEIGHT);
		JLabel label = new JLabel();
		label.setFont(new Font("Default", Font.BOLD, 20));
		label.setForeground(Color.red);
		if(winnerNum == 0)
			label.setText("It is a draw!");
		else
			label.setText(" And winner is " + players[winnerNum - 1].getName() + "!");
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
								if(tempChar == players[PLAYER_2].getPiece()) {
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
								if(tempChar == players[PLAYER_2].getPiece()) {
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
							if(tempChar == players[PLAYER_2].getPiece()) {
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
							if(tempChar == players[PLAYER_2].getPiece()) {
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
		public boolean updateMove(int numx, int numy, ConnectFourPlayer p1){
		
		if(board[numx][numy] == PIECE1 || board[numx][numy] == PIECE2){
				return false;
			}
		else{
			//Need to put the piece at the bottom of the column
			int tempy = BOTTOM_OF_COLUMN;
			while(board[numx][tempy] == PIECE1 || board[numx][tempy] == PIECE2) {
				tempy--;
			}
			board[numx][tempy] = p1.getPiece();
			foo.updateBoard(board);
			foo.repaint();
			//printBoard(); //Debug purposes
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
