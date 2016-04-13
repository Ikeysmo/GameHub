package connectFour;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import ticTacToe.TicTacToePlayer;

public class ConnectFour implements ActionListener, Runnable {
	JFrame gameMenu = null;
	JRadioButton pvp = new JRadioButton("Local PVP");
	JRadioButton playerCPUE = new JRadioButton("Player v CPU(easy)");
	JRadioButton playerCPUH = new JRadioButton("Player v CPU(hard)");
	JRadioButton pvpOnline = new JRadioButton("Remote PVP");
	JButton newGame = new JButton("New Game");
	JPanel mainPanel = new JPanel();
	JPanel menubar = new JPanel();
	JLabel instr1 = new JLabel("Remote Player IP ADDRESS:");
	JLabel errorMsg = new JLabel();
	JTextField ip_field = new JTextField();
	JTextField player1name = new JTextField("Player 1 Name");
	JTextField player2name = new JTextField("Player 2 Name");
	JPanel secondPanel = new JPanel();
	
	JFrame mudda;
	BoardPanel foo;
	
	public int turn = 0;
	private ConnectFourPlayer[] players = new ConnectFourPlayer[2];
	private char[][] board = new char[10][10];
	
	public ConnectFour() {
		gameMenu = new JFrame("Connect-Four!");
		gameMenu.setSize(600, 160);
		gameMenu.setMinimumSize(new Dimension(500, 180));
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
	
	public static void main(String[] args) {
		new ConnectFour();
	}
	
	public char[][] getBoard() {
		return board;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == pvpOnline){
			//ungray text field
			ip_field.setEditable(true);
			player1name.setEditable(true);
			player2name.setEditable(false);
		}
		if(e.getSource() == pvp){
			ip_field.setEditable(false);
			player1name.setEditable(true);
			player2name.setEditable(true);
		}
		if(e.getSource() == playerCPUE){
			ip_field.setEditable(false);
			player1name.setEditable(true);
			player2name.setEditable(false);
		}
		if(e.getSource() == playerCPUH){
			ip_field.setEditable(false);
			player1name.setEditable(true);
			player2name.setEditable(false);
		}
		
		if(e.getSource() == newGame) {
			if(pvp.isSelected()) {
				errorMsg.setText("");
				/*if(player1name.getText().isEmpty() || player1name.getText().equals("Player 1 Name")){
				errorMsg.setText("Error: Must enter a name for player 1 and player 2!");
				return;
			}
			if(player2name.getText().isEmpty() || player2name.getText().equals("Player 2 Name")){
				errorMsg.setText("Error: Must enter a name for player 1 and player 2!");
				return;
			}*/
			if(mudda != null)
				mudda.dispose();
			mudda = new JFrame("Player v Player (Local)");
			mudda.setSize(500, 500);
			mudda.setVisible(true);
			players[0] = new ConnectFourPlayer(player1name.getText());
			players[1] = new ConnectFourPlayer(player2name.getText());
			players[0].getConnectFour(this);
			players[1].getConnectFour(this);
			foo = new BoardPanel(players[0], players[1], mudda);
			mudda.add(foo);
			
			//show two fields...
		} else if (pvpOnline.isSelected()) {
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
			players[0] = new ConnectFourPlayer(player1name.getText());
			players[1] = new RemotePlayer(ip_field.getText(), this, players[0]);
			players[0].getConnectFour(this);
			foo = new BoardPanel(players[0], players[1], mudda);
			mudda.add(foo);
			//show one field
		}
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
		//	players[0] = new ConnectFourPlayer(player1name.getText());
		//	players[1] = new CPUPlayer(CPUPlayer.EASY, this);
		//	players[0].getConnectFour(this);
		//	foo = new BoardPanel(players[0], players[1], mudda);
		//	mudda.add(foo);
		//	//get field
		//}
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
		//	players[0] = new ConnectFourPlayer(player1name.getText());
		//	players[1] = new CPUPlayer(CPUPlayer.HARD,this);
		//	players[0].getConnectFour(this);
		//	foo = new BoardPanel(players[0], players[1] , mudda);
		//	mudda.add(foo);
		//	//get field
		//}
			
		new Thread(this).start();
		//handle the business
		}
	}
	
