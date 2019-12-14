package gameHub;

/**
 * The login to get into GameHub
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

/* Import for all games */
import games.brickBreaker.BrickBreaker;
import games.connectFour.ConnectFour;
import games.flappyBird.FlappyBird;
import games.hangman.Hangman;
import games.pong.Pong;
import games.snake.Snake;
import games.ticTacToe.TicTacToe;
import games.triviaGame.Trivia;
import games.wordWhomp.WordWhomp;

/* AWT (GUI) components */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/* Swing Components */
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Gamehub implements FocusListener, KeyListener, ActionListener, ListSelectionListener {
	
	//Screen size Dimensions
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	//Width of the screen
	double width = screenSize.getWidth();
	//Height of the screen
	double height = screenSize.getHeight();
	
	//Size of the game icon
	public final int GAME_ICON_SIZE = 150;
	
	/* The player's Account */
	private PlayerAccount p1 = new PlayerAccount("John", "Doe");
	
	/*
	 * GAMEHUB WINDOW
	 * Purpose: Full access to all of GameHubs functionalities
	 * Components:
	 */
	private JFrame gameHubWindow; //Needs values not initilized yet
	private JButton tictactoeButton;
	private JButton connectfourButton;
	private JButton snakeButton;
	private JButton hangManButton;
	private JButton triviaGameButton;
	private JButton wordWhompButton;
	private JButton brickBreakerButton;
	private JButton pongButton;
	private JButton flappyBirdButton;
	private JPanel homePanel = new JPanel();
	private JScrollPane bd = new JScrollPane(homePanel);
	private ImageIcon tictac = new ImageIcon("icons/ticTacToeIcon.gif", "tictactoe");
	private ImageIcon con4 = new ImageIcon("icons/connectFour.png", "connect 4");
	private ImageIcon snake = new ImageIcon("icons/snake.png", "snake");
	private ImageIcon hangman = new ImageIcon("icons/hangman.png", "hangman");
	private ImageIcon triviaGame = new ImageIcon("icons/triviaGame.png", "triviaGame");
	private ImageIcon wordWhomp = new ImageIcon("icons/wordWhomp.png", "wordWhomp");
	private ImageIcon brickBreaker = new ImageIcon("icons/brickBreaker.png", "brickBreaker");
	private ImageIcon pong = new ImageIcon("icons/pong.png", "pong");
	private ImageIcon flappyBird = new ImageIcon("icons/flappyBird.png", "flappyBird");
	
	public static void main(String[] args) {
		new Gamehub();
	}
	
	public Gamehub() {
		setUpGameHubWindow();
	}
	
	/**
	 * Helper method to set up the GameHubWindow JFrame
	 * 
	 * @author Zachary R Jones
	 */
	public void setUpGameHubWindow() {
		gameHubWindow = new JFrame("Welcome to GameHub " +p1.getUsername() +"!" + System.lineSeparator());
		gameHubWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameHubWindow.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if (JOptionPane.showConfirmDialog(gameHubWindow, 
		                "Are you sure to close this window?", "Really Closing?", 
		                JOptionPane.YES_NO_OPTION,
		                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            }
		    }
		});
		BufferedImage pop = null;
		try {
			pop = ImageIO.read(new File("icons/gamehub_logo.png"));
			gameHubWindow.setIconImage(pop);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameHubWindow.setMinimumSize(new Dimension(600,600));
		gameHubWindow.setResizable(false);
		gameHubWindow.getContentPane().setBackground(new Color(52,36,74));
		gameHubWindow.setLayout(null);
		//gameHubWindow.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		homePanel.setBackground(Color.black);
		homePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//add code to flesh out home panel or show games
		Image img = tictac.getImage() ;  
		Image newimg = img.getScaledInstance( GAME_ICON_SIZE, GAME_ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;  
		tictac = new ImageIcon( newimg );
		img = con4.getImage() ;  
		newimg = img.getScaledInstance( GAME_ICON_SIZE, GAME_ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;  
		con4 = new ImageIcon( newimg );
		img = snake.getImage() ;  
		newimg = img.getScaledInstance( GAME_ICON_SIZE, GAME_ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;  
		snake = new ImageIcon( newimg );
		img = hangman.getImage() ;  
		newimg = img.getScaledInstance( GAME_ICON_SIZE, GAME_ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;  
		hangman = new ImageIcon( newimg );
		img = triviaGame.getImage() ;  
		newimg = img.getScaledInstance( GAME_ICON_SIZE, GAME_ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;  
		triviaGame = new ImageIcon( newimg );
		img = wordWhomp.getImage() ;  
		newimg = img.getScaledInstance( GAME_ICON_SIZE, GAME_ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;  
		wordWhomp = new ImageIcon( newimg );
		img = brickBreaker.getImage() ;  
		newimg = img.getScaledInstance( GAME_ICON_SIZE, GAME_ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;  
		brickBreaker = new ImageIcon( newimg );
		img = pong.getImage() ;  
		newimg = img.getScaledInstance( GAME_ICON_SIZE, GAME_ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;  
		pong = new ImageIcon( newimg );
		img = flappyBird.getImage();
		newimg = img.getScaledInstance( GAME_ICON_SIZE, GAME_ICON_SIZE, java.awt.Image.SCALE_SMOOTH ) ;
		flappyBird = new ImageIcon( newimg );
		tictactoeButton = new JButton(tictac);
		connectfourButton = new JButton(con4);
		snakeButton = new JButton(snake);
		hangManButton = new JButton(hangman);
		triviaGameButton = new JButton(triviaGame);
		wordWhompButton = new JButton(wordWhomp);
		brickBreakerButton = new JButton(brickBreaker);
		pongButton = new JButton(pong);
		flappyBirdButton = new JButton(flappyBird);
		//homePanel.add(new JLabel("Do the thing here!"));
		c.gridx = 0;
		c.gridy = 0;
		homePanel.add(tictactoeButton, c);
		c.gridx = 1;
		c.gridy = 0;
		homePanel.add(connectfourButton, c);
		c.gridx = 2;
		c.gridy = 0;
		homePanel.add(snakeButton, c);
		c.gridx = 0;
		c.gridy = 1;
		homePanel.add(hangManButton, c);
		c.gridx = 1;
		c.gridy = 1;
		homePanel.add(triviaGameButton, c);
		c.gridx = 2;
		c.gridy = 1;
		homePanel.add(wordWhompButton, c);
		c.gridx = 0;
		c.gridy = 2;
		homePanel.add(brickBreakerButton, c);
		c.gridx = 1;
		c.gridy = 2;
		homePanel.add(pongButton, c);
		c.gridx = 2;
		c.gridy = 2;
		homePanel.add(flappyBirdButton, c);
		tictactoeButton.addActionListener(this);
		connectfourButton.addActionListener(this);
		snakeButton.addActionListener(this);
		hangManButton.addActionListener(this);
		triviaGameButton.addActionListener(this);
		wordWhompButton.addActionListener(this);
		brickBreakerButton.addActionListener(this);
		pongButton.addActionListener(this);
		flappyBirdButton.addActionListener(this);
		gameHubWindow.setContentPane(homePanel);
		
		//Lastly, set it to visible
		gameHubWindow.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		
		if(arg0.getSource() == connectfourButton){
			new ConnectFour();
		}
		else if(arg0.getSource() == tictactoeButton){
			new TicTacToe();
		}
		else if(arg0.getSource() == snakeButton) {
			new Snake();
		}
		else if(arg0.getSource() == hangManButton) {
			try {
				new Hangman();
			} catch (IOException e) {
				System.err.println("Something went wrong with Hangman");
				e.printStackTrace();
			}
		}
		else if(arg0.getSource() == brickBreakerButton) {
			new BrickBreaker();
		} 
		else if(arg0.getSource() == pongButton) {
			new Pong();
		}
		else if(arg0.getSource() == triviaGameButton) {
			new Trivia();
		}
		else if(arg0.getSource() == wordWhompButton) {
			try {
				new WordWhomp();
			} catch (IOException e) {
				System.err.println("Something went wrong with WordWhomp");
				e.printStackTrace();
			}
		} else if(arg0.getSource() == flappyBirdButton) {
			new FlappyBird();
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if(!tictactoeButton.isEnabled() ||
				!connectfourButton.isEnabled() ||
				!snakeButton.isEnabled() ||
				!hangManButton.isEnabled() ||
				!triviaGameButton.isEnabled() ||
				!wordWhompButton.isEnabled() ||
				!brickBreakerButton.isEnabled() ||
				!pongButton.isEnabled() ||
				!flappyBirdButton.isEnabled()){
			tictactoeButton.setEnabled(true);
			connectfourButton.setEnabled(true);
			snakeButton.setEnabled(true);
			hangManButton.setEnabled(true);
			triviaGameButton.setEnabled(true);
			wordWhompButton.setEnabled(true);
			brickBreakerButton.setEnabled(true);
			pongButton.setEnabled(true);
			flappyBirdButton.setEnabled(true);
		}
	}

	@Override
	public void keyPressed(KeyEvent kp) {  
	
	}

	@Override
	public void keyReleased(KeyEvent arg0) {		}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		
	}

}
