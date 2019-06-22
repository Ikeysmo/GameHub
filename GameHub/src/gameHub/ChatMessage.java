package gameHub;

/**
 * The class for chat messages
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	/* The message to be sent */
	String message;
	/* The date that the message is sent */
	Date date;
	/* Who the message is from */
	String from;
	/* Who the message is to */
	String to;
	
	/**
	 * The constructor of the chat message
	 * 
	 * @param message The message being sent
	 * @parma from Who the message is from
	 * @param to Who the message is to
	 */
	public ChatMessage(String message, String from, String to) {
		this.message = message + System.lineSeparator();
		this.from = from;
		this.to= to;
		this.date = new Date();
	}
	
	/**
	 * The toString method for Chat messages
	 * 
	 * @return The message being sent
	 */
	public String toString(){
		return message;
		
	}

}
