package connectFour;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	public final static int PLAYERVPLAYER = 0;
	public final static int PLAYERVCPU = 1;
	public final static int PLAYERREMOTE = 2;
	private boolean gameOver = false;
	private JFrame frame;
	private char[][] board = new char[10][10];
	private int turn = 0; //alternate between 0 and 1
	
	public BoardPanel(ConnectFourPlayer p1, ConnectFourPlayer p2, JFrame currWindow) {
		p1.getPanel(this);
		p2.getPanel(this);
		
		frame = currWindow;
	}
	
	@Override
	public void paint(Graphics g) {
		getRootPane().setBackground(Color.white);
		drawBoard(g);
	}
	
	public void updateBoard(char[][] board) {
		this.board = board;
	}
	
	private void drawBoard(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		for(int i = 1; i < 10; i++) {
			g.drawLine((width/10)*i, 0, (width/10)*i, height);
			g.drawLine(0, (height/10)*i, width , (height/10)*i);
		}
		
		//draw the pieces
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if(board[i][j] == 'X' || board[i][j] == 'O')
					g.setColor(Color.blue);
					int fontsize = height/10;
					g.setFont(new Font("Default", Font.BOLD, fontsize));
					int locationx = (width/20)*(2*i) + fontsize/10;
					int locationy = (height/20)*(2*j)+(int)(0.90*fontsize);
					
					g.drawString(String.valueOf(board[i][j]), locationx, locationy); 
					
			}
		}
	}

}
