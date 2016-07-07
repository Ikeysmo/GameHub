package gameHub;

/**
 * The login to get into GameHub
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import connectFour.ConnectFour;
import ticTacToe.TicTacToe;

public class GamehubLogIn implements FocusListener, KeyListener, ActionListener, Runnable, ListSelectionListener {
	
	/* The player's Account */
	private PlayerAccount p1;
	/* Socket to connect to the Server */
	private Socket s;
	/* To Send data */
	private ObjectOutputStream oos;
	/* To Receive data */
	private ObjectInputStream ois;
	/* Is this Login connect? */
	private boolean isConnected;
	
	/*
	 * IP_ADDRESS WINDOW
	 * 1st Window seen
	 * Purpose: To input ip_address of the Host Server
	 * Components: ipAddressWindow, ipAddressMessage, ipAddressBox, ipAddressSubmit
	 */
	private JFrame ipAddressWindow = new JFrame("HOST SERVER IP_ADDRESS");
	private JLabel ipAddressMessage = new JLabel("HOST SERVER IP_ADDRESS: ");
	private JPanel ipAddressPanel = new JPanel();
	private JTextField ipAddressBox = new JTextField("");
	private JButton ipAddressSubmit = new JButton("Submit");
	private String ip_Address = null;
	
	/*
	 * MAIN WINDOW
	 * 2nd Window seen
	 * Purpose: To input username name and password in, and login to the Gamehub
	 * Components: mainWindow, welcomeeMessage, instru, loginbox, passBox, loginbutton, registerButton
	 */
	private JFrame mainWindow = new JFrame("Welcome to GameHub!");
	private JPanel mainWindowPanel = new JPanel();
	private JLabel welcomeMessage = new JLabel("Welcome to GameHub. Please sign in now!");
	private JLabel instru = new JLabel("Enter username and password below!");
	private JTextField loginBox = new JTextField("Enter Username");
	private JPasswordField passBox = new JPasswordField("");
	private JButton loginButton = new JButton("Log In!");
	private JButton registerButton = new JButton("Register!");
	
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
	private JList onlineList = new JList();
	private JLabel errormsg = new JLabel();
	private JPanel homePanel = new JPanel();
	private JScrollPane bd = new JScrollPane(homePanel);
	private JScrollPane wd = new JScrollPane(chatIn);
	private JPanel scorePanel = new JPanel();
	private JPanel trophyPanel = new JPanel();
	private ImageIcon tictac = new ImageIcon("tic_tac_toe.png", "tictactoe");
	private ImageIcon con4 = new ImageIcon("connect4.png", "connect 4");
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
	 * Helper method to set up the IP_Address JFrame
	 * 
	 * @author Zachary R Jones
	 */
	public void setUpIp_AddressWindow() {
		ipAddressWindow.requestFocus();
		ipAddressWindow.setSize(new Dimension(600,600));
		ipAddressPanel.setSize(new Dimension(500,500));
		ipAddressPanel.setLayout(new GridLayout());
		ipAddressBox.setSize(new Dimension(200, 100));
		ipAddressMessage.setSize(new Dimension(200, 100));
		ipAddressSubmit.setSize(new Dimension(200, 100));
		ipAddressSubmit.addActionListener(this);
		ipAddressPanel.add(ipAddressMessage);
		ipAddressPanel.add(ipAddressBox);
		ipAddressPanel.add(ipAddressSubmit);
		ipAddressWindow.add(ipAddressPanel);
		
		ipAddressWindow.setVisible(true);
	}
	
	/**
	 * Helper method to set up the MainWindow JFrame
	 * 
	 * @author Zachary R Jones
	 */
	public void setUpMainWindow() {
		//games.setListData(listData);
		welcomeMessage.setFont(new Font("Default", Font.BOLD, 20));
		welcomeMessage.setForeground(Color.DARK_GRAY);
		mainWindow.requestFocus(); //gets focus of window so loginBox isn't empty
		loginBox.setFont(new Font("Default", Font.BOLD, 20));
		loginBox.addFocusListener(this);
		passBox.addKeyListener(this);
		//loginBox.addKeyListener(this);
		loginBox.setPreferredSize(new Dimension(170,30));
		loginBox.setMinimumSize(new Dimension(170,30));
		passBox.setFont(new Font("Default", Font.BOLD, 20));
		passBox.setPreferredSize(new Dimension(170, 30));
		passBox.setMinimumSize(new Dimension(170, 30));
		passBox.addFocusListener(this);
		//passBox.addKeyListener(this);
		mainWindowPanel.setBackground(Color.orange);
		mainWindowPanel.add(welcomeMessage);
		BufferedImage pop = null;
		try {
			pop = ImageIO.read(new File("gamepad.png"));
			JLabel img_show = new JLabel(new ImageIcon(pop));
			mainWindowPanel.add(img_show);
		} catch (IOException e) {
			e.printStackTrace();
		}
		instru.setFont(new Font("default", Font.BOLD, 20));
		mainWindowPanel.add(instru);
		mainWindowPanel.add(Box.createRigidArea(new Dimension(100, 20)));
		mainWindowPanel.add(loginBox);
		mainWindowPanel.add(passBox);
		mainWindowPanel.add(loginButton);
		mainWindowPanel.add(registerButton);
		errormsg.setForeground(Color.red);
		mainWindow.getContentPane().add(mainWindowPanel);
		mainWindow.getContentPane().add(errormsg, "South");
		mainWindow.setSize(590,660);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		mainWindow.requestFocus();
		loginButton.addActionListener(this);
		registerButton.addActionListener(this);
		loginBox.setText("Username");
		onlineList.addListSelectionListener(this);
	}
	
	/**
	 * Helper method to set up the GameHubWindow JFrame
	 * 
	 * @author Zachary R Jones
	 */
	public void setUpGameHubWindow() {
		isConnected = true;
		if(gameHubWindow != null && !gameHubWindow.isShowing())
			mainWindow.dispose(); //destroy the window, load up new ones for a signed in account
		gameHubWindow = new JFrame("Welcome to GameHub " +p1.getUsername() +"!" + System.lineSeparator());
		gameHubWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameHubWindow.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if (JOptionPane.showConfirmDialog(gameHubWindow, 
		                "Are you sure to close this window?", "Really Closing?", 
		                JOptionPane.YES_NO_OPTION,
		                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		    			isConnected = disconnect();
		            }
		    }
		});
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
		homePanel.setBackground(Color.white);
		scorePanel.setBackground(new Color(240,250,240));
		trophyPanel.setBackground(Color.green);
		mainPanel.setForeground(Color.cyan);
		mainPanel.setBackground(Color.orange);
		mainPanel.addTab("Home", bd);
		scorePanel.add(new JLabel("This is where to put scores!"));
		trophyPanel.add(new JLabel("Trophy listing!"));
		//add code to flesh out home panel or show games
		tictactoeButton = new JButton(tictac);
		connectfourButton = new JButton(con4);
		tictactoeButton.setEnabled(false);
		//homePanel.add(new JLabel("Do the thing here!"));
		homePanel.add(tictactoeButton);
		homePanel.add(connectfourButton);
		tictactoeButton.addActionListener(this);
		connectfourButton.addActionListener(this);
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
		chatIn.setFont(new Font("Default", Font.BOLD, 15));
		chatIn.setBorder(new LineBorder(Color.black));
		chatOut.setPreferredSize(new Dimension(920,30));
		chatOut.setFont(new Font("default", Font.BOLD, 15));
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
	
	/**
	 * Helper method to set up the GameHub JFrame
	 * 
	 * @author Zachary R Jones
	 */
	public void setUpInviteWindow() {
		
	}
	public GamehubLogIn(String ipaddress) {
		if(ipaddress != null) 
			ip_Address = ipaddress;
		setUpIp_AddressWindow();
	}
	
	//Disconnects from remote server
	//Returns true on success, false on failure
	public boolean disconnect(){
	    try{
	        this.oos.writeObject("0x000000");
	        this.s.close();
	        this.isConnected = false;
	        System.out.println("The Socket is closed");
	        return false;
	    }catch(Exception e){
	    	System.out.println("The Socket is open");
	        return true;
	    }
	}

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			new GamehubLogIn(null);
		} else {
			new GamehubLogIn(args[0]); //load Gamehub dynamically instead of static, so we can pass in ip address
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		//If logIn box gets clicked on, clear the error messages as well as the login/password fields
		errormsg.setText(""); 
		if(arg0.getSource() == loginBox)
			loginBox.setText("");
		else if(arg0.getSource() == passBox)
			passBox.setText("");
		else if(arg0.getSource() == chatOut){
			if(chatOut.getText().equals("Outgoing"))
				chatOut.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		//If focus is lost from loginBox, restore instructions to enter username
		if(arg0.getSource() == loginBox && loginBox.getText().isEmpty())
			loginBox.setText("Enter Username");
		//else if(arg0.getSource() == passBox && passBox.getPassword().length == 0)
		//passBox.setText("Enter Password   ");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		errormsg.setText(""); //always clear at any time something happens
		if(arg0.getSource() == loginButton){ //if login button, begin process of getting online
			try {
				if(loginBox.getText().equals("Enter Username")){ //user hasn't typed anything
					errormsg.setText("Error: Must enter a valid Username!");
					return;
				}

				else if(loginBox.getText().equals("")) //if blank, ignore
					return;
				s = new Socket(ip_Address, 2020); //create connection to server
				oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(loginBox.getText() + "/" + String.valueOf(passBox.getPassword())); //initial "hello" to server, sends username and password
				ois = new ObjectInputStream(s.getInputStream()); 
				String serverReply = (String) ois.readObject();
				System.out.println("Server Reply:" + serverReply); 
				chatIn.append(serverReply + "!" + System.lineSeparator());
				if(!serverReply.contains("Welcome")){ //server sends back welcome... if not, then error --> incorrect username/password!
					errormsg.setText("Error: Incorrect Username or Password");
					return;
				}
				p1 = new PlayerAccount(loginBox.getText().toUpperCase(), String.valueOf(passBox.getPassword())); //create a new player account with typed in username/password now that we've signed in
				mainWindow.dispose(); //destroy the window, load up new ones for a signed in account
				mainWindow = null;
				
				//the changes are affected here
				setUpGameHubWindow();
				new Thread(this).start(); //begin separate thread for listening on the created socket
			}

			catch(IOException | ClassNotFoundException e){
				errormsg.setText("The ip_address: " + ip_Address + " was invalid.\n Please restart and enter correct Host IP_Address");;
			}

		}
		else if (arg0.getSource() == registerButton){
			//put code here
			try {
				if(loginBox.getText().equals("Enter Username")){ //user hasn't typed anything
					errormsg.setText("Error: Must enter a valid Username!");
					return;
				}

				else if(loginBox.getText().equals("") || loginBox.getText().contains("*")) //if blank, ignore
					return;
				s = new Socket(ip_Address, 2020); //create connection to server
				oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject("*"+loginBox.getText() + "/" + String.valueOf(passBox.getPassword())); //initial "hello" to server, sends username and password
				ois = new ObjectInputStream(s.getInputStream()); 
				String serverReply = (String) ois.readObject();
				System.out.println("Server Reply:" + serverReply); 
				if(!serverReply.contains("Welcome")){ //server sends back welcome... if not, then error --> incorrect username/password!
					errormsg.setText("Error: Incorrect Username or Password");
					return;
				}
				//p1 = new PlayerAccount(loginBox.getText().toUpperCase(), String.valueOf(passBox.getPassword())); //create a new player account with typed in username/password now that we've signed in
				//setUpGameHubWindow();
				new Thread(this).start(); //begin separate thread for listening on the created socket
			}

			catch(IOException | ClassNotFoundException e){
				errormsg.setText(e.getMessage());
				System.out.println("Even funnier");
			}

		}
		
		else if(arg0.getSource() == chatSubmitButton){
			try {
				oos.writeObject(new ChatMessage(chatOut.getText(),p1.getUsername() , "Everyone")); //Sends the chatMessage to everyone
				chatOut.setText(""); //Clear out the box
			} catch (IOException e){e.printStackTrace();}
		}
		
		else if(arg0.getSource() == connectfourButton){
			makeInvite(p1.getUsername(), (String) onlineList.getSelectedValue(), GameInvite.connect4);
		}
		else if(arg0.getSource() == tictactoeButton){
			makeInvite(p1.getUsername(), (String) onlineList.getSelectedValue(), GameInvite.tictactoe);
		} 
		else if(arg0.getSource() == ipAddressSubmit) {
			System.out.println("jkdasjfkd");
			ip_Address = ipAddressBox.getText();
			ipAddressWindow.dispose();
			setUpMainWindow();
		}
		
	}
	
	public void makeInvite(String username, String selectedValue, String game) {
		System.out.println("create invite window!");
		//new game invite window created here
		try {
			oos.writeObject(new GameInvite(username, selectedValue, game));
		} catch (IOException e) {
			e.printStackTrace();
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
									System.out.println("what");
									try {
										new TicTacToe(p1.getUsername(), invite.from, false, ip_Address);
									} catch (UnknownHostException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								else if(invite.game.contains(GameInvite.connect4)){
									System.out.println("trying to open conn4 now!");
									try{
										new ConnectFour(p1.getUsername(), invite.from, false, ip_Address);
									}
									catch(UnknownHostException e){
										;
									}
									catch(IOException e){;}
									
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
						if(invite.game.equals(GameInvite.tictactoe))
							new TicTacToe(p1.getUsername(), (String)onlineList.getSelectedValue(), true, ip_Address); //attempt to pass in the sockets to tictactoe so it can connect directly to the server
						else if (invite.game.equals(GameInvite.connect4))
							new ConnectFour(p1.getUsername(), (String)onlineList.getSelectedValue(), true, ip_Address);
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
		if(!tictactoeButton.isEnabled() || !connectfourButton.isEnabled()){
			tictactoeButton.setEnabled(true);
			connectfourButton.setEnabled(true);
		}
	}

	
	public void keyPressed(KeyEvent kp) {  
		//If Enter Is Pressed
		if(kp.getKeyCode() == KeyEvent.VK_ENTER) { 
			if(kp.getSource() == chatOut){
				chatSubmitButton.doClick();
			}
			else if (kp.getSource() == passBox){
				System.out.println("hallelujah");
				loginButton.doClick();
			}
		}
	
	}

	@Override
	public void keyReleased(KeyEvent arg0) {		}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

}
