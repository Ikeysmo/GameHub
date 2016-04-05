package ticTacToe;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class CPUPlayer extends TicTacToePlayer {
	public static final int EASY = 0;
	public static final int HARD = 1;
	private TicTacToe tic;
	private int mode;
	public CPUPlayer(int mode, TicTacToe tic) {
		// TODO Auto-generated constructor stub
		name = "AI";
		this.mode = mode;
		this.tic = tic;
		tic.getBoard();
	}

	public void run(){
		System.out.println("Superman Saves");
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("facebook");
	}
	@Override
	public Point makeMove() {
		// TODO Auto-generated method stub
		int index = 0;
		Vector <Point> moves = null;
		char[][] board = tic.getBoard();
		moves = new Vector<Point>();
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(board[i][j] != 'X' && board[i][j] != 'O')
					moves.add(new Point(i,j));
			} //fill up available moves already
		}
		if(mode == EASY){
		index = (int) (Math.random()*moves.size());
		System.out.println("Index is:" + index);
		tic.updateMove(moves.elementAt(index).x, moves.elementAt(index).y, this);
		}
		
		else if(mode == HARD){
			//all the code for harder mode!
			//prevent opponent from winning!
			int count = 0;
			Point po = null;
			for(int i = 0; i < 3; i++){
				if(board[i][0] == piece)
					count++;
				if(board[i][0] != 'X' && board[i][0] != 'O')
					po = new Point(i,0);
			}
			if(!(count > 1))
				po = null;
			count = 0;
			
			for(int i = 0; i < 3; i++){
				if(board[i][1] == piece)
					count++;
				if(board[i][1] != 'X' && board[i][1] != 'O')
					po = new Point(i,1);
			}
			if(!(count > 1))
				po = null;
			
			count = 0;
			for(int i = 0; i < 3; i++){
				if(board[i][2] == piece)
					count++;
				if(board[i][2] != 'X' && board[i][2] != 'O')
					po = new Point(i,2);
			}
			if(!(count > 1))
				po = null;
			

			//tic.updateMove(index, index, p1)	
		}
		return new Point(moves.elementAt(index).x, moves.elementAt(index).y);
	
	}
}


