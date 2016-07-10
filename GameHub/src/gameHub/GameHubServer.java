package gameHub;

/**
 * The Gamehub Server
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class GameHubServer implements Runnable{
	ConcurrentHashMap<String, ObjectOutputStream> onlineList = new ConcurrentHashMap<String, ObjectOutputStream>(); //Collection of who's currently logged in/Online
	ConcurrentHashMap<String, String> passwords = new ConcurrentHashMap<String, String>(); //Collection of passwords
	private int portNumber = 2020; //port number
	private ServerSocket ss;
	private final String COMMANDERROR = "ERROR! Invalid Command!";
	private final String ADMIN_PASS = "ENGINEER";
	private ConcurrentHashMap<String, String> matches = new ConcurrentHashMap<String, String>(); //Collection of who's in matches with who
	private GameHubGameServer gameServer;
	public GameHubServer() throws IOException {
		System.out.println("-------GAMEHUB SERVER-------\n");
		System.out.println("----------------------------\n");
		
		ss = new ServerSocket(portNumber);
		gameServer = new GameHubGameServer(this);
		//new GameHubWebServer(); //set up webserver!
		ss.getInetAddress(); //for static reasons
		System.out.println("HOST ADDRESS: \n" + InetAddress.getLocalHost().getHostAddress());
		System.out.println("----------------------------\n");
		System.out.println("(Type \"help\" for server commands)");
		Thread t = new Thread(new Runnable(){
			public void run(){
				InputStreamReader cin = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(cin);
				while(true){
					String command;
					try {
						command = br.readLine();
						//System.out.println("Recieved: " + command);
						command = command.trim();
						command = command.toUpperCase();
						String[] command_array = command.split(" ");
						//System.out.println(Arrays.toString(command_array));
						switch (command_array[0]){
						case "HELP":
							System.out.println("Here are commands:\n----------------------------\n*get ipaddress\n*add username [username]\n*remove username [username]\n*get online_list\n*get password [username] [admin_password]\n*remove game [username]\n");
							break;
						case "GET":
							if(command_array.length < 1){
								System.out.println(COMMANDERROR);
								break;}
							switch(command_array[1]){
							case "IPADDRESS":
								System.out.println("SUCCESS: IP Address is " + InetAddress.getLocalHost().getHostAddress());
								break;
							case "ONLINE_LIST":
								//return online list
								if(onlineList.isEmpty())
									System.out.println("SUCCESS: Sorry, no one is online!");
								else{
									//ConcurrentHashMap<String, ObjectOutputStream> list =(ConcurrentHashMap<String, ObjectOutputStream>) onlineList;
									Vector<String> onlist = new Vector<String>();
									for(Entry k : onlineList.entrySet()){
										onlist.addElement((String) k.getKey());
									}
									System.out.println("Online List\n--------------------------");
									Object[] conn_list = onlist.toArray();
									for(int i = 0; i < conn_list.length; i++)
										System.out.println(conn_list[i]);
									System.out.println("--------------------------\nFinished printing online list!");
								}
								break;
							case "PASSWORD": //did not implement the developer password yet
								if(command_array.length == 3){
									String user_search = command_array[2];
									String retr_password = passwords.get(user_search);
									if(retr_password == null)
										System.out.println("ERROR! Username does not exist!");
									else
										System.out.println("SUCCESS: " + user_search + " password is " + passwords.get(user_search));
								}
								else
									System.out.println(COMMANDERROR);
								//try to find the person, if he isn't there, scream problem!
								break;
							default:
								System.out.println(COMMANDERROR);
							}
							break;
						case "ADD":
							switch(command_array[1]){
							case "ACCOUNT": 
								if(command_array.length == 4){
									// 2 and 3 are the username/password
									if(passwords.get(command_array[1]) == null){
										passwords.put(command_array[2], command_array[3]);
										System.out.println("SUCCESS: Account was successfully added!");
									}
									else{
										System.out.println("SUCCESS: Sorry, user already exists. Please remove then add!");
									}
								}
								else
									System.out.println(COMMANDERROR);
								break;
							default:
								System.out.println(COMMANDERROR);
							}
							break;
						case "REMOVE":
							switch(command_array[1]){
						
							case "USERNAME": 
								if(command_array.length == 4){
									if(command_array[3].equals(ADMIN_PASS)){
										//search for username
										String check = passwords.remove(command_array[2]);
										if(check == null)
											System.out.println("The username does not exist!");
										else
											System.out.println("Successfully removed username!");
									}
									else
										System.out.println("AUTHENTICATION FAILURE! INCORRECT ADMIN PASSWORD!");
								}
								else
									System.out.println(COMMANDERROR);
								break;
							case "GAME": //
								System.out.println("This is not implemented yet!");
								break;
							default:
								System.out.println(COMMANDERROR);
							}
							break;
						case "EXIT":
							System.out.println("Exit command is not implemented yet!");
							break;
						default:
							System.out.println(COMMANDERROR);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			}
		});
		t.start();
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
		new GameHubServer();
	}

	@Override
	public void run() {
		//Each client listener thread we make enters here!
		Socket s = null; //initialize local pointers
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		String firstMessage = null;
		String userName = null;
		String password = null;
		boolean nextClientThreadStarted = false; //this is incase a thread fails before creating a new thread to listen
		boolean newAccount = false;
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
			userName = firstMessage.substring(0, firstMessage.indexOf("/"));
			password = firstMessage.substring(firstMessage.indexOf("/") + 1);//done with this part
			if(userName.contains("*")){
				newAccount = true;
				userName = userName.replace("*", "");
			}
			userName = userName.toUpperCase();

			//now see if username/password is legal and matches 
			if(!(isLegal(userName) && isLegal(password))){ //handles spaces,slashes and length
				//something isn't legal! swear words are SKIPPED!!!
				try {
					oos.writeObject("Invalid format"); //generic message sent
					System.out.println("Failed to Join! Client didn't enter valid characters!");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try { //10.2 close!
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return; //
			}

			if(!passwords.containsKey(userName)){ //if doesn't have username, it is a new account, add it to the lists, and also save new entry
				if(newAccount){
					passwords.put(userName, password);
					onlineList.put(userName, oos);
					FileOutputStream fos;
					try {
						fos = new FileOutputStream("Accounts.data");
						ObjectOutputStream oss = new ObjectOutputStream(fos);
						oss.writeObject(passwords);
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{//not a new account, can't do this!
					try {
						oos.writeObject("Error! The password: "+ password+ " is incorrect!"); //generic message sent
						System.out.println("Failed to Join! Username doesn't exist");
						s.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					return; //

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
				sendToAll("Everybody Welcome " + userName);
				oos.writeObject("Welcome " + userName);
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
					
					//This is for if any requests to leave the server are made
					if(messageFromClient.equals("0x000000")) {
						onlineList.remove(userName);
						sendToAll("Goodbye to " + userName + " who has just left the chat room.");
						String[] chatNameLists = onlineList.keySet().toArray(new String[0]);
						sendToAll(chatNameLists);
						s.close();
						break;
					}
					
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
							gameServer.addMatch(invite.from.toUpperCase(), invite.to.toUpperCase());
							send(invite, invite.from); //send it one more time!
							//matches.put(invite.from.toUpperCase(), invite.to.toUpperCase()); //add Client 1 to matches, that points to Client 2
							//matches.put(invite.to.toUpperCase(), invite.from.toUpperCase()); //add Client 2 to matches, that points to Client 1
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
						gameServer.removeMatch(userName);
					}
					else{
						//if I can find my own name in match, send it to the guy i'm linked to
						System.out.println("Got something tic tac toe related");
						if(matches.containsKey(userName)){
							//I'm in a match
							send(messageFromClient, matches.get(userName)); //send to one guy
						}
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();

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
				e.printStackTrace();
			}
		}
	}
	private synchronized void send(Object message, String username){ //this sends message to one person 
		ObjectOutputStream temp = onlineList.get(username.toUpperCase());
		if(temp == null)
			System.out.println("What's wrong?");
		try {
			temp.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

}
