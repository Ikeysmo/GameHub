package gameHub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class GameHubServer implements Runnable{
	ConcurrentHashMap<String, ObjectOutputStream> onlineList = new ConcurrentHashMap<String, ObjectOutputStream>();
	ConcurrentHashMap<String, String> passwords = new ConcurrentHashMap<String, String>();
	private int portNumber = 2020; //wala
	private ServerSocket ss;
	
	public GameHubServer() throws IOException {
		// TODO Auto-generated constructor stub
		ss = new ServerSocket(2020);
		System.out.println(ss.getInetAddress());
		try{
			FileInputStream fis = new FileInputStream("Accounts.data");
			ObjectInputStream ois = new ObjectInputStream(fis);
			passwords = (ConcurrentHashMap<String,String>) ois.readObject();
			ois.close();
			fis.close();
		}
		catch(FileNotFoundException | ClassNotFoundException e){
			FileOutputStream fos = new FileOutputStream("Accounts.data");
			fos.close();
		}
		
		
		
		new Thread(this).start();
		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new GameHubServer();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//Each client thread we make enters here!
		Socket s = null; //initialize local pointers
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		String firstMessage = null;
		String password = null;
		boolean nextClientThreadStarted = false;
		
		try {
			s = ss.accept();
			new Thread(this).start(); //make thread
			nextClientThreadStarted = true;
			System.out.println("Connection from " + s.getInetAddress());
			
			ois = new ObjectInputStream (s.getInputStream());  // prepare to receive
			firstMessage = (String) ois.readObject(); // 1st msg from client must be String
			System.out.println("Received 1st message: " + firstMessage);
			oos = new ObjectOutputStream(s.getOutputStream()); // prepare to send
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Initial connect failure: " + e);
			 try {s.close();}           // try to hang up
		        catch(Exception ioe){}     // s already terminated!
		     if (!nextClientThreadStarted) // make thread for next client if accept() failed
		         new Thread(this).start(); // but not if a later operation failed.
			return;
		} //waiting for client to call
		System.out.println(firstMessage);
		//part 10 is below
		if(firstMessage.indexOf("/")>0){ //makes sure it has '/'
			String userName = firstMessage.substring(0, firstMessage.indexOf("/"));
			password = firstMessage.substring(firstMessage.indexOf("/") + 1);//done with this part
			userName = userName.toUpperCase();
			if(!(isLegal(userName) && isLegal(password))){ //handles spaces,slashes and length
				//something isn't legal! swear words are SKIPPED!!!
				try {
					oos.writeObject("Invalid format"); //generic message sent
					System.out.println("Failed to Join! Client didn't enter valid characters!");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try { //10.2 close!
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return; //10.3 end this thread!
			}
			//part 12!
			if(!passwords.containsKey(userName)){
				passwords.put(userName, password);
				onlineList.put(userName, oos);
				FileOutputStream fos;
				try {
					fos = new FileOutputStream("Accounts.data");
					ObjectOutputStream oss = new ObjectOutputStream(fos);
					oss.writeObject(passwords);
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//break;
			}
			//join client! which is part 3!
			//else of sorts
			else{
				String storedPassword = passwords.get(userName);
				if(!password.equals(storedPassword)){
					try { //send it back to them
						oos.writeObject("Error! The password: "+ password+ " is incorrect!");
						s.close();
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} //handles if passwords don't match
				else{
					if(!onlineList.containsKey(userName)){
						onlineList.put(userName,  oos); //then go to part 3
					}
					//else... rejoining???
				}
			}
			 //finishes part 11??
			//part 13
			System.out.println(userName + " is joining");
			try{
				oos.writeObject("Welcome " + userName);
				sendToAll("Welcome " + userName);
				onlineList.put(userName, oos); //have to test rejoin from new location
				//do
				String[] chatNameLists = onlineList.keySet().toArray(new String[0]);
				//use same code here!
				sendToAll(chatNameLists);
				//end it here!
			}
			catch (Exception e){
				//do nothing!
			}
			try{
			while(true){
				Object messageFromClient = ois.readObject();//wait for MY client to say something
				System.out.println("Received '" + messageFromClient + "' from " + userName); // (debug trace)
				if (messageFromClient instanceof ChatMessage)
				    sendToAll(userName + " says: " + messageFromClient);
				else if(messageFromClient instanceof GameInvite)
				    sendToAll(messageFromClient); // send some not-a-text-message object to all clients!
			}
			}
		    catch(Exception e){
		    	//oos.writeObject(chatName + " is leaving the chat room");
				//sendToAll(message);
				onlineList.remove(userName);
				sendToAll("Goodbye to " + userName + " who has just left the chat room.");
		    	
		    }
		}//now go to part 3?

	}
	private boolean isLegal(String word){
		if(word.contains(" ") || word.contains("/"))
			return false;
		else if(!(word.length() > 0 && word.length() <= 32))
			return false;
		//do SWEAR words
		else
			return true;
	}
	
	private synchronized void sendToAll(Object message){
		//
		ObjectOutputStream[] oosArray = onlineList.values().toArray(new ObjectOutputStream[0]);
		for(ObjectOutputStream clientOOS : oosArray){
			try {
				clientOOS.writeObject(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
