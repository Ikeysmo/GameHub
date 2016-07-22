package games;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import player.Player;

public abstract class Game extends JPanel{
	
	public final static Color saddleBrown = new Color(139, 69, 19);
	
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();
	/* The height of the end frame */
	public final static int END_FRAME_HEIGHT = 150;
	/* The width of the end frame */
	public final static int END_FRAME_WIDTH = 500;
	
	private int frameHeight;
	private int frameWidth;
	private int minFrameHeight;
	private int minFrameWidth;
	private int maxFrameHeight;
	private int maxFrameWidth;
	private JFrame gameFrame;
	private JPanel gamePanel;
	private Player players[];
	private int playerNum;
	
	public Game(String gameTitle,
			String gameIcon,
			JFrame gameFrame,
			JPanel gamePanel,
			int frameHeight,
			int frameWidth,
			int minFrameHeight,
			int minFrameWidth,
			int playerNum)
		{
		this.frameHeight = frameHeight;
		this.frameWidth = frameWidth;
		this.minFrameHeight = minFrameHeight;
		this.minFrameWidth = minFrameWidth;
		this.players = new Player[2];
		
		MenuBar menuBar = new MenuBar();
		
		Menu menu1 = new Menu();
		menu1.setName("Player");
		menu1.setLabel("Player");
		MenuItem menuItem11 = new MenuItem();
		menuItem11.setName("Name");
		menuItem11.setLabel("Name");
		menu1.add(menuItem11);
		MenuItem menuItem12 = new MenuItem();
		menuItem12.setName("Awards");
		menuItem12.setLabel("Awards");
		menu1.add(menuItem12);
		MenuItem menuItem13 = new MenuItem();
		menuItem13.setName("Data");
		menuItem13.setLabel("Data");
		menu1.add(menuItem13);
		MenuItem menuItem14 = new MenuItem();
		menuItem14.setName("HighScores");
		menuItem14.setLabel("HighScores");
		menu1.add(menuItem14);
		MenuItem menuItem15 = new MenuItem();
		menuItem15.setName("Trophies");
		menuItem15.setLabel("Trophies");
		menu1.add(menuItem15);
		
		Menu menu2 = new Menu();
		menu2.setName("Load");
		menu2.setLabel("Load");
		MenuItem menuItem21 = new MenuItem();
		menuItem21.setName("Game1");
		menuItem21.setLabel("Game1");
		menu2.add(menuItem21);
		MenuItem menuItem22 = new MenuItem();
		menuItem22.setName("Game2");
		menuItem22.setLabel("Game2");
		menu2.add(menuItem22);
		MenuItem menuItem23 = new MenuItem();
		menuItem23.setName("Game3");
		menuItem23.setLabel("Game3");
		menu2.add(menuItem23);
		MenuItem menuItem24 = new MenuItem();
		menuItem24.setName("Game4");
		menuItem24.setLabel("Game4");
		menu2.add(menuItem24);
		
		Menu menu3 = new Menu();
		menu3.setName("Save");
		menu3.setLabel("Save");
		MenuItem menuItem31 = new MenuItem();
		menuItem31.setName("Game1");
		menuItem31.setLabel("Game1");
		menu3.add(menuItem31);
		MenuItem menuItem32 = new MenuItem();
		menuItem32.setName("Game2");
		menuItem32.setLabel("Game2");
		menu3.add(menuItem32);
		MenuItem menuItem33 = new MenuItem();
		menuItem33.setName("Game3");
		menuItem33.setLabel("Game3");
		menu3.add(menuItem33);
		MenuItem menuItem34 = new MenuItem();
		menuItem34.setName("Game4");
		menuItem34.setLabel("Game4");
		menu3.add(menuItem34);
		
		Menu menu4 = new Menu();
		menu4.setName("Misc");
		menu4.setLabel("Misc");
		MenuItem menuItem41 = new MenuItem();
		menuItem41.setName("About");
		menuItem41.setLabel("About");
		menu4.add(menuItem41);
		
		Menu menuHelp = new Menu();
		menuHelp.setName("Help");
		menuHelp.setLabel("Help");
		
		
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu3);
		menuBar.add(menu4);
		menuBar.setHelpMenu(menuHelp);
		
		gameFrame.setMenuBar(menuBar);
		
		Dimension minSize = new Dimension(minFrameHeight, minFrameWidth);
		
		gameFrame.setSize(frameHeight, frameWidth);
		gameFrame.setMinimumSize(minSize);
		//gamePanel.setForeground(Color.YELLOW);
		//gamePanel.setBackground(Color.BLACK);
		gameFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		gameFrame.setTitle(gameTitle);
		ImageIcon icon = createImageIcon("icons/" + gameIcon,
                "a pretty but meaningless splat");
		gameFrame.setIconImage(icon.getImage());
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.gamePanel = gamePanel;
		this.gameFrame = gameFrame;
		
		this.playerNum = playerNum;
		
		getGameFrame().setLocation((Game.getScreenSize().width / 2) - this.getGameFrame().getWidth()/2, (Game.getScreenSize().height / 2) - this.getGameFrame().getHeight()/2);
	}
	
	public int getFrameHeight() {
		return frameHeight;
	}
	
	public int getFrameWidth() {
		return frameWidth;
	}
	
	public int getMinFrameHeight() {
		return minFrameHeight;
	}
	
	public int getMinFrameWidth() {
		return minFrameWidth;
	}
	
	public int getMaxFrameHeight() {
		return maxFrameHeight;
	}
	
	public int getMaxFrameWidth() {
		return maxFrameWidth;
	}
	
	public JFrame getGameFrame() {
		return gameFrame;
	}
	
	public JPanel getGamePanel() {
		return gamePanel;
	}
	
	public void setGameFrame(JFrame gameFrame) {
		this.gameFrame = gameFrame;
	}
	
	public void setGamePanel(JPanel gamePanel) {
		this.gameFrame.add(gamePanel);
		this.gamePanel = gamePanel;
		//this.gameFrame.pack();
	}
	
	public void setPlayer(int i, Player player) {
		this.players[i] = player;
	}
	
	public Player getPlayer(int i) {
		return players[i];
	}
	
	public int getPlayerNum() {
		return playerNum;
	}
	
	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
	
	public static Dimension getScreenSize() {
		return screenSize;
	}
	
	public abstract boolean updateMove(int numx, int numy, Player p1);
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
		ImageIcon imageIcon = new ImageIcon(path, description);
	    if (imageIcon.equals(null)) {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
		return imageIcon;
	}
	
}
