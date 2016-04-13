package gameHub;
import java.net.ConnectException;

public class PlayerAccount {
	String username;
	String password;
	Player temp; //hold reference to all the different type of players for instance games.
	
	public PlayerAccount() {
		// TODO Auto-generated constructor stub
	}

	public void getOnline() throws ConnectException{
		//throw a connection if fails... then sign offline!
	}
	
	public int getScore(){
		return 0;
		
	}
	
	public String getUsername(){
		return username;
	}
}
