package ticTacToe;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import gameHub.Player;
import ticTacToe.BoardPanel;
import ticTacToe.TicTacToe;

public class TicTacToePlayer extends Player implements MouseListener{
	public boolean mouseNotClicked = false;
	public boolean ready = false;
	private BoardPanel panel = null;
	protected TicTacToe tic = null;
	protected int numx = -1;
	protected int numy = -1;
	protected char piece;
	protected String name;
	
	public TicTacToePlayer(){}
	public TicTacToePlayer(String name){
		this.name = name;
	}
	
	//somehow get move?
	public synchronized Point makeMove() throws IOException {
		// TODO Auto-generated method stub
		while(true){
			try {
				panel.addMouseListener(this);
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(tic.updateMove(numx, numy, this)) //if this is true, valid move!
				break;
		}
		return new Point(numx,numy);
	}
		//wait for event... 
	
	@Override
	public synchronized void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		int widthFactor = panel.getWidth()/3;
		int heightFactor = panel.getHeight()/3;
		numx = e.getX()/widthFactor;
		numy = e.getY()/heightFactor;
		
		ready = false;
		notify();
		panel.removeMouseListener(this);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public String getName(){
		return name;
	}
	public void assignPiece(char x){
		if(x == 'X' || x == 'O')
			this.piece = x; //assign piece if this piece is legal!
		else
			throw new IllegalArgumentException();
	}
	public char getPiece(){
		return this.piece;
	}
	
	public void getPanel(BoardPanel panel){
		this.panel = panel;
	}
	public void getTic(TicTacToe tic){
		this.tic = tic;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
