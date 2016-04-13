package gameHub;

import java.io.Serializable;

public class GameInvite implements Serializable{
	String from;
	String to;
	String game;
	private boolean accepted;
	private boolean checked = false; //this goes true if it's been checked at any point!
	
	public void Accept(){
		accepted = true;
		checked = true;
	}
	public void Deny(){
		accepted = false;
		checked = true;
	}
	public boolean isAccepted(){
		return accepted;
	}
	
	public boolean isChecked(){
		return checked;
	}
	public GameInvite(String from, String to, String game) {
		// TODO Auto-generated constructor stub
		this.from = from;
		this.to= to;
		this.game = game;
	}

}
