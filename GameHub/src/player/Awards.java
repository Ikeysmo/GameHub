package player;

public class Awards {
	
	//instance variables
	/* Name of the Award */
	private String name;
	/* Game where award was given */
	private String game;
	
	public Awards(String name, String game) {
		this.name = name;
		this.game = game;
	}
	
	public String getName() {
		return name;
	}
	
	public String getGame() {
		return game;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setGame(String game) {
		this.game = game;
	}
	
	public String toString() {
		String s = "";
		s += "Award name: " + name + "\n";
		s += "Game name:  " + game + "\n";
		return s;
	}

}
