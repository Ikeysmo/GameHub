package gameHub;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import connectFour.ConnectFour;
import ticTacToe.TicTacToe;

//just here to configure github!
public class GamehubLogIn implements FocusListener, ActionListener, Runnable, ListSelectionListener {
	private JFrame mainmode = new JFrame("Welcome to GameHub!");
	private JFrame onlineWindow;
	private JList gamesList = new JList();
	private JList onlineList = new JList();
	private JLabel welcome = new JLabel("Welcome to GameHub. Please sign in now!");
	private JTextField loginBox = new JTextField("Enter Username");
	private JPasswordField passBox = new JPasswordField("");
	private JPanel mainPanel = new JPanel();
	private JButton loginButton = new JButton("Log In!");
	private JButton registerButton = new JButton("Register!");
	private JButton ticButton = new JButton("TicTacToe!");
	private JButton connButton = new JButton("Connect4!");
	private JLabel errormsg = new JLabel();
	private JPopupMenu shot = new JPopupMenu();
	private String ip_Address = "127.0.0.1";
	private Socket s;
	private PlayerAccount p1;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	private JPanel gamePanel = new JPanel();
	public GamehubLogIn() {
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
		connButton.setEnabled(false);
		ticButton.setEnabled(false);
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
		loginBox.setText("Enter Username   ");
		shot.add(new JMenuItem("Connect 4"));
		onlineList.addListSelectionListener(this);
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
				s = new Socket("127.0.0.1", 2020); //assuming port 4444 for lab 4
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
				onlineWindow = new JFrame("Online List");
				onlineWindow.setSize(400, 600);
				onlineList.setFont(new Font("Default", Font.BOLD, 30));
				JPanel OnlinePanel = new JPanel();
				onlineWindow.add(onlineList);
				//OnlinePanel.add(shot);
				//onlineWindow.add(OnlinePanel);
				onlineWindow.setVisible(true);
				Point loc = mainmode.getLocation();
				onlineWindow.setLocation(loc.x+500, loc.y);
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
				new TicTacToe();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(arg0.getSource() == connButton){
			try {//send a game invite!
				oos.writeObject(new GameInvite(p1.getUsername(), (String) onlineList.getSelectedValue(), "Connect4"));
				new ConnectFour();
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
				if(message instanceof String[]){
					onlineList.setListData((String[]) message);
				}
				else if(message instanceof GameInvite){
					System.out.println("I got the game invite!");
				}
				else if(message instanceof ChatMessage){
					ChatMessage chat = (ChatMessage) message;
					System.out.println(chat.from + " says " + chat.message);
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

}
