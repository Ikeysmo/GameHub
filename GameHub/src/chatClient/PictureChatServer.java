package chatClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
//does not close out old session if new person joins!!!
public class PictureChatServer implements Runnable {
	ConcurrentHashMap<String, ObjectOutputStream> whosIn = new ConcurrentHashMap<String, ObjectOutputStream>();
	ConcurrentHashMap<String, String> passwords = new ConcurrentHashMap<String, String>();
	private int portNumber = 6666; //wala
	private ServerSocket ss;
	private Vector<String> rejoiningClients = new Vector<String>(); //vector intended!
	public PictureChatServer() throws IOException {
		System.out.println("Isaiah Smoak - ECE 309");
		// TODO Auto-generated constructor stub
		ss = new ServerSocket(portNumber);
		System.out.println(ss.getInetAddress());
		//run(); //or do I put this in my own thread
		new Thread(this).start();
		
	}

	private synchronized void sendToAll(Object message){
		//
		ObjectOutputStream[] oosArray = whosIn.values().toArray(new ObjectOutputStream[0]);
		for(ObjectOutputStream clientOOS : oosArray){
			try {
				clientOOS.writeObject(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		
		//initial connection
		try {
			s = ss.accept();
			new Thread(this).start(); //make another thread
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
		}
		//now handle the first message to accept/decline request!
		System.out.println(firstMessage);
		//part 10 is below
		if(firstMessage.indexOf("/")>0){ //makes sure it has '/'
			String chatName = firstMessage.substring(0, firstMessage.indexOf("/"));
			password = firstMessage.substring(firstMessage.indexOf("/") + 1);//done with this part
			chatName = chatName.toUpperCase();
			if(!(isLegal(chatName) && isLegal(password))){ //handles spaces,slashes and length
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
			//finishes checking if legal username/password! 
	
			//Now handle joining processing:
			//CHECK IF CHATNAME EXIST FOR PASSWORD NEW PERSON
			if(!passwords.containsKey(chatName)){
				passwords.put(chatName, password);
				whosIn.put(chatName, oos);
				//ADDS THE PERSON TO LIST!
			} 
			
			//join client! which is part 3!
			//else the chat name exist?
			//ELSE CHAT NAME EXISTS
			else{
				String storedPassword = passwords.get(chatName); //get password for chatname
				if(!password.equals(storedPassword)){ //IF PASSWORD DOESN'T MATCH CHAT NAME
					try { //send it back to them
						oos.writeObject("Error! The password: "+ password+ " is incorrect!");
						s.close();
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} //handles if passwords don't match
				else{ //PASSWORD MATCHES USERNAME
					if(!whosIn.containsKey(chatName)){ //IF NOT IN WHOS IN LIST, ADD HIM TO IT!
						whosIn.put(chatName,  oos); //then go to part 3
					}
					//else IF CORRECT USERNAME/PASSWORD, AND ALSO IN WHO'S IN LIST... rejoining???
					else{
						System.out.println("This guy is rejoining");
						rejoiningClients.add(chatName); //needed to add this person to rejoin list!!
						ObjectOutputStream chatOOS = whosIn.get(chatName);
						//
						try {
							chatOOS.writeObject("Session being terminated due to rejoin at another location.");
							chatOOS.close(); //nothing wrong with this??
						}
						catch(IOException e){}
						
						whosIn.replace(chatName, oos); //should close the other client!!!	
						//DON'T WANT TO RETURN... THIS IS THE CORRECT ONE!
					}
				}
			}
			 //finishes part 11??
			//part 13
			
//			if(rejoiningClients.contains(chatName)){
//				System.out.println("I luvvvv blueberries!");
//				//this is where I will know to rejoin... 
//				//remove the rejoining person
//			}
			
			//this is where to handle rejoining!!!

			try{
				oos.writeObject("Welcome " + chatName);
				if(!rejoiningClients.contains(chatName)){
					sendToAll("Welcome " + chatName); //don't send to all!
					System.out.println(chatName + " is joining"); //may eliminatej
				}
				//whosIn.put(chatName, oos); //have to test rejoin from new location
				//do
				String[] chatNameLists = whosIn.keySet().toArray(new String[0]);
				//use same code here!
				sendToAll(chatNameLists);
				//end it here!
			}
			catch (Exception e){
				//do nothing!
				System.out.println("Superman is ERROR!");
			}
			try{
				while(true){
					Object messageFromClient = ois.readObject();//wait for MY client to say something
					System.out.println("Received '" + messageFromClient + "' from " + chatName); // (debug trace)
					if (messageFromClient instanceof String)
						sendToAll(chatName + " says: " + messageFromClient);
					else
						sendToAll(messageFromClient); // send some not-a-text-message object to all clients!
				}
			} //try to read from socket
			catch(Exception e){ //catches a failure to read!
				//oos.writeObject(chatName + " is leaving the chat room");
				//sendToAll(message);
				if(rejoiningClients.contains(chatName)){
					System.out.println("not printing my guy left the chat room!");
					rejoiningClients.remove(chatName);
					try {
						s.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					whosIn.remove(chatName);
					sendToAll("Goodbye to " + chatName + " who has just left the chat room.");
					System.out.println(chatName+ " is leaving the chat room");
					try {
						s.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
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
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if(args.length != 0){
			System.out.println("The program does not take arguments!");
			
		}
		new PictureChatServer();
	}

}
