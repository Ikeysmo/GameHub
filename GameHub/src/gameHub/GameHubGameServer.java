package gameHub;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/*This server opens up a port, and listens for connections to relay GAME DATA!
 * This has methods to find out who's online, and handle it accordingly. Authoratitive, GameHubServer only offers/request matches
 */
public class GameHubGameServer implements Runnable {
	private int gamePortNumber = 2021; //for gaming
	private ServerSocket ss;
	private ConcurrentHashMap<String, String> matches = new ConcurrentHashMap<String,String>(); //Collection of who's in matches with who
	private ConcurrentHashMap<String, ObjectOutputStream> onlineList = new ConcurrentHashMap<String, ObjectOutputStream>(); //Collection of who's currently logged in/Online
	private GameHubServer GHS;

	public GameHubGameServer(GameHubServer e) throws IOException {
		//listens on port
		ss = new ServerSocket(gamePortNumber);
		GHS = e;
		new Thread(this).start();
	}
	public boolean addMatch(String p1, String p2){ //checks to make sure user isn't in another game too!
		if(matches.containsKey(p1.toUpperCase()) || matches.containsKey(p2.toUpperCase()))
			return false;
		else{
			matches.put(p1.toUpperCase(), p2.toUpperCase()); //adds both players to match
			matches.put(p2.toUpperCase(), p1.toUpperCase());
			System.out.println("Success in adding players to match");
			return true; //success
		}
	}

	public boolean removeMatch(String username){
		//remove
		onlineList.remove(username.toUpperCase()); //remove from list
		String user2 = matches.get(username.toUpperCase()); //get second player in match
		matches.remove(username.toUpperCase()); //remove both players from match
		matches.remove(user2);
		return true;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		Socket s = null; //initialize local pointers
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		String firstMessage = null;
		String username = null;
		boolean nextClientThreadStarted = false; //this is incase a thread fails before creating a new thread to listen

		try {
			s = ss.accept(); //accept new connection
			new Thread(this).start(); //make another thread immediately
			nextClientThreadStarted = true; //let know that a thread has been created
			System.out.println("Connection to gameHubGameServer from " + s.getInetAddress());
			oos = new ObjectOutputStream(s.getOutputStream()); // prepare to send
			ois = new ObjectInputStream (s.getInputStream());  // prepare to receive

			firstMessage = (String) ois.readObject(); // 1st msg from client must be String username
			username = firstMessage.toString().toUpperCase();
			System.out.println("Received 1st message (GGHS): " + firstMessage);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Initial connect failure: " + e);
			try {s.close();}           // try to hang up
			catch(Exception ioe){}     // s already terminated!
			if (!nextClientThreadStarted) // make thread for next client if accept() failed
				new Thread(this).start(); // but not if a later operation failed.
			return;
		} 
		
		//see if matches are there?
		if(matches == null){
			System.out.println("Red bananansanananans");
			return;
		}
		if(!matches.containsKey(firstMessage.toUpperCase())){ //if opponent isn't in matches, then close thread and maybe socket too!
			System.out.println("Blue bananansnssnanss from " + firstMessage);
			return;
		}
		onlineList.put(firstMessage.toUpperCase(), oos); //add to list!
		/*Assumes  person is truly in the match */
		try{
			while(true){
				System.out.println("Ever enter here: Dies");
				Object messageFromClient = ois.readObject();//wait for something
				System.out.println("(GHS) Receieved tic tac toe bruh");
				if(messageFromClient instanceof String){
					String tempMessage = (String) messageFromClient;
					//User1ExitUser2
					if(tempMessage.contains("Exit")){ //if one of the people in the match sends Exit, then trying to leave match. Handle accordingly
						tempMessage.replace("Exit", " ");
						tempMessage = tempMessage.trim();
						String user1 = tempMessage.substring(tempMessage.indexOf(" "));
						String user2 = tempMessage.substring(0, tempMessage.indexOf(' '));
						user1 = user1.trim().toUpperCase();
						user2 = user2.trim().toUpperCase();
						System.out.println("Removing users");
						matches.remove(user1);
						matches.remove(user2);
					}
				}
				else{
					//if I can find my own name in match, send it to the guy i'm linked to
					System.out.println("Got something tic tac toe related");
					if(matches.containsKey(username)){
						//I'm in a match
						send(messageFromClient, matches.get(username)); //send to one guy
					}
				}
			}

		}
		catch( ClassNotFoundException | IOException de){
			System.out.println("hey someone just left... should remove!");}
	}
	private void send(Object messageFromClient, String username) {
		//onlineList = GHS.getOnlineList(); //use own, local version!
		ObjectOutputStream temp = onlineList.get(username);
		try {
			temp.writeObject(messageFromClient);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
