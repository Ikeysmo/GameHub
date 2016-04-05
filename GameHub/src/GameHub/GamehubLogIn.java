package GameHub;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

//just here to configure github!
public class GamehubLogIn implements FocusListener {
	private JFrame mainmode = new JFrame("Welcome to GameHub!");
	private JList gamesList = new JList();
	private JLabel welcome = new JLabel("Welcome to GameHub. Please sign in now!");
	private JTextField loginBox = new JTextField("Enter Username   ");
	private JTextField passBox = new JTextField("Enter Password   ");
	private JPanel mainPanel = new JPanel();
	private JButton loginButton = new JButton("Log In!");
	private JButton registerButton = new JButton("Register!");
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
		else if(arg0.getSource() == passBox && passBox.getText().isEmpty())
			passBox.setText("Enter Password   ");
	}

}
