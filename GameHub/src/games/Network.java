package games;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Network implements Runnable{
	public String ip_address = "localhost";
	public int port = 2020; 
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Network_Handler net; 
	
	public Network(){
		//initialize things.. don't want it to connect automatically I'm sure...
	}
	
	public void connect() throws Exception{
		@SuppressWarnings("resource") //this is because of the socket not "closed" warning
		Socket s = new Socket(ip_address, port);
		oos = new ObjectOutputStream(s.getOutputStream());
		ois = new ObjectInputStream(s.getInputStream());
		/* This is where the protocol of interacting with gamehubgameserver
		 * gets implemented automatically
		 */
		new Thread(this).start(); //begin thread for listening
	}
	
	public void disconnect(){
		try {
			oos.close();
		} catch (IOException e) {
			//likely the stream is already closed, and ignore error!
		}
	}
	public void requestResources(){
	}
	
	public void set_network_handler(Network_Handler handle){
		this.net = handle;
	}
	public void send(Object sed) throws IOException{
		oos.writeObject(sed);
	}

	public void run(){
		
		try{
			while(true){
			Object data = ois.readByte();
			net.Handle_Data(data); // let's the user application deal with the data
			} //if there are any disconnections, the server has to relay information!!! 
		}
		catch(IOException e){} //something happened, do something
		
	}
	
}
