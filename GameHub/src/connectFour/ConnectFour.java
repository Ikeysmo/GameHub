package connectFour;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.IOException;

import ticTacToe.RemotePlayer;

public class ConnectFour {
	public int turn = 0;
	private ConnectFourPlayer[] players = new ConnectFourPlayer[2];
	private char[][] board;
	
	public ConnectFour() {
		
	}
	
	public static void main(String[] args) {
		new ConnectFour();
	}
	
	public char[][] getBoard() {
		return board;
	}
	
	public void actionPerformed(ActionEvent e) {
		//Do later
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
			
			
			gameOver = detectGame(); //let know if game is over
			
			if(turn == 1)
				turn = 0;
			else turn = 1;
			System.out.println("turn is now: " + turn);
		}
		//now find out winner
		findWinner();
		
	}
	
	private void findWinner() {
		horizontalWinner();
		verticalWinner();
		diagonalWinner();
	}
	
	private void horizontalWinner() {
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
									//let player know who won
								} else {
									//other player won... 
								}
							}
				} catch(ArrayIndexOutOfBoundsException e) {
					//Nothing should happen
				}
				}
			}
		}
	}
	
	private void verticalWinner() {
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
									//let player know who won
								} else {
									//other player won... 
								}
							}
					} catch(ArrayIndexOutOfBoundsException e) {
						//Nothing should happen
					}
				}
			}
		}
	}
	
	private void diagonalWinner() {
		checkForwardDiagonal();
		checkBackwardDiagonal();
	}
	
	private void checkForwardDiagonal() {
		
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
								//let player know who won
							} else {
								//other player won... 
							}
						}
					}
					} catch(ArrayIndexOutOfBoundsException e) {
						//Nothing should happen
					}
				}
			}
		}
	}
	
	private void checkBackwardDiagonal() {
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
								//let player know who won
							} else {
								//other player won... 
							}
						}
					}
					} catch(ArrayIndexOutOfBoundsException e) {
						//Nothing should happen
					}
				}
			}
		}
	}

}
