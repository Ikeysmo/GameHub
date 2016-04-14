package gameHub;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
public class GamehubLogIn implements FocusListener, ActionListener, Runnable, ListSelectionListener {
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
	private String ip_Address = "localhost";
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
	
	
	public GamehubLogIn() {
		
		//Try to make the images
		try {
			BufferedImage ticTacToeIcon = ImageIO.read(new File("tic_tac_toe.png"));
			ticTacToeIcon = resize(ticTacToeIcon, 100, 100);
			ticButton = new JButton(new ImageIcon(ticTacToeIcon));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedImage connect4Icon = ImageIO.read(new File("connect4.png"));
			connect4Icon = resize(connect4Icon, 100, 100);
			connButton = new JButton(new ImageIcon(connect4Icon));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		// TODO Auto-generated constructor stub
		//games.setListData(listData);
		//may need something to put list of games in JList??
		welcome.setFont(new Font("Default", Font.BOLD, 20));
		welcome.setForeground(Color.DARK_GRAY);
		mainmode.requestFocus();
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
		mainPanel.add(loginBox);
		mainPanel.add(passBox);
		mainPanel.add(loginButton);
		mainPanel.add(registerButton);
		errormsg.setForeground(Color.red);
		mainmode.getContentPane().add(mainPanel);
		mainmode.getContentPane().add(errormsg, "South");
		mainmode.setSize(550,170);
		mainmode.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainmode.setVisible(true);
		mainmode.requestFocus();
		loginButton.addActionListener(this);
		registerButton.addActionListener(this);
		chatSubmitButton.addActionListener(this);
		loginBox.setText("Enter Username   ");
		shot.add(new JMenuItem("Connect 4"));
		onlineList.addListSelectionListener(this);
		
		
		mainmode.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"clickButton");
		mainmode.getRootPane().getActionMap().put("clickButton", new AbstractAction(){
			public void actionPerformed(ActionEvent ae) 
			{
				loginButton.doClick();
			}
		});
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GamehubLogIn();
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		errormsg.setText("");
		if(arg0.getSource() == loginBox)
			loginBox.setText("");
		else if(arg0.getSource() == passBox)
			passBox.setText("");
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == loginBox && loginBox.getText().isEmpty())
			loginBox.setText("Enter Username");
		//else if(arg0.getSource() == passBox && passBox.getPassword().length == 0)
			//passBox.setText("Enter Password   ");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		errormsg.setText("");
		if(arg0.getSource() == loginButton){
			try {
				if(loginBox.getText().equals("Enter Username")){
					errormsg.setText("Error: Must enter a valid Username!");
					return;
				}
					
				else if(loginBox.getText().equals(""));
				s = new Socket(ip_Address, 2020); //assuming port 4444 for lab 4
				oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(loginBox.getText() + "/" + String.valueOf(passBox.getPassword()));
				ois = new ObjectInputStream(s.getInputStream());
				String serverReply = (String) ois.readObject();
				System.out.println("Server Reply:" + serverReply);
				if(!serverReply.contains("Welcome")){
					errormsg.setText("Error: Incorrect Username or Password");
					return;
				}
				p1 = new PlayerAccount(loginBox.getText(), String.valueOf(passBox.getPassword()));
				mainmode.dispose();
				mainmode = null;
				mainmode = new JFrame("Welcome to GameHub!");
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
				chatHistory.setPreferredSize(new Dimension(450, 470));
				chatOutbox.setPreferredSize(new Dimension(500,60));
				chatOut.setText("Outcoming");
				chatOut.setLineWrap(true);
				//chatOut.setPreferredSize(new Dimension(500, 100));
				//chatOut.set
				chatOut.setFont(new Font("Default", Font.BOLD, 20));
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
				
				new Thread(this).start();
			}
			
			catch(IOException | ClassNotFoundException e){
				errormsg.setText(e.getMessage());
			}
			
		}
		else if (arg0.getSource() == registerButton){
			//put code here
		}
		else if(arg0.getSource() == ticButton){
			try {
				oos.writeObject(new GameInvite(p1.getUsername(), (String) onlineList.getSelectedValue(), "TicTacToe"));
				new TicTacToe(oos, ois);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(arg0.getSource() == connButton){
			try {//send a game invite!
				oos.writeObject(new GameInvite(p1.getUsername(), (String) onlineList.getSelectedValue(), "Connect4"));
				new ConnectFour(oos, ois);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(arg0.getSource() == chatSubmitButton){
			
			try {
				oos.writeObject(new ChatMessage(chatOut.getText(),p1.getUsername() , "Everyone"));
				chatOut.setText(""); //Clear out the box
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//this is where I begin to run the code after login
	
		
		while(true){
			try {
				Object message = ois.readObject();
				System.out.println("Got something!");
				if(message instanceof String[]){
					onlineList.setListData((String[]) message);
				}
				else if(message instanceof GameInvite){
					
					GameInvite invite = (GameInvite) message;
					System.out.println("Got invite from " +invite.from + " to " +invite.to+ "!");
					if(invite.to.equalsIgnoreCase(p1.username)){
						JFrame inviteWindow = new JFrame("Invite");
						inviteWindow.setSize(300, 300);
						inviteWindow.add(new JLabel("You got an invite!"));
						JPanel middlePanel = new JPanel();
						JButton acceptButton = new JButton("Accept");
						acceptButton.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								// TODO Auto-generated method stub
								invite.Accept();
								try {
									oos.writeObject(invite);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								System.out.println("Accepted invite!");
							}
						}); //make local function
						middlePanel.add(acceptButton);
						JButton denyButton = new JButton("Deny");
						denyButton.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								invite.Deny();
								try {
									oos.writeObject(invite);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								System.out.println("Denied invitation!");
							}
						});
						middlePanel.add(denyButton);
						inviteWindow.add(middlePanel);
						inviteWindow.setTitle("Invite from " +invite.from + "!");
						inviteWindow.setVisible(true);
						
					}
				}
				else if(message instanceof ChatMessage){
					System.out.println("Abe Lincoln");
					ChatMessage chat = (ChatMessage) message;
					chatIn.append(chat.from + ": " + chat.message);
				} //this does not CATCH anything that isn't one of these! The games catches the rest!
				
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

}
