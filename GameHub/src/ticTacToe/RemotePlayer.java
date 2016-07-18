package ticTacToe;

/**
 * This is the Remote Player for TicTacToe
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 * @version 1.0
 */

import games.Game;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;

import player.Player;

public class RemotePlayer extends Player implements Runnable, MouseListener {
	/* Object Input Stream*/
	ObjectInputStream ois;
	/* Object Output Stream*/
	ObjectOutputStream oos;
	/* Is the mouse clicked? */
	public boolean mouseNotClicked = false;
	/* Is it ready?*/
	public boolean ready = false;
	/*piece*/
	char piece;
	/* X-cord of the move */
	protected int numx = -1;
	/* Y-cord of the move */
	protected int numy = -1;
	/* The board being played on*/
	private BoardPanel panel = null;
	/* The TicTacToe game*/
	protected TicTacToe tic = null;
	
	/* The number of rows */
	public final static int ROWNUM = 3;
	/* The number of columns */
	public final static int COLUMNNUM = 3;
	
	
	/**
	 * Constructor of the RemotePlayer
	 * Player name represents the opposing online player's account. Gamehub Login should have this!
	 * 
	 * @param oos input stream
	 * @param ois output stream to server
	 * @param tic this is reference to tic tac toe to call things, set turn
	 * @param piece The piece of the player
	 */
	public RemotePlayer(ObjectOutputStream oos, ObjectInputStream ois, Game tic, String name) {
		this.ois = ois;
		this.oos = oos;
		this.game = tic;
		this.name = name; 
	}
	
	/**
	 * Getter method for the player name
	 * 
	 * @return player name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Method for the Remote player to make a move
	 * 
	 * @return point where move was made
	 */
	@Override
	public synchronized Point makeMove(BoardPanel panel) throws IOException {
			while(true){
				try {
					panel.addMouseListener(this);
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				 //Waiting on move from remote!
				 oos.writeObject(new Point(numx, numy));
				 //Got the move!
				 return null;
			}
	}
	//TODO: What is everything?
	/**
	 * This method updates everything?
	 * update everything.. which is sending data to server
	 * 
	 * @param p Point where move was made
	 */
	public Point updateAll() throws IOException{
		//Sending move to server now!
		Point answer;
		try {
			answer = (Point)ois.readObject();
			return answer;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("SHOULD NO HAVE REACHED HERE");
		return null;
		
		
	}
	
	/**
	 * Getter method for the TicTacToe game
	 * 
	 * @return the TicTacToe game
	 */
	public void setGame(TicTacToe game){
		this.game = game;
	}
	
	/**
	 * Assigns a piece to the player (Setter)
	 * 
	 * @param 'X' or 'O'
	 */
	public void assignPiece(char x){
		if(x == 'X' || x == 'O')
			this.piece = x; //assign piece if this piece is legal!
		else
			throw new IllegalArgumentException();
	}
	
	public char getPiece() {
		return piece;
	}
	
	public void run() {
		
	}
	
	/**
	 * Method for if a mouse is pressed
	 * 
	 * @param e The mouse being pressed
	 */
	@Override
	public synchronized void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		int widthFactor = panel.getWidth()/COLUMNNUM;
		int heightFactor = panel.getHeight()/ROWNUM;
		numx = e.getX()/widthFactor;
		numy = e.getY()/heightFactor;
		
		ready = false;
		notify();
		panel.removeMouseListener(this);
	}
	
	/**
	 * Getter method for the board
	 * 
	 * @return the board
	 */
	public void setPanel(BoardPanel panel){
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
