package triviaGame;

import games.Game;

import javax.swing.JFrame;
import javax.swing.JPanel;

import player.Player;

/*This game will have multiple types of brain teasers to work with */
public class Trivia extends Game {

	public Trivia() {
		super("Trivia Game", "triviaGame.png", new JFrame(), new JPanel(), 500, 500, 200, 200, 2);
		getGameFrame().setVisible(true);
	}

	public static void main(String[] args) {
		new Trivia();

	}

	@Override
	public boolean updateMove(int numx, int numy, Player p1) {
		// TODO Auto-generated method stub
		return false;
	}

}
