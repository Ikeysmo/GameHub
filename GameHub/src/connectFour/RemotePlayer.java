package connectFour;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RemotePlayer extends ConnectFourPlayer implements Runnable {
	/* Object Input Stream*/
	ObjectInputStream ois;
	/* Object Output Stream*/
	ObjectOutputStream oos;
	/*TicTacToe Game */
	ConnectFour connectFour;
	/* The name of the player */
	String name;
	/* Piece */
	char piece;
	
	public RemotePlayer(ObjectOutputStream oos, ObjectInputStream ois, ConnectFour connectFour, String name, char piece) {
		this.ois = ois;
		this.oos = oos;
		this.connectFour = connectFour;
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
				 if(connectFour.updateMove(answer.x, answer.y, this)) //if this is true, valid move! Return
					break;
			}
			return answer;
		} catch (ClassNotFoundException e) {
			// Could not find class
			e.printStackTrace();
		}
		return null;
	}
	
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
