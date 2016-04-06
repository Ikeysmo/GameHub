package GameHub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//just here to configure github!
public class GamehubLogIn implements FocusListener, ActionListener {
	private JFrame mainmode = new JFrame("Welcome to GameHub!");
	private JList gamesList = new JList();
	private JLabel welcome = new JLabel("Welcome to GameHub. Please sign in now!");
	private JTextField loginBox = new JTextField("Enter Username   ");
	private JPasswordField passBox = new JPasswordField("");
	private JPanel mainPanel = new JPanel();
	private JButton loginButton = new JButton("Log In!");
	private JButton registerButton = new JButton("Register!");
	private String ip_Address = "127.0.0.1";
	private Socket s;
	public GamehubLogIn() {
		// TODO Auto-generated constructor stub
		//games.setListData(listData);
		//may need something to put list of games in JList??
		welcome.setFont(new Font("Default", Font.BOLD, 20));
		welcome.setForeground(Color.DARK_GRAY);
		mainmode.requestFocus();
		loginBox.setFont(new Font("Default", Font.BOLD, 20));
		loginBox.addFocusListener(this);
		passBox.setFont(new Font("Default", Font.BOLD, 20));
		passBox.setPreferredSize(new Dimension(150, 30));
		passBox.setMinimumSize(new Dimension(150, 30));
		passBox.addFocusListener(this);
		mainPanel.setBackground(Color.orange);
		mainPanel.add(welcome);
		mainPanel.add(loginBox);
		mainPanel.add(passBox);
		mainPanel.add(loginButton);
		mainPanel.add(registerButton);
		mainmode.getContentPane().add(mainPanel);
		mainmode.setSize(500,150);
		mainmode.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainmode.setVisible(true);
		mainmode.requestFocus();
		loginButton.addActionListener(this);
		registerButton.addActionListener(this);
		loginBox.setText("Enter Username   ");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GamehubLogIn();
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == loginBox)
			loginBox.setText("");
		else if(arg0.getSource() == passBox)
			passBox.setText("");
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == loginBox && loginBox.getText().isEmpty())
			loginBox.setText("Enter Username   ");
		//else if(arg0.getSource() == passBox && passBox.getPassword().length == 0)
			//passBox.setText("Enter Password   ");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == loginButton){
			try {
				s = new Socket("127.0.0.1", 2020); //assuming port 4444 for lab 4
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(loginBox.getText() + "/" + String.valueOf(passBox.getPassword()));
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				String serverReply = (String) ois.readObject();
				System.out.println(serverReply);
				if(!serverReply.contains("Welcome"))
					throw new IllegalArgumentException();
				mainmode.dispose();
				mainmode = null;
				mainmode = new JFrame("Welcome to GameHub!");
				mainmode.setSize(500, 500);
				mainmode.setVisible(true);
			}
			catch(IOException | ClassNotFoundException e){}
			
		}
		else if (arg0.getSource() == registerButton){
			//put code here
		}
	}

}
