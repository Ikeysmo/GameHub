package player;

public class HighScores {
	//instance variables
	
	/* scores */
	private int[] scores;
	/* players who have the scores */
	private String[] players;
	/* game name */
	private String game;
	
	public HighScores(String game) {
		this.game = game;
		
		scores = new int[10];
		players = new String[10];
		
		//Set defaults for score board
		for(int i = 0; i < 10; i++) {
			scores[i] = 0;
			players[i] = "Nah";
		}
	}
	
	//Getters
	public int[] getScores() {
		return scores;
	}
	
	public String[] getPlayers() {
		return players;
	}
	
	public String getGameName() {
		return game;
	}
	
	//Setters
	public void setGameName(String game) {
		this.game = game;
	}
	
	//NO seter for scores or players (can't set arrays, only edit)
	
	//-------------------------------
	//Helper Methods
	//-------------------------------
	public void setNewScore(String player, int score) {
		
	}

}
