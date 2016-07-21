package brickBreaker;

import games.Game;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import player.Player;

public class BrickBreaker extends Game {
	
	public BrickBreaker() {
		super("BrickBreaker", "brickBreaker.png", new JFrame(), new JPanel(), 500, 500, 200, 200, 2);
		getGameFrame().setVisible(true);
	}
	
	public BrickBreaker(String localplayer, String remoteplayer, boolean goFirst, String ipaddress, Boolean gofirst) throws UnknownHostException, IOException {
		super("BrickBreaker", "brickBreaker.png", new JFrame(), new JPanel(), 500, 500, 200, 200, 2);
	}

	public static void main(String[] args) {
		new BrickBreaker();

	}

	@Override
	public boolean updateMove(int numx, int numy, Player p1) {
		// TODO Auto-generated method stub
		return false;
	}

}
