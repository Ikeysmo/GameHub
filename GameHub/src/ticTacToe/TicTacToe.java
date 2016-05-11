package ticTacToe;

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
	JFrame gameMenu = null;
	JPanel mainPanel = new JPanel();
	JPanel menubar = new JPanel();
	JLabel errorMsg = new JLabel();
	JPanel secondPanel = new JPanel();
	JFrame mudda;
	BoardPanel foo;
	public int turn = 0;
	private TicTacToePlayer[] players = new TicTacToePlayer[2];
	private char[][] board = new char[3][3];
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public TicTacToe(String localplayer, String remoteplayer, Boolean goFirst) throws UnknownHostException, IOException {
		//player 1 represents this guy's version
		Socket s = new Socket("localhost", 2021);
		oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(localplayer);
		ois = new ObjectInputStream(s.getInputStream());
		//System.out.println("First p: "+localplayer + ", Second p: " + remoteplayer);
		if(!goFirst){
			players[1] = new TicTacToePlayer(localplayer); //you become player num 2
			players[1].getTic(this);
			players[1].assignPiece('X');
			players[0] = new RemotePlayer(oos, ois, this, remoteplayer, 'O'); //this guys go first
		}else {
			players[0] = new TicTacToePlayer(localplayer);
			players[0].getTic(this);
			players[0].assignPiece('O');
			players[1] = new RemotePlayer(oos, ois, this, remoteplayer, 'X');
		}
		if(mudda != null)
			mudda.dispose();
		mudda = new JFrame(remoteplayer + " is Online!");
		mudda.setSize(500, 500);
		mudda.setVisible(true);
		System.out.println("Should be another checkpoint");		
		foo = new BoardPanel(players[0], players[1] , mudda);
		mudda.add(foo);
		mudda.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //what i ideally want is to have server end match when done
		new Thread(this).start();
	}

	public char[][] getBoard(){
		return board;	
	}
						

//=====================================================================
	
	public void run() {
		board = new char[3][3];
		boolean gameOver = false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
						temp = (RemotePlayer) players[1]; //this has to be instance of RP
					else
						temp = (RemotePlayer) players[0]; //this has to be RP
					
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
			for(int i = 0; i < 3; i++)
					System.out.println(String.valueOf(board[i][0]) + String.valueOf(board[i][1]) + String.valueOf(board[i][2]));
			System.out.println("turn is now: " + turn);
		}
		//now find out winner
		findWinner();
	}
	
private void findWinner() {
	// TODO Auto-generated method stub
	int playerWon = -1;
	for(int i = 0; i < 2; i++){
		//verticals
		if(board[0][0] == players[i].getPiece() && board[0][1] == players[i].getPiece() && board[0][2] == players[i].getPiece())
			playerWon = i;
		if(board[1][0] == players[i].getPiece() && board[1][1] == players[i].getPiece() && board[1][2] == players[i].getPiece())
			playerWon = i;
		if(board[2][0] == players[i].getPiece() && board[2][1] == players[i].getPiece() && board[2][2] == players[i].getPiece())
			playerWon = i;
		//horizontals
		if(board[0][0] == players[i].getPiece() && board[1][0] == players[i].getPiece() && board[2][0] == players[i].getPiece())
			playerWon = i;
		if(board[0][1] == players[i].getPiece() && board[1][1] == players[i].getPiece() && board[2][1] == players[i].getPiece())
			playerWon = i;
		if(board[0][2] == players[i].getPiece() && board[1][2] == players[i].getPiece() && board[2][2] == players[i].getPiece())
			playerWon = i;
		//diagonals
		if(board[0][0] == players[i].getPiece() && board[1][1] == players[i].getPiece() && board[2][2] == players[i].getPiece())
			playerWon = i;
		if(board[0][2] == players[i].getPiece() && board[1][1] == players[i].getPiece() && board[2][0] == players[i].getPiece())
			playerWon = i;
		
		
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

//=====================================================================
	
	private boolean detectGame() {
		// TODO Auto-generated method stub
		//first see if it's full
		if(detectFull())
			return true;
		
		//vertical
		char piece = 'X';
		for(int i = 0; i < 3; i++){
			if(board[i][0] == piece &&board[i][1] == piece && board[i][2] == piece)
				return true;
		}
		
		//horizontal
		for(int i = 0; i < 3; i++){
			if(board[0][i] == piece &&board[1][i] == piece && board[2][i] == piece)
				return true;
		}
		
		//diagonals
		if(board[0][0] == piece && board[1][1] == piece && board[2][2] == piece)
			return true;
		if(board[0][2] == piece && board[1][1] == piece && board[2][0] == piece)
			return true;
		
		
		//now try for '0'
		piece = 'O';
		for(int i = 0; i < 3; i++){
			if(board[i][0] == piece &&board[i][1] == piece && board[i][2] == piece)
				return true;
		}
		//horizontal
		for(int i = 0; i < 3; i++){
			if(board[0][i] == piece &&board[1][i] == piece && board[2][i] == piece)
				return true;
		}
		//diagonals
		if(board[0][0] == piece && board[1][1] == piece && board[2][2] == piece)
			return true;
		if(board[0][2] == piece && board[1][1] == piece && board[2][0] == piece)
			return true;
		//else...
		return false;
	}
	private boolean detectFull() {
		// TODO Auto-generated method stub
		for(int i = 0; i < 3; i++){
			for(int j = 0; j<3; j++){
				if(board[i][j] != 'X' && board[i][j] != 'O'){
					
					return false;
				}
					
			}
		}
		return true;
	}
	public boolean updateMove(int numx, int numy, TicTacToePlayer p1){
		
		//if(detectGame())//false
		//	return false;
		
			//Is the place already taken?
		if(board[numx][numy] == 'X' || board[numx][numy] == 'O'){
				return false;
		} else{ //Then do the move
			System.out.println("Updating board at " + (numx+1) + "," + (numy+1));
			System.out.println("This is the piece " + p1.getPiece());
			board[numx][numy] = p1.getPiece();
			foo.updateBoard(board);
			foo.repaint();
			//printBoard();
			return true;
		}
			
		
	}

	private void printBoard() {
		// TODO Auto-generated method stub
		for(int i = 0; i < 3; i++)
			System.out.print(String.valueOf(board[i][0]));
		System.out.println("");
		for(int i = 0; i < 3; i++)
			System.out.print(String.valueOf(board[i][1]));
		System.out.println("");
		for(int i = 0; i < 3; i++)
			System.out.print(String.valueOf(board[i][2]));
	}
}