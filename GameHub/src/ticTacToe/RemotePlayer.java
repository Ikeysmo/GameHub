package ticTacToe;

import java.awt.Point;
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

public class RemotePlayer extends TicTacToePlayer implements Runnable {
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	
	
	public RemotePlayer(ObjectOutputStream oos2, ObjectInputStream ois2, TicTacToe tic, String playername, char piece) {
		// Player name represents the opposing online player's account. Gamehub Login should have this!
		ois = ois2; //input stream
		oos = oos2; //output stream to server
		this.tic = tic; //this is reference to tic tac toe to call things, set turn
		this.name = playername; 
		this.piece = piece;
	}
	public String getName(){
		return name;
	}
	@Override
	public Point makeMove() throws IOException {
		// TODO Auto-generated method stub
		try {
			Point answer;
			while(true){
				 System.out.println("Waitingo on move from remote!");
				 answer = (Point)ois.readObject();
				 System.out.println("Got the move!");
				 if(answer == null){
					 System.out.println("shouldn't receive null!");
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
	
	public void updateAll(Point p) throws IOException{
		//update everything.. which is sending data to server
		System.out.println("Sending move to server now!");
		oos.writeObject(p);
		
	}

	
	public void run(){ //not using this.. why???
		//recieving...
		Point answer;
		while(true){
			try {
				answer = (Point) ois.readObject();
				System.out.println(answer);
			} 
			catch (ClassNotFoundException | IOException e) {}
		}
	}
	
//end of class
}
