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
	ConcurrentHashMap<String, ObjectOutputStream> onlineList = new ConcurrentHashMap<String, ObjectOutputStream>(); //Collection of who's currently logged in/Online
	ConcurrentHashMap<String, String> passwords = new ConcurrentHashMap<String, String>(); //Collection of passwords
	private int portNumber = 2020; //port number
	private ServerSocket ss;
	private ConcurrentHashMap<String, String> matches = new ConcurrentHashMap<String, String>(); //Collection of who's in matches with who
	public GameHubServer() throws IOException {
	
		ss = new ServerSocket(2020);
		System.out.println(ss.getInetAddress());
		//try to retrieve a saved list of everyone who's ever registered
		try{ 
			FileInputStream fis = new FileInputStream("Accounts.data");
			ObjectInputStream ois = new ObjectInputStream(fis);
			passwords = (ConcurrentHashMap<String,String>) ois.readObject();
			ois.close();
			fis.close();
		}
		catch(FileNotFoundException | ClassNotFoundException e){
			FileOutputStream fos = new FileOutputStream("Accounts.data"); //File not found, created one
			fos.close();
		}



		new Thread(this).start(); //start thread(s) for listening

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new GameHubServer();
	}

	@Override
	public void run() {
		//Each client listener thread we make enters here!
		Socket s = null; //initialize local pointers
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		String firstMessage = null;
		String password = null;
		boolean nextClientThreadStarted = false; //this is incase a thread fails before creating a new thread to listen

		try {
			s = ss.accept(); //accept new connection
			new Thread(this).start(); //make another thread
			nextClientThreadStarted = true; //let know that a thread has been created
			System.out.println("Connection from " + s.getInetAddress());

			ois = new ObjectInputStream (s.getInputStream());  // prepare to receive
			firstMessage = (String) ois.readObject(); // 1st msg from client must be String username/password
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
		//Parse through firstMessage recieved to see if it matches username/password
		if(firstMessage.indexOf("/")>0){ //makes sure it has '/'
			String userName = firstMessage.substring(0, firstMessage.indexOf("/"));
			password = firstMessage.substring(firstMessage.indexOf("/") + 1);//done with this part
			userName = userName.toUpperCase();
			
			//now see if username/password is legal and matches 
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
				return; //
			}
			
			if(!passwords.containsKey(userName)){ //if doesn't have username, it is a new account, add it to the lists, and also save new entry
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
			//join client! 
			//else... now we see if username and password entered matches password for username
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
					if (messageFromClient instanceof ChatMessage){
						ChatMessage chat = (ChatMessage) messageFromClient;
						System.out.println("Hey you've got mail!");
						sendToAll(chat);	
					}
					else if(messageFromClient instanceof GameInvite){
						System.out.println("Forwarding invite");
						GameInvite invite = (GameInvite) messageFromClient;
						if(invite.isAccepted() && invite.isChecked()){
							//create a new match!
							System.out.println("Creating match!");
							//matches is added twice because of how concurrentHashMaps work. Needs to be substituted for likely multidimensional vector
							matches.put(invite.from.toUpperCase(), invite.to.toUpperCase()); //add Client 1 to matches, that points to Client 2
							matches.put(invite.to.toUpperCase(), invite.from.toUpperCase()); //add Client 2 to matches, that points to Client 1
						}
						else if(!invite.isChecked() || !invite.isAccepted()){ //send it to the person if denied or not checked!
							sendToAll(messageFromClient); // send some not-a-text-message object to all clients! It generally won't be sent to all
						}
					}
					else if(messageFromClient instanceof String){
						String tempMessage = (String) messageFromClient;
						//User1ExitUser2
						if(tempMessage.contains("Exit")); //if one of the people in the match sends Exit, then trying to leave match. Handle accordingly
						tempMessage.replace("Exit", " ");
						tempMessage = tempMessage.trim();
						String user1 = tempMessage.substring(tempMessage.indexOf(" "));
						String user2 = tempMessage.substring(0, tempMessage.indexOf(' '));
						user1 = user1.trim().toUpperCase();
						user2 = user2.trim().toUpperCase();
						matches.remove(user1);
						matches.remove(user2);
					}
					else{
						//if I can find my own name in match, send it to the guy i'm linked to
						if(matches.containsKey(userName)){
							//I'm in a match
							send(messageFromClient, matches.get(userName)); //send to one guy
						}
					}
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
		//sends message to everybody
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
	private synchronized void send(Object message, String username){ //this sends message to one person 
		ObjectOutputStream temp = onlineList.get(username);
		try {
			temp.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
