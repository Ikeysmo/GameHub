package ticTacToe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	public final static int PLAYERVPLAYER = 0;
	public final static int PLAYERVCPU = 1;
	public final static int PLAYERREMOTE = 2;
	private boolean gameOver = false;
	private JFrame frame;
	private char[][] board = new char[3][3]; 
	private int turn = 0; //alternate between 0 and 1
	
	public BoardPanel(TicTacToePlayer p1, TicTacToePlayer p2, JFrame currWindow) {
		// TODO Auto-generated constructor stub
		p1.getPanel(this);
		p2.getPanel(this);

		frame = currWindow;
		
	}
	
	@Override 
	public void paint(Graphics g){
		getRootPane().setBackground(Color.white);
		drawBoard(g);
		
	}
	public void updateBoard(char[][] board){
		this.board = board;
	}
	
	private void drawBoard(Graphics g) {
		// TODO Auto-generated method stub
		int width = getWidth();
		int height = getHeight();
	
		for(int i = 1; i < 3; i++){
			g.drawLine((width/3)*i, 0, (width/3)*i, height);
			g.drawLine(0, (height/3)*i, width , (height/3)*i);
		}
		//draw the pieces
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(board[i][j] == 'X' || board[i][j] == 'O')
					g.setColor(Color.blue);
					int fontsize = height/3;
					g.setFont(new Font("Default", Font.BOLD, fontsize));
					int locationx = (width/6)*(2*i) + fontsize/5;
					int locationy = (height/6)*(2*j)+(int)(0.90*fontsize);
					
					g.drawString(String.valueOf(board[i][j]), locationx, locationy); 
					
			}
		}
	}
	

}