	public void run() {
		board = new char[10][10];
		boolean gameOver = false;
		players[0].assignPiece('X');
		players[1].assignPiece('O');
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!gameOver){
			mudda.setTitle("It is " + players[turn].getName() + "'s turn!");
			
			try {
				Point d = players[turn].makeMove();
				
				if(players[1] instanceof RemotePlayer){
					RemotePlayer temp = (RemotePlayer) players[1];
					temp.updateAll(d); //send it out to everyone
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
	
	private void winningScreen(int winnerNum) {
		JFrame finished = new JFrame("Game!");
		finished.setSize(350, 100);
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
	 * The rules here are not
	 * 0 = no has one yet
	 * 1 = player 1 wins
	 * 2 = player 2 wins
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
	
	private int horizontalWinner() {
		//i is rows
		//j is columns
		//Check all of the columns
		for(int j = 0; j < 10; j++) {
			//Check all of the rows
			for(int i = 0; i < 10; i++) {
				if (board[i][j] == 'X' || board[i][j] == 'O' ) { //The point is not empty
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
							if (pieces == 4) {
								//yes
								//you win
								if(tempChar == players[1].getPiece()) {
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
	
	private int verticalWinner() {
		//i is rows
		//j is columns
		//Check all of the columns
		for(int j = 0; j < 10; j++) {
			//Check all of the rows
			for(int i = 0; i < 10; i++) {
				if (board[i][j] == 'X' || board[i][j] == 'O' ) { //The point is not empty
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
							if (pieces == 4) {
								//yes
								//you win
								if(tempChar == players[1].getPiece()) {
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
	
	private int diagonalWinner() {
		if (checkForwardDiagonal() != 0) {
			return checkForwardDiagonal();
		}
		if (checkBackwardDiagonal() != 0) {
			return checkBackwardDiagonal();
		}
		return 0; //No Winner
	}
	
	private int checkForwardDiagonal() {
		
		//i is rows
		//j is columns
		//Check all of the columns
		for(int j = 0; j < 10; j++) {
			//Check all of the rows
			for(int i = 0; i < 10; i++) {
				if (board[i][j] == 'X' || board[i][j] == 'O' ) { //The point is not empty
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
						if (pieces == 4) {
							//yes
							//you win
							if(tempChar == players[1].getPiece()) {
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
	
	private int checkBackwardDiagonal() {
		//i is rows
		//j is columns
		//Check all of the columns
		for(int j = 0; j < 10; j++) {
			//Check all of the rows
			for(int i = 0; i < 10; i++) {
				if (board[i][j] == 'X' || board[i][j] == 'O') { //The point is not empty
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
						if (pieces == 4) {
							//yes
							//you win
							if(tempChar == players[1].getPiece()) {
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
	
	//=====================================================================
	
	public boolean updateMove(int numx, int numy, ConnectFourPlayer p1){
		
		//if(detectGame())//false
		//	return false;
		
			//check to see if works?
		if(board[numx][numy] == 'X' || board[numx][numy] == 'O'){
				return false;
			}
		else{
			//Need to put the piece at the bottom of the column
			int tempy = 9;
			while(board[numx][tempy] == 'X' || board[numx][tempy] == 'O') {
				tempy--;
			}
			board[numx][tempy] = p1.getPiece();
			foo.updateBoard(board);
			foo.repaint();
			//printBoard();
			return true;
		}
			
		
	}
	
	private void printBoard() {
		// TODO Auto-generated method stub
		for(int i = 0; i < 10; i++)
			System.out.print(String.valueOf(board[i][0]));
		System.out.println("");
		for(int i = 0; i < 10; i++)
			System.out.print(String.valueOf(board[i][1]));
		System.out.println("");
		for(int i = 0; i < 10; i++)
			System.out.print(String.valueOf(board[i][2]));
	}

}
