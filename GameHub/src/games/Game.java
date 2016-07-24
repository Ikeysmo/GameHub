package games;

import java.awt.Cursor;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import player.Player;

//to turn game into online capable, 1) import network class, 2)implement network handler, 3) override handle_data method

public abstract class Game extends JPanel{
	private JFrame gameFrame; //main window for the game
	private Vector<Player> players;
	
	//automatically loads default JFRAME & JPANEL, and provide generic menu interface
	public Game(String gameTitle, String gameIcon, int frame_width, int frame_height, int playerNum)
		{
		this.players = new Vector<Player>();
		MenuBar menuBar = new MenuBar(); //consistent menu across all games? Or a leave it up to the game?
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
		//end of that standard menu
		gameFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		gameFrame.setTitle(gameTitle);
		ImageIcon icon = createImageIcon("icons/" + gameIcon, "a pretty but meaningless splat");
		gameFrame.setIconImage(icon.getImage());
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setSize(frame_width, frame_height);
		gameFrame.setVisible(true);
		}
	
	
	public JPanel getGamePanel() {
		return this;
	}
	
	public void setGamePanel(JPanel gamePanel) { //recommended to not use!
		this.add(gamePanel);
	}
	
	public void addPlayer(Player player) {
		this.players.add(player);
	}
	
	public Player getPlayer(int i) {
		return players.elementAt(i);
	}
	
	public int getNumPlayers() {
		return players.size();
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path, String description) {
		ImageIcon imageIcon = new ImageIcon(path, description);
	    if (imageIcon.equals(null)) {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
		return imageIcon;
	}
	
}
