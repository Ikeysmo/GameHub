package gameHub;

/**
 * The login to get into GameHub
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import games.Game;
import games.brickBreaker.BrickBreaker;
import games.connectFour.ConnectFour;
import games.hangman.Hangman;
import games.pong.Pong;
import games.snake.Snake;
import games.ticTacToe.TicTacToe;
import games.triviaGame.Trivia;
import games.wordWhomp.WordWhomp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Gamehub implements FocusListener, KeyListener, ActionListener, Runnable, ListSelectionListener {
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();
	
	public final int GAME_ICON_SIZE = 150;
	
	/* The player's Account */
	private PlayerAccount p1 = new PlayerAccount("Zachary", "Jones");
	/* To Send data */
	private ObjectOutputStream oos;
	/* To Receive data */
	private ObjectInputStream ois;
	/* Is this Login connect? */
	private boolean isConnected;
	
	/*
	 * GAMEHUB WINDOW
	 * 3rd Window seen
	 * Purpose: Full access to all of GameHubs functionalities
	 * Components:
	 */
	private JFrame gameHubWindow; //Needs values not initilized yet
	private JTabbedPane mainPanel = new JTabbedPane();
	private JTextField chatOut = new JTextField();
	private JTextArea chatIn = new JTextArea();
	private JButton chatSubmitButton = new JButton("Submit");
	private JButton tictactoeButton;
	private JButton connectfourButton;
	private JButton snakeButton;
	private JButton hangManButton;
	private JButton triviaGameButton;
	private JButton wordWhompButton;
	private JButton brickBreakerButton;
	private JButton pongButton;
	private JList<String> onlineList = new JList<String>();
	private JPanel homePanel = new JPanel();
	private JScrollPane bd = new JScrollPane(homePanel);
	private JScrollPane wd = new JScrollPane(chatIn);
	private JPanel scorePanel = new JPanel();
	private JPanel trophyPanel = new JPanel();
	private ImageIcon tictac = new ImageIcon("icons/ticTacToeIcon.gif", "tictactoe");
	private ImageIcon con4 = new ImageIcon("icons/connectFour.png", "connect 4");
	private ImageIcon snake = new ImageIcon("icons/snake.png", "snake");
	private ImageIcon hangman = new ImageIcon("icons/hangman.png", "hangman");
	private ImageIcon triviaGame = new ImageIcon("icons/triviaGame.png", "triviaGame");
	private ImageIcon wordWhomp = new ImageIcon("icons/wordWhomp.png", "wordWhomp");
	private ImageIcon brickBreaker = new ImageIcon("icons/brickBreaker.png", "brickBreaker");
	private ImageIcon pong = new ImageIcon("icons/pong.png", "pong");
	private JPanel onlinePanel = new JPanel();
	private JPanel chatPanel = new JPanel(); //for the bottom
	
	/*
	 * INVITE WINDOW
	 * 4th Window seen
	 * Purpose: to invite a fellow friend on the server to a game (more info needed)
	 * Components: inviteWindow, middlePanel, acceptButton, denyButton
	 */
	JFrame inviteWindow = new JFrame("Invite");
	JPanel middlePanel = new JPanel();
	JButton acceptButton = new JButton("Accept");
	JButton denyButton = new JButton("Deny");
	
	/**
	 * Helper method to set up the GameHubWindow JFrame
	 * 
	 * @author Zachary R Jones
	 */
	public void setUpGameHubWindow() {
		isConnected = true;
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
		gameHubWindow.setMinimumSize(new Dimension(1024,800));
		gameHubWindow.setResizable(false);
		gameHubWindow.setVisible(true);
		gameHubWindow.getContentPane().setBackground(new Color(52,36,74));
		gameHubWindow.setLayout(null);
		//gameHubWindow.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		onlineList.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 30));
		onlineList.setPreferredSize(new Dimension(224,600));
		onlineList.setBackground(new Color(215,204,230));
		//stopped
		wd.setPreferredSize(new Dimension(1010,100));
		homePanel.setBackground(Color.black);
		homePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		scorePanel.setBackground(new Color(240,250,240));
		trophyPanel.setBackground(Color.green);
		mainPanel.setForeground(Color.black);
		mainPanel.setBackground(Color.orange);
		mainPanel.addTab("Home", bd);
		scorePanel.add(new JLabel("This is where to put scores!"));
		trophyPanel.add(new JLabel("Trophy listing!"));
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
		tictactoeButton = new JButton(tictac);
		connectfourButton = new JButton(con4);
		snakeButton = new JButton(snake);
		hangManButton = new JButton(hangman);
		triviaGameButton = new JButton(triviaGame);
		wordWhompButton = new JButton(wordWhomp);
		brickBreakerButton = new JButton(brickBreaker);
		pongButton = new JButton(pong);
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
		tictactoeButton.addActionListener(this);
		connectfourButton.addActionListener(this);
		snakeButton.addActionListener(this);
		hangManButton.addActionListener(this);
		triviaGameButton.addActionListener(this);
		wordWhompButton.addActionListener(this);
		brickBreakerButton.addActionListener(this);
		pongButton.addActionListener(this);
		mainPanel.addTab("Scores", scorePanel);
		mainPanel.addTab("Trophies", trophyPanel);
		chatSubmitButton.addActionListener(this);

		mainPanel.setBackground(new Color(52,36,74));
		mainPanel.setBounds(0, 0, 800, 600);
		onlinePanel.setBackground(new Color(52,36,74));
		onlinePanel.setBounds(804, 0, 215, 600);
		onlineList.setBorder(new LineBorder(Color.black));
		chatPanel.setBackground(new Color(52,36,74));
		JLabel this_online = new JLabel("Who's Online");
		this_online.setForeground(Color.white);
		this_online.setFont(new Font("Arial MT Bold", Font.BOLD, 15));
		onlinePanel.add(this_online);
		onlinePanel.add(onlineList);
		chatPanel.setBounds(0, 600, 1024, 200);
		//chatIn.setPreferredSize(new Dimension(1010, 100));
		chatIn.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		chatIn.setBorder(new LineBorder(Color.black));
		chatOut.setPreferredSize(new Dimension(920,30));
		chatOut.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		chatIn.setForeground(Color.BLUE);
		chatIn.setBackground(new Color(185,219,217));
		chatOut.requestFocus();
		chatOut.addKeyListener(this);
		chatOut.setBorder(new LineBorder(Color.black));
		chatIn.setEditable(false);
		chatPanel.add(wd);
		chatPanel.add(chatOut);
		chatPanel.add(chatSubmitButton);
		gameHubWindow.add(mainPanel);
		gameHubWindow.add(onlinePanel);
		gameHubWindow.add(chatPanel);
	}
	
	public Gamehub() {
		setUpGameHubWindow();
	}

	public static void main(String[] args) {
			new Gamehub();
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
			Snake snake = new Snake();
			snake.getGameFrame().setVisible(true);
		}
		else if(arg0.getSource() == hangManButton) {
			try {
				new Hangman();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(arg0.getSource() == brickBreakerButton) {
			new BrickBreaker();
		} else if(arg0.getSource() == pongButton) {
			new Pong();
		}
		else if(arg0.getSource() == triviaGameButton) {
			new Trivia();
		}
		else if(arg0.getSource() == wordWhompButton) {
			try {
				new WordWhomp();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void get_scores(){;}
	public void add_trophy(){;}
	
	
	@Override
	public void run() {
		//this is where I begin to run the code after login

		/*
		 * This loop listens for input and de-multiplexes it to handle it depending on what type of object it is
		 * E.g if it is chatmessage, echo it to everyone. If it is a gameInvite, selectively forward it, etc..
		 */
		while(isConnected){
			try {
				Object message = ois.readObject();
				System.out.println("Got something!");
				if(message instanceof String[]){ //if it is a string array, it is list of those who are online
					onlineList.setListData((String[]) message);
					onlinePanel.updateUI();
					onlineList.updateUI();
				}
				else if(message instanceof GameInvite){ //It recieved a gameInvite, proceed to create a notification window
					GameInvite invite = (GameInvite) message; 
					System.out.println("Got invite from " +invite.from + " to " +invite.to+ "!");
					if(invite.to.equalsIgnoreCase(p1.username)){
						
						inviteWindow.setSize(300, 300);
						inviteWindow.add(new JLabel("You got an invite!"));
						acceptButton.addActionListener(new ActionListener() {
							//finished creating notification window, add temporary actionlisteners
							@Override
							public void actionPerformed(ActionEvent arg0) {
								invite.Accept(); //if acceptbutton is pressed, invite is accepted
								try {
									oos.writeObject(invite); //accepted invite is sent back to server who notifies sender client that it's invite was accepted
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								System.out.println("Accepted invite!");
								if(invite.game.contains(GameInvite.tictactoe)){ //now that you created invite, load up tic tac toe.. haven't added one for connect4
									try {
										new TicTacToe(p1.getUsername(), invite.from, false);
									} catch (UnknownHostException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								else if(invite.game.contains(GameInvite.connect4)){
										//TODO: Fix ConnectFour constructor
									//	new ConnectFour(p1.getUsername(), invite.from, false, ip_Address, false);
								}
								else if(invite.game.contains(GameInvite.snake)){
										//TODO: Fix Snake constructor
										//new Snake(p1.getUsername(), invite.from, false, ip_Address, false);
								}
								else if(invite.game.contains(GameInvite.hangman)){
										//TODO: Fix Hangman constructor
										//new Hangman(p1.getUsername(), invite.from, false, ip_Address, false);
								}
								else if(invite.game.contains(GameInvite.triviaGame)){
										//TODO: Fix Trivia constructor
										//new Trivia(p1.getUsername(), invite.from, false, ip_Address, false);
								}
								else if(invite.game.contains(GameInvite.wordWhomp)){
										//TODO: Fix WordWhomp constructor
										//new WordWhomp(p1.getUsername(), invite.from, false, ip_Address, false);
								}
								else if(invite.game.contains(GameInvite.brickBreaker)){
										//TODO: Fix BrickBreaker constructor
										//new BrickBreaker(p1.getUsername(), invite.from, false, ip_Address, false);
								} else if(invite.game.contains(GameInvite.pong)){
										//TODO: Fix Pong constructor
										//new Pong(p1.getUsername(), invite.from, false, ip_Address, false);
								}
								inviteWindow.dispose(); //destroy invite/notificaiton window as no longer needed
							}
						}); //make local function
						middlePanel.add(acceptButton);
						denyButton.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								// Opposite of accepted... it is denied
								invite.Deny();
								try {
									oos.writeObject(invite);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								System.out.println("Denied invitation!");
								inviteWindow.dispose();
							}
						});
						middlePanel.add(denyButton);
						inviteWindow.add(middlePanel);
						inviteWindow.setTitle("Invite from " +invite.from + "!");
						inviteWindow.setVisible(true);
					}
					else if(invite.from.equals(p1.getUsername()) && invite.isAccepted()){
						System.out.println("Opponent accepted invite, opening game now");
						if(invite.game.equals(GameInvite.tictactoe)) {
							new TicTacToe(p1.getUsername(), (String)onlineList.getSelectedValue(), true); //attempt to pass in the sockets to tictactoe so it can connect directly to the server
						} else if (invite.game.equals(GameInvite.connect4)) {
							//TODO: Fix ConnectFour constructor
							//new ConnectFour(p1.getUsername(), (String)onlineList.getSelectedValue(), true);
						} else if (invite.game.equals(GameInvite.snake)) {
							//TODO: Fix Snake constructor
							//new Snake(p1.getUsername(), (String)onlineList.getSelectedValue(), true);
						} else if (invite.game.equals(GameInvite.hangman)) {
							//TODO: Fix Hangman constructor
							//new Hangman(p1.getUsername(), (String)onlineList.getSelectedValue(), true);
						} else if (invite.game.equals(GameInvite.triviaGame)) {
							//TODO: Fix Trivia constructor
							//new Trivia(p1.getUsername(), (String)onlineList.getSelectedValue(), true);
						} else if (invite.game.equals(GameInvite.wordWhomp)) {
							//TODO: Fix WordWhomp constructor
							//new WordWhomp(p1.getUsername(), (String)onlineList.getSelectedValue(), true);
						} else if (invite.game.equals(GameInvite.brickBreaker)) {
							//TODO: Fix BrickBreaker constructor
							//new BrickBreaker(p1.getUsername(), (String)onlineList.getSelectedValue(), true);
						} else if (invite.game.equals(GameInvite.pong)){
							//new Pong(p1.getUsername(), (String)onlineList.getSelectedValue(), true);
						}
					}
				}
				else if(message instanceof ChatMessage){
					System.out.println("Abe Lincoln");
					ChatMessage chat = (ChatMessage) message;
					/*if(chat.from.equalsIgnoreCase(p1.getUsername()))
						chatIn.setForeground(Color.blue);
					else
						chatIn.setForeground(Color.red);  */
					chatIn.append(chat.from + ": " + chat.message); //reveal chat message to GUI window
					chatIn.setCaretPosition(chatIn.getDocument().getLength());
				} 
				//this does not CATCH anything that isn't one of these! The games should catch the rest!
				else{
					System.out.println("Hey, received something:" + message);//this is where I look at my matches list and handle it correspondingly(forward it to whoever is also in match)
					
					chatIn.append(message.toString()+ "!" + System.lineSeparator());
				}
			}catch(java.net.SocketException s) {
				System.out.println("For some reason these is an exception here\nCan't seem to get rid of");
			}catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
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
				!pongButton.isEnabled()){
			tictactoeButton.setEnabled(true);
			connectfourButton.setEnabled(true);
			snakeButton.setEnabled(true);
			hangManButton.setEnabled(true);
			triviaGameButton.setEnabled(true);
			wordWhompButton.setEnabled(true);
			brickBreakerButton.setEnabled(true);
			pongButton.setEnabled(true);
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
