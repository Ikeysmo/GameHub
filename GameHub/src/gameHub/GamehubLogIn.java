package gameHub;

/**
 * The login to get into GameHub
 * 
 * @author Isaiah Smoak
 * @author Zachary Jones
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
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
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import connectFour.ConnectFour;
import ticTacToe.TicTacToe;

//just here to configure github!
public class GamehubLogIn implements FocusListener, KeyListener, ActionListener, Runnable, ListSelectionListener {
	
	private JFrame mainmode = new JFrame("Welcome to GameHub!");
	private JFrame onlineWindow = new JFrame("Online List");
	private JList gamesList = new JList();
	private JList onlineList = new JList();
	private JLabel welcome = new JLabel("Welcome to GameHub. Please sign in now!");
	private JTextField loginBox = new JTextField("Enter Username");
	private JPasswordField passBox = new JPasswordField("");
	private JPanel mainPanel = new JPanel();
	private JButton loginButton = new JButton("Log In!");
	private JButton registerButton = new JButton("Register!");
	private JButton ticButton; //Init Below in constructor
	private JButton connButton; //Init Below in constructor
	//private JButton ticButton = new JButton("TicTacToe!");
	//private JButton connButton = new JButton("Connect4!");
	private JLabel errormsg = new JLabel();
	private JPopupMenu shot = new JPopupMenu();
	private String ip_Address = "192.12.4.5";
	private Socket s;
	private JTextArea chatOut = new JTextArea();
	private JTextArea chatIn = new JTextArea();
	private JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private PlayerAccount p1;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	private JPanel gamePanel = new JPanel();
	private JButton chatSubmitButton = new JButton("Submit");

	JPanel OnlinePanel = new JPanel();
	JScrollPane chatHistory = new JScrollPane(chatIn);
	JScrollPane chatOutbox = new JScrollPane(chatOut);

	JPanel OnlineBottomPanel = new JPanel();


	public GamehubLogIn(String ipaddress) {
		if(ipaddress != null) 
			ip_Address = ipaddress;
		//Try to make the images
		try {
			BufferedImage ticTacToeIcon = ImageIO.read(new File("tic_tac_toe.png")); //gets the image for TicTacToe
			ticTacToeIcon = resize(ticTacToeIcon, 100, 100);
			ticButton = new JButton(new ImageIcon(ticTacToeIcon));
		}catch(IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedImage connect4Icon = ImageIO.read(new File("connect4.png")); //gets the image for ConnectFour
			connect4Icon = resize(connect4Icon, 100, 100);
			connButton = new JButton(new ImageIcon(connect4Icon));
		}catch(IOException e) {
			e.printStackTrace();
		}


		//games.setListData(listData);
		/*Create window/GUI interface */
		welcome.setFont(new Font("Default", Font.BOLD, 20));
		welcome.setForeground(Color.DARK_GRAY);
		mainmode.requestFocus(); //gets focus of window so loginBox isn't empty
		loginBox.setFont(new Font("Default", Font.BOLD, 20));
		loginBox.addFocusListener(this);
		loginBox.setPreferredSize(new Dimension(170,30));
		loginBox.setMinimumSize(new Dimension(170,30));


		passBox.setFont(new Font("Default", Font.BOLD, 20));
		passBox.setPreferredSize(new Dimension(170, 30));
		passBox.setMinimumSize(new Dimension(170, 30));
		passBox.addFocusListener(this);
		mainPanel.setBackground(Color.orange);
		connButton.setEnabled(true);
		ticButton.setEnabled(true);
		mainPanel.add(welcome);
		BufferedImage pop = null;
		try {
			pop = ImageIO.read(new File("gamepad.png"));
			System.out.println("whaddup");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		JPanel imagepanel = new JPanel(){
//			private BufferedImage image;
//			public void JPanel(BufferedImage l){
//				image = l;
//			}
//			public void paint(Graphics g){
//				super.paint(g);
//				g.drawImage(image, 0, 0, null);
//				
//			}
//			public void addImage(BufferedImage di){
//				this.image = di;
//			}
//		};
//		
		ImageIcon img = new ImageIcon(pop);
		JLabel img_show = new JLabel(new ImageIcon(pop));
		JLabel instru = new JLabel("Enter username and password below!");
		instru.setFont(new Font("default", Font.BOLD, 20));
		
		mainPanel.add(img_show);
		mainPanel.add(instru);
		mainPanel.add(loginBox);
		mainPanel.add(passBox);
		mainPanel.add(loginButton);
		mainPanel.add(registerButton);
		errormsg.setForeground(Color.red);
		mainmode.getContentPane().add(mainPanel);
		mainmode.getContentPane().add(errormsg, "South");
		mainmode.setSize(600,600);
		mainmode.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainmode.setVisible(true);
		mainmode.requestFocus();
		loginButton.addActionListener(this);
		registerButton.addActionListener(this);
		chatSubmitButton.addActionListener(this);

		loginBox.setText("Username");
		shot.add(new JMenuItem("Connect 4"));
		onlineList.addListSelectionListener(this);


		mainmode.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"clickButton"); //Zach's Current way of chat, enter key working
		mainmode.getRootPane().getActionMap().put("clickButton", new AbstractAction(){
			public void actionPerformed(ActionEvent ae) 
			{
				loginButton.doClick();
			}
		});

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GamehubLogIn(args[0]); //load GameHub dynamically instead of static, so we can pass it actionListeners
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
				if(!serverReply.contains("Welcome")){ //server sends back welcome... if not, then error --> incorrect username/password!
					errormsg.setText("Error: Incorrect Username or Password");
					return;
				}
				p1 = new PlayerAccount(loginBox.getText().toUpperCase(), String.valueOf(passBox.getPassword())); //create a new player account with typed in username/password now that we've signed in
				mainmode.dispose(); //destroy the window, load up new ones for a signed in account
				mainmode = null;
				mainmode = new JFrame("Welcome to GameHub " +p1.getUsername() +"!");
				mainmode.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainmode.setSize(500, 500);
				mainmode.setLocation(400, 200);
				mainmode.setVisible(true);
				ticButton.addActionListener(this);
				connButton.addActionListener(this);
				gamePanel.setLayout(new GridLayout());
				gamePanel.add(ticButton);
				gamePanel.add(connButton);
				mainmode.add(gamePanel);
				onlineWindow.setMinimumSize(new Dimension(650, 600));
				onlineList.setFont(new Font("Default", Font.BOLD, 30));
				onlineList.setPreferredSize(new Dimension(150,470));

				chatIn.setText("Incoming!" + System.lineSeparator());
				chatIn.setFont(new Font("Default", Font.BOLD, 20));
				chatIn.setSize(300, 470);
				chatIn.setEditable(false);
				chatHistory.setPreferredSize(new Dimension(450, 470));
				chatOutbox.setPreferredSize(new Dimension(500,60));
				chatOut.setText("Outgoing");
				chatOut.setLineWrap(true);
				chatOut.addFocusListener(this);
				//chatOut.setPreferredSize(new Dimension(500, 100));
				//chatOut.set
				chatOut.setFont(new Font("Default", Font.BOLD, 20));
				chatOut.addKeyListener(this);
				OnlinePanel.add(chatHistory);
				OnlinePanel.add(splitter);
				OnlinePanel.add(onlineList);
				OnlineBottomPanel.add(chatOutbox);
				OnlineBottomPanel.add(chatSubmitButton);
				//onlineWindow.add(onlineList);
				//OnlinePanel.add(shot);
				onlineWindow.add(OnlinePanel);
				onlineWindow.add(OnlineBottomPanel, "South");

				//For using the enter key in order to submit a chat

				onlineWindow.setVisible(true);
				Point loc = mainmode.getLocation();
				onlineWindow.setLocation(loc.x+500, loc.y);

				onlineWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_0,0),"clickButton");
				onlineWindow.getRootPane().getActionMap().put("clickButton", new AbstractAction(){
					public void actionPerformed(ActionEvent ae)
					{
						chatSubmitButton.doClick();
					}
				});

				new Thread(this).start(); //begin separate thread for listening on the created socket
			}

			catch(IOException | ClassNotFoundException e){
				errormsg.setText(e.getMessage());
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
				p1 = new PlayerAccount(loginBox.getText().toUpperCase(), String.valueOf(passBox.getPassword())); //create a new player account with typed in username/password now that we've signed in
				mainmode.dispose(); //destroy the window, load up new ones for a signed in account
				mainmode = null;
				mainmode = new JFrame("Welcome to GameHub " +p1.getUsername() +"!");
				mainmode.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainmode.setSize(500, 500);
				mainmode.setLocation(400, 200);
				mainmode.setVisible(true);
				ticButton.addActionListener(this);
				connButton.addActionListener(this);
				gamePanel.setLayout(new GridLayout());
				gamePanel.add(ticButton);
				gamePanel.add(connButton);
				mainmode.add(gamePanel);
				onlineWindow.setMinimumSize(new Dimension(650, 600));
				onlineList.setFont(new Font("Default", Font.BOLD, 30));
				onlineList.setPreferredSize(new Dimension(150,470));

				chatIn.setText("Incoming!" + System.lineSeparator());
				chatIn.setFont(new Font("Default", Font.BOLD, 20));
				chatIn.setSize(300, 470);
				chatIn.setEditable(false);
				chatHistory.setPreferredSize(new Dimension(450, 470));
				chatOutbox.setPreferredSize(new Dimension(500,60));
				chatOut.setText("Outgoing");
				chatOut.setLineWrap(true);
				chatOut.addFocusListener(this);
				//chatOut.setPreferredSize(new Dimension(500, 100));
				//chatOut.set
				chatOut.setFont(new Font("Default", Font.BOLD, 20));
				chatOut.addKeyListener(this);
				OnlinePanel.add(chatHistory);
				OnlinePanel.add(splitter);
				OnlinePanel.add(onlineList);
				OnlineBottomPanel.add(chatOutbox);
				OnlineBottomPanel.add(chatSubmitButton);
				//onlineWindow.add(onlineList);
				//OnlinePanel.add(shot);
				onlineWindow.add(OnlinePanel);
				onlineWindow.add(OnlineBottomPanel, "South");

				//For using the enter key in order to submit a chat

				onlineWindow.setVisible(true);
				Point loc = mainmode.getLocation();
				onlineWindow.setLocation(loc.x+500, loc.y);

				onlineWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_0,0),"clickButton");
				onlineWindow.getRootPane().getActionMap().put("clickButton", new AbstractAction(){
					public void actionPerformed(ActionEvent ae)
					{
						chatSubmitButton.doClick();
					}
				});

				new Thread(this).start(); //begin separate thread for listening on the created socket
			}

			catch(IOException | ClassNotFoundException e){
				errormsg.setText(e.getMessage());
			}

		}
		else if(arg0.getSource() == ticButton){
			try { //send invite if pressed
				oos.writeObject(new GameInvite(p1.getUsername(), (String) onlineList.getSelectedValue(), GameInvite.tictactoe)); //send new GameInvite to server to be forwarded to other client
				//System.out.println("Pledge of allegiance"); //just there for debugging
				//new TicTacToe(p1.getUsername(), (String)onlineList.getSelectedValue(), true); //attempt to pass in the sockets to tictactoe so it can connect directly to the server
				//System.out.println("Opening tictactoe");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if(arg0.getSource() == connButton){
			try {//send a game invite! Same idea as ticButton
				oos.writeObject(new GameInvite(p1.getUsername(), (String) onlineList.getSelectedValue(), GameInvite.connect4));
				//new ConnectFour(oos, ois);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(arg0.getSource() == chatSubmitButton){

			try {
				oos.writeObject(new ChatMessage(chatOut.getText(),p1.getUsername() , "Everyone")); //Sends the chatMessage to everyone
				chatOut.setText(""); //Clear out the box
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		//this is where I begin to run the code after login

		/*
		 * This loop listens for input and de-multiplexes it to handle it depending on what type of object it is
		 * E.g if it is chatmessage, echo it to everyone. If it is a gameInvite, selectively forward it, etc..
		 */
		while(true){
			try {
				Object message = ois.readObject();
				System.out.println("Got something!");
				if(message instanceof String[]){ //if it is a string array, it is list of those who are online
					onlineList.setListData((String[]) message);
				}
				else if(message instanceof GameInvite){ //It recieved a gameInvite, proceed to create a notification window

					GameInvite invite = (GameInvite) message; 
					System.out.println("Got invite from " +invite.from + " to " +invite.to+ "!");
					if(invite.to.equalsIgnoreCase(p1.username)){
						JFrame inviteWindow = new JFrame("Invite");
						inviteWindow.setSize(300, 300);
						inviteWindow.add(new JLabel("You got an invite!"));
						JPanel middlePanel = new JPanel();
						JButton acceptButton = new JButton("Accept");
						acceptButton.addActionListener(new ActionListener() {
							//finished creating notification window, add temporary actionlisteners
							@Override
							public void actionPerformed(ActionEvent arg0) {
								invite.Accept(); //if acceptbutton is pressed, invite is accepted
								try {
									oos.writeObject(invite); //accepted invite is sent back to server who notifies sender client that it's invite was accepted
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								System.out.println("Accepted invite!");
								if(invite.game.contains(GameInvite.tictactoe)){ //now that you created invite, load up tic tac toe.. haven't added one for connect4
									System.out.println("what");
									try {
										new TicTacToe(p1.getUsername(), invite.from, false, ip_Address);
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
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
						JButton denyButton = new JButton("Deny");
						denyButton.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								// Opposite of accepted... it is denied
								invite.Deny();
								try {
									oos.writeObject(invite);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
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
						System.out.println("Oppoenet accepted invite, opening game now");
						if(invite.game.equals(invite.tictactoe))
							new TicTacToe(p1.getUsername(), (String)onlineList.getSelectedValue(), true, ip_Address); //attempt to pass in the sockets to tictactoe so it can connect directly to the server
						else if (invite.game.equals(invite.connect4))
							new ConnectFour(p1.getUsername(), (String)onlineList.getSelectedValue(), true, ip_Address);
					}
				}
				else if(message instanceof ChatMessage){
					System.out.println("Abe Lincoln");
					ChatMessage chat = (ChatMessage) message;
					chatIn.append(chat.from + ": " + chat.message); //reveal chat message to GUI window
				} 
				//this does not CATCH anything that isn't one of these! The games should catch the rest!
				else{
					System.out.println("Hey, received something:" + message);//this is where I look at my matches list and handle it correspondingly(forward it to whoever is also in match)
				}

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		ticButton.setEnabled(true);
		connButton.setEnabled(true);
	}

	/**
	 * This is a method that I found in order to figure out how to resize
	 * the icons for the games.
	 * 
	 * Source: http://stackoverflow.com/questions/9417356/bufferedimage-resize
	 * 
	 * @param img
	 * @param newW
	 * @param newH
	 * @return
	 */
	private static BufferedImage resize(BufferedImage img, int newW, int newH) { 
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}  
	
	public void keyPressed(KeyEvent kp) {  
		//If Enter Is Pressed
		if(kp.getKeyCode() == KeyEvent.VK_ENTER) { 
			try {
				oos.writeObject(new ChatMessage(chatOut.getText(),p1.getUsername() , "Everyone")); //Sends the chatMessage to everyone
				chatOut.setText(""); //Clear out the box
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
