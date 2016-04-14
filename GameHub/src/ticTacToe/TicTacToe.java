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

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class TicTacToe implements ActionListener, Runnable{
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
	private TicTacToePlayer[] players = new TicTacToePlayer[2];
	private char[][] board = new char[3][3];

	public TicTacToe() {
		// TODO Auto-generated constructor stub
		gameMenu = new JFrame("Tic-Tac-Toe!");
		gameMenu.setSize(600, 160);
		gameMenu.setMinimumSize(new Dimension(500,180));
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

	public TicTacToe(ObjectOutputStream oos, ObjectInputStream ois) {
		// TODO Auto-generated constructor stub
		if(mudda != null)
			mudda.dispose();
		mudda = new JFrame("Player Online");
		mudda.setSize(500, 500);
		mudda.setVisible(true);
		players[0] = new TicTacToePlayer(player1name.getText());
		players[1] = new RemotePlayer(oos, ois, this, players[0]);
		players[0].getTic(this);
		foo = new BoardPanel(players[0], players[1] , mudda);
		mudda.add(foo);
		new Thread(this).start();
	}

	public static void main(String[] args) {
		new TicTacToe();
	}
	public char[][] getBoard(){
		return board;
	}
	@Override
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

		if(e.getSource() == newGame){
			if(pvp.isSelected()){
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
				players[0] = new TicTacToePlayer(player1name.getText());
				players[1] = new TicTacToePlayer(player2name.getText());
				players[0].getTic(this);
				players[1].getTic(this);
				foo = new BoardPanel(players[0], players[1] , mudda);
				mudda.add(foo);

				//show two fields...
			}
			else if (pvpOnline.isSelected()){
				errorMsg.setText("");
				if(player1name.getText().isEmpty() || player1name.getText().equals("Player 1 Name")){
					errorMsg.setText("Error: Must enter a name for player 1 and player 2!");
					return;
				}
				if(mudda != null)
					mudda.dispose();
				mudda = new JFrame("Player Online");
				mudda.setSize(500, 500);
				mudda.setVisible(true);
				players[0] = new TicTacToePlayer(player1name.getText());
				players[1] = new RemotePlayer(ip_field.getText(), this, players[0]);
				players[0].getTic(this);
				foo = new BoardPanel(players[0], players[1] , mudda);
				mudda.add(foo);
				//show one field
			}
			else if(playerCPUE.isSelected()){
				errorMsg.setText("");
				if(player1name.getText().isEmpty() || player1name.getText().equals("Player 1 Name")){
					errorMsg.setText("Error: Must enter a name for player 1 and player 2!");
					return;
				}
				if(mudda != null)
					mudda.dispose();
				mudda = new JFrame("Player vs AI");
				mudda.setSize(500, 500);
				mudda.setVisible(true);
				players[0] = new TicTacToePlayer(player1name.getText());
				players[1] = new CPUPlayer(CPUPlayer.EASY, this);
				players[0].getTic(this);
				foo = new BoardPanel(players[0], players[1] , mudda);
				mudda.add(foo);
				//get field
			}
			else if(playerCPUH.isSelected()){
				errorMsg.setText("");
				if(player1name.getText().isEmpty() || player1name.getText().equals("Player 1 Name")){
					errorMsg.setText("Error: Must enter a name for player 1 and player 2!");
					return;
				}
				if(mudda != null)
					mudda.dispose();
				mudda = new JFrame("Player vs AI");
				mudda.setSize(500, 500);
				mudda.setVisible(true);
				players[0] = new TicTacToePlayer(player1name.getText());
				players[1] = new CPUPlayer(CPUPlayer.HARD,this);
				players[0].getTic(this);
				foo = new BoardPanel(players[0], players[1] , mudda);
				mudda.add(foo);
				//get field
			}
			
			
			new Thread(this).start();
			//handle thy business
		}
	}
//=====================================================================
	
	public void run() {
		board = new char[3][3];
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