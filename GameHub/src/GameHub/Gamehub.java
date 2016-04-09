package GameHub;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import ticTacToe.BoardPanel;
import ticTacToe.TicTacToePlayer;

public class Gamehub extends JPanel implements ActionListener, Runnable {
	JFrame gameMenu = null;
	JRadioButton pvp = new JRadioButton("Local PVP");
	JRadioButton playerCPUE = new JRadioButton("Player v CPU(easy)");
	JRadioButton playerCPUH = new JRadioButton("Player v CPU(hard)");
	JRadioButton pvpOnline = new JRadioButton("Remote PVP");
	JButton newGame = new JButton("New Game");
	JPanel TicTacToePanel = new JPanel();
	JPanel TicTacToemenubar = new JPanel();
	JLabel instr1 = new JLabel("Remote Player IP ADDRESS:");
	JLabel TicTacToeerrorMsg = new JLabel();
	JTextField ip_field = new JTextField();
	JTextField player1name = new JTextField("Player 1 Name");
	JTextField player2name = new JTextField("Player 2 Name");
	JPanel secondPanel = new JPanel();
	JFrame mudda;
	BoardPanel foo;
	
	JPanel games = new JPanel();
	JRadioButton ticTacToe = new JRadioButton("Tic-Tac-Toe");
	JRadioButton connectFour =  new JRadioButton("ConnectFour");
	
	public int turn = 0;
	private Player[] players;
	private char[][] board;
	
	public Gamehub() {
		super(new GridLayout(1, 1));
		
		// TODO Auto-generated constructor stub
		gameMenu = new JFrame("Game Hub!");
		gameMenu.setSize(600, 160);
		gameMenu.setMinimumSize(new Dimension(500,180));
		gameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pvp.addActionListener(this);
		playerCPUE.addActionListener(this);
		playerCPUH.addActionListener(this);
		pvpOnline.addActionListener(this);
		newGame.addActionListener(this);
		ButtonGroup group = new ButtonGroup();
		group.add(pvp);
		group.add(playerCPUE);
		group.add(playerCPUH);
		group.add(pvpOnline);
		TicTacToemenubar.add(pvp);
		TicTacToemenubar.add(playerCPUE);
		TicTacToemenubar.add(playerCPUH);
		TicTacToemenubar.add(pvpOnline);
		TicTacToemenubar.add(newGame);
		TicTacToePanel.add(TicTacToemenubar);
		TicTacToeerrorMsg.setFont(new Font("Default", Font.BOLD, 15));
		TicTacToeerrorMsg.setForeground(Color.red);
		instr1.setFont(new Font("Default", Font.BOLD, 15));
		ip_field.setFont(new Font("Default", Font.BOLD, 15));
		ip_field.setPreferredSize(new Dimension(150,20));
		ip_field.setEditable(false);
		player1name.setPreferredSize(new Dimension(150,20));
		player1name.setFont(new Font("Default", Font.BOLD, 15));
		pvp.setSelected(true);
		player2name.setPreferredSize(new Dimension(150,20));
		player2name.setFont(new Font("Default", Font.BOLD, 15));
		TicTacToePanel.add(instr1);
		TicTacToePanel.add(ip_field);
		TicTacToePanel.add(Box.createRigidArea(new Dimension(100,0)));
		TicTacToePanel.add(player1name);
		TicTacToePanel.add(player2name);
		TicTacToePanel.add(TicTacToeerrorMsg);
		
		ticTacToe.addActionListener(this);
		connectFour.addActionListener(this);
		games.add(ticTacToe);
		games.add(connectFour);
		
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("images/middle.gif");
         
        JComponent panel1 = makeTextPanel("Panel #1");
        tabbedPane.addTab("Tic-Tac-Toe", icon, TicTacToePanel,
                "Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
         
        JComponent panel2 = makeTextPanel("Panel #2");
        tabbedPane.addTab("ConnectFour", icon, TicTacToeerrorMsg,
                "Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
         
        //Add the tabbed pane to this panel.
        add(tabbedPane);
         
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		gameMenu.add(tabbedPane);
		//gameMenu.getContentPane().add(TicTacToePanel,"North");
		//gameMenu.getContentPane().add(secondPanel, "Center");
		//gameMenu.getContentPane().add(TicTacToeerrorMsg, "South");
		//gameWindow.getContentPane().add(new BoardPanel(),"Center");

		gameMenu.setVisible(true);
	}
	
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
     
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TabbedPaneDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
     
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        //Add content to the window.
        frame.add(new TabbedPaneDemo(), BorderLayout.CENTER);
         
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	public static void main(String[] args) {
		new Gamehub();
	}
	
}
