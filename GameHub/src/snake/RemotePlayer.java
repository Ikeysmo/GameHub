package snake;

/**
 * This is the Remote Player class for the Snake Game
 * 
 * @author Zachary Jones
 * @author Isaiah Smoak
 * @version 1.0
 */

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class RemotePlayer extends SnakePlayer implements Runnable {
	/*Port the remotePlayer is running on*/
	int port = 2000;
	/*IP address of the remote player*/
	String ip_address;
	/*Socket for the remote player*/
	Socket s;
	/* For retrieving objects*/
	ObjectInputStream ois;
	/* For sending objects*/
	ObjectOutputStream oos;
	/* The Snake games the remote player is running on*/
	public Snake snake;
	
	/**
	 * The constructor of the RemotePlayer
	 * 
	 * @param ipAddress IP addres of the player
	 * @param tic The game snake
	 * @param op TheSnakePlayer for this RemotePLayer
	 */
	public RemotePlayer(String ipaddress, Snake snake, SnakePlayer op) {
		this.snake = snake;
		this.ip_address = ipaddress;
		
		try {
			s = new Socket(); //Make a new socket
			s.connect(new InetSocketAddress(ip_address, port), 500); //Open the new Socket
			oos = new ObjectOutputStream(s.getOutputStream()); //Make a object output stream
			ois = new ObjectInputStream(s.getInputStream()); //Make a object input stream
			oos.writeObject(op.name); //Send the SnakePlayer to the object output stream
			name = (String) ois.readObject(); //Read the name of the player from the object input stream
			snake.turn = 1; //Set whose turn it is in the game
			piece = 'O'; //Give the player a piece
			//player 2
			//TODO: You need to comment out how all of the exceptions work
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Couldn't connect.. beginning server side!");
			try {//if failed, try hosting! and become player 1
				snake.turn = 0;
				piece = 'X';
				ServerSocket ss = new ServerSocket(port);
				s = ss.accept();
				oos = new ObjectOutputStream(s.getOutputStream());
				ois = new ObjectInputStream(s.getInputStream());
				try {
					name = (String) ois.readObject();
				} catch (ClassNotFoundException eR) {
					// TODO Auto-generated catch block
					eR.printStackTrace();
				}
				oos.writeObject(op.name);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();

		}
		//new Thread(this).start(); //I actually want it to wait!
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Making a move as a RemotePlayer
	 * 
	 * @return the place chosen on the move
	 */
	@Override
	public Point makeMove() throws IOException {
		try {
			Point answer; //Where the person made his move
			
			//This loop breaks when a valid move is found
			while(true){
				 answer = (Point)ois.readObject(); //Look for the answer
				 if(snake == null)
					 System.out.println("There is no Snake game");
				 else if(answer == null){
					 System.out.println("There is not in the Object input stream");
				 }
				 if(snake.updateMove(answer.x, answer.y, this)) //if this is true, valid move!
					break;
			}
			return answer;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method updates RemotePLayer with the move made
	 * and any other changes
	 * 
	 * @param p The point
	 */
	public void updateAll(Point p) throws IOException{
		//TODO: What does this mean?
		//update everything
		oos.writeObject(p);
		
	}

	/**
	 * This is the run method, where the move is done
	 * 
	 */
	public void run(){
		Point answer;
		
		//This loop breaks when a move is found
		while(true){
			try {
				answer = (Point) ois.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
