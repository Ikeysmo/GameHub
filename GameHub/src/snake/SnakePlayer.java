package snake;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import gameHub.Player;

public class SnakePlayer extends Player implements MouseListener {
	public boolean mouseNotClicked = false;
	public boolean ready = false;
	private BoardPanel panel = null;
	protected Snake snake = null;
	protected int numx = -1;
	protected int numy = -1;
	protected char piece;
	
	public SnakePlayer() {
		
	}
	
	public synchronized Point makeMove() throws IOException {
		
		while(true) {
			try {
				panel.addMouseListener(this);
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(snake.updateMove(numx, numy, this)) // if this is true, valid move!
				break;
		}
		return new Point(numx, numy);
	}
		//wait for event...
	
	@Override
	public synchronized void mousePressed(MouseEvent e) {
		
		int widthFactor = panel.getWidth()/10;
		int heightFactor = panel.getHeight()/10;
		numx = e.getX()/widthFactor;
		numy = e.getY()/heightFactor;
		
		ready = false;
		notify();
		panel.removeMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	public String getName() {
		return name;
	}
	
	public void assignPiece(char x) {
		if(x == 'X' || x == 'O')
			this.piece = x;
		else
			throw new IllegalArgumentException();
	}
	
	public char getPiece() {
		return this.piece;
	}
	
	public SnakePlayer(String name) {
		this.name = name;
	}
	
	public void getPanel(BoardPanel panel) {
		this.panel = panel;
	}
	
	public void getSnake(Snake snake) {
		this.snake = snake;
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
