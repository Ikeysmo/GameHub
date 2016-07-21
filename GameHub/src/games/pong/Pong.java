package games.pong;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import player.Player;
import games.Game;

public class Pong extends Game {
	
	public Pong() {
		super("Pong", "pong.png", new JFrame(), new JPanel(), 500, 500, 200, 200, 2);
		getGameFrame().setVisible(true);
	}
	
	public Pong(String localplayer, String remoteplayer, boolean goFirst, String ipaddress, Boolean gofirst) throws UnknownHostException, IOException {
		super("Pong", "pong.png", new JFrame(), new JPanel(), 500, 500, 200, 200, 2);
	}

	public static void main(String[] args) {
		new Pong();

	}

	@Override
	public boolean updateMove(int numx, int numy, Player p1) {
		// TODO Auto-generated method stub
		return false;
	}

}
