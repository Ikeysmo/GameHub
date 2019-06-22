package games.ticTacToe;

/**
 * This is the Remote Player for TicTacToe
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 * @version 1.0
 */


import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RemotePlayer extends TicTacToePlayer implements Runnable {
	/* Object Input Stream*/
	ObjectInputStream ois;
	/* Object Output Stream*/
	ObjectOutputStream oos;
	/*TicTacToe Game */
	TicTacToe tic;
	/* The name of the player */
	String name;
	/* Piece */
	char piece;
	
	
	/**
	 * Constructor of the RemotePlayer
	 * Player name represents the opposing online player's account. Gamehub Login should have this!
	 * 
	 * @param oos input stream
	 * @param ois output stream to server
	 * @param tic this is reference to tic tac toe to call things, set turn
	 * @param piece The piece of the player
	 */
	public RemotePlayer(ObjectOutputStream oos, ObjectInputStream ois, TicTacToe tic, String name, char piece) {
		this.ois = ois;
		this.oos = oos;
		this.tic = tic;
		this.name = name; 
		this.piece = piece;
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
	public Point makeMove() throws IOException {
		try {
			Point answer;
			while(true){
				 //Waiting on move from remote!
				 answer = (Point)ois.readObject();
				 //Got the move!
				 if(answer == null){
					 //Error occured
				 }
				 if(tic.updateMove(answer.x, answer.y, this)) //if this is true, valid move! Return
					break;
			}
			return answer;
		} catch (ClassNotFoundException e) {
			// Could not find class
			e.printStackTrace();
		}
		return null;
	}
	//TODO: What is everything?
	/**
	 * This method updates everything?
	 * update everything.. which is sending data to server
	 * 
	 * @param p Point where move was made
	 */
	public void updateAll(Point p) throws IOException{
		//Sending move to server now!
		oos.writeObject(p);
		
	}
	
	public void run() {
		
	}
}
