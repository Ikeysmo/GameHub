package templateGame;

import javax.swing.JFrame;
import javax.swing.JPanel;

import player.Player;
import games.Game;

public class Template extends Game {

	
	public Template() {
		super("Tic Tac Toe", "ticTacToeIcon.gif", new JFrame(), new JPanel(), 700, 700, 500, 500, 2);
		this.getGameFrame().setVisible(true);
	}
	
	public static void main(String args[]) {
		new Template();
	}

	@Override
	public boolean updateMove(int numx, int numy, Player p1) {
		// TODO Auto-generated method stub
		return false;
	}
}
