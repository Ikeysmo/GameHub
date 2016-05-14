package connectFour;

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

import ticTacToe.TicTacToe;

public class RemotePlayer extends ConnectFourPlayer implements Runnable {
	int port = 2000;
	String ip_address;
	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	public ConnectFour tic;
	
	public RemotePlayer() {
		
	}
	
	public RemotePlayer(String ipAddress, ConnectFour tic, ConnectFourPlayer op) {
		this.tic = tic;
		
		ip_address = ipAddress;
		try {
			s = new Socket();
			s.connect(new InetSocketAddress(ip_address, port), 500);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			oos.writeObject(op.name);
			name = (String) ois.readObject();
			tic.turn = 1;
			piece = 'O';
			//player 2	
		}
		
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Couldn't connect.. beginning server side!");
			try {//if failed, try hosting! and become player 1
				tic.turn = 0;
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

	public RemotePlayer(ObjectOutputStream oos2, ObjectInputStream ois2, ConnectFour tic, String playername, char piece) {
		// Player name represents the opposing online player's account. Gamehub Login should have this!
		ois = ois2; //input stream
		oos = oos2; //output stream to server
		this.tic = tic; //this is reference to tic tac toe to call things, set turn
		this.name = playername; 
		this.piece = piece;
	}
	
	@Override
	public Point makeMove() throws IOException {
		// TODO Auto-generated method stub
		try {
			Point answer;
			while(true){
				 answer = (Point)ois.readObject();
				 if(tic == null)
					 System.out.println("why");
				 else if(answer == null){
					 System.out.println("bugg");
				 }
				 if(tic.updateMove(answer.x, answer.y, this)) //if this is true, valid move!
					break;
			}
			return answer;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateAll(Point p) throws IOException{
		//update everything
		oos.writeObject(p);
		
	}

	
	public void run(){
		//recieving...
		Point answer;
		while(true){
			try {
				answer = (Point) ois.readObject();
				System.out.println(answer);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
}
