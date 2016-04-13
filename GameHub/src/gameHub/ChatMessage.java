package gameHub;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable{
	String message;
	Date date;
	String from;
	String to;
	public ChatMessage(String message, String from, String to) {
		// TODO Auto-generated constructor stub
		this.message = message;
		this.from = from;
		this.to= to;
		this.date = new Date();
	}

}
