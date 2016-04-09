package chatClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
//Isaiah Smoak ECE 309
public class PictureChatClient implements Runnable, ActionListener, ListSelectionListener{

	public Socket s;
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	private JList<String> whosInList = new JList<String>();
	private JScrollPane whosInScrollPane   = new JScrollPane(whosInList);
	JPanel bottomPanel = new JPanel();
	File localDirectory = new File(System.getProperty("user.dir"));
	JButton previewPicturesButton = new JButton("Preview Pictures To Send");
	JFrame chatWindow = new JFrame(); // the graphics window.
	JFrame myPicturesWindow = new JFrame("My pictures in " + localDirectory);
	JLabel showSelectionLabel = new JLabel("Select a picture to Send");
	JButton clearSelectionButton = new JButton("Clear Selection");
	JList<ImageIcon> myPicturesList = new JList<ImageIcon>();
	JScrollPane picturesScrollPane = new JScrollPane(myPicturesList);
	JButton sendButton = new JButton("SEND");
	JTextArea inChatTextArea  = new JTextArea();
	JTextArea outChatTextArea = new JTextArea();
	JScrollPane inScrollPane = new JScrollPane(inChatTextArea);
	JScrollPane outScrollPane = new JScrollPane(outChatTextArea);
	JSplitPane  splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inScrollPane, outScrollPane);
	JFrame whosInWindow = new JFrame();
	JLabel infoLabel = new JLabel();
	String chatName;
	
	private String newLine = System.lineSeparator();
	//what about send button being different???
	//Isaiah Smoak ECE 309	
	public PictureChatClient(String serverAddress, String chatName, String password)throws Exception {

		System.out.println("Local directory is " + localDirectory);
		// TODO Auto-generated constructor stub
		s = new Socket(serverAddress, 6666); //assuming port 4444 for lab 4
		oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(chatName + "/" + password);
		ois = new ObjectInputStream(s.getInputStream());
		String serverReply = (String) ois.readObject();
		System.out.println(serverReply);
		if(!serverReply.startsWith("Welcome")){
			throw new IllegalArgumentException(serverReply); //and terminate!!!
		}
		this.chatName = chatName;
		//at this point join was successful...
		//BUILD GUI
		//start thread
		new Thread(this).start(); //begin execution in run()
		whosInWindow.setLocation(400, 100);
		whosInWindow.setVisible(true);
		whosInWindow.setSize(400, 800);
		whosInWindow.getContentPane().add(whosInScrollPane, "Center");
		whosInWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		chatWindow.getContentPane().add(splitPane, "Center");
		splitPane.setDividerLocation(400);
		outChatTextArea.setEditable(false);
		inChatTextArea.setFont(new Font("default", Font.BOLD, 20));
		outChatTextArea.setFont(new Font("default", Font.BOLD, 20));
		infoLabel.setFont(new Font("default", Font.BOLD, 20));
		infoLabel.setForeground(Color.red);
		inChatTextArea.setLineWrap(true);
		outChatTextArea.setLineWrap(true);
		inChatTextArea.setWrapStyleWord(true);
		outChatTextArea.setWrapStyleWord(true);
		inChatTextArea.setText("Enter chat here to be sent.");
		outChatTextArea.setText("Recieved chat will be displayed here. (You can move the separator bar!)");
		
		chatWindow.setSize(1000, 800);
		chatWindow.setVisible(true);
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		infoLabel.setText(chatName+ " send a message by submit button, or press alt-ENTER to send. When done, close window to leave!");
		sendButton.setMnemonic(KeyEvent.VK_ENTER);
		sendButton.addActionListener(this); //give prog addr to button
		sendButton.setVisible(true);
		previewPicturesButton.addActionListener(this);
		bottomPanel.setLayout(new GridLayout(1,2));
		bottomPanel.add(sendButton);
		bottomPanel.add(previewPicturesButton);
		chatWindow.getContentPane().add(bottomPanel, "South");
		chatWindow.getContentPane().add(infoLabel, "North");
		myPicturesWindow.getContentPane().add(clearSelectionButton, "North");
		myPicturesWindow.getContentPane().add(picturesScrollPane, "Center");
		myPicturesWindow.getContentPane().add(showSelectionLabel, "South");
		showSelectionLabel.setForeground(Color.blue);
		clearSelectionButton.setForeground(Color.white); //commented out just for giggles!
		clearSelectionButton.setBackground(Color.blue);
		clearSelectionButton.addActionListener(this);
		myPicturesList.setSelectionMode(0); //single-select
		myPicturesWindow.setSize(400, 600);
		myPicturesWindow.setLocation(700, 100);
		myPicturesList.addListSelectionListener(this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Chat client thread is running.");
		receiveChat();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Isaiah Smoak - ECE 309");
		if (args.length != 3){
			System.out.println("Error, not given 3 arguments! Give server address, chat name, and password");
			return;
		}
		try{
			new PictureChatClient(args[0], args[1], args[2]);
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}

	private void receiveChat(){

		System.out.println("In receiveChat(); waiting for messages from the server.");
		try{
			while(true) //do forever loop
			{	
				
				Object chatMessage = ois.readObject(); // wait...
			       if (chatMessage instanceof String)
			          {
			          outChatTextArea.append(newLine + chatMessage);
			          outChatTextArea.setCaretPosition(outChatTextArea.getDocument().getLength());
			          }
			       if (chatMessage instanceof String[]) // an *array* of Strings!
			          {
			      	  String[] whosInArray = (String[]) chatMessage;
			      	  whosInList.setListData(whosInArray);
			          }
			       if (chatMessage instanceof ImageIcon){
			    	   ImageIcon recievedPicture = (ImageIcon) chatMessage;
			    	   Vector<ImageIcon> pictureList = new Vector<ImageIcon>();
			    	   pictureList.addElement(recievedPicture);
			    	   outChatTextArea.append(newLine + recievedPicture);
				       outChatTextArea.setCaretPosition(outChatTextArea.getDocument().getLength());
			    	   JFrame receivedPictureWindow = new JFrame();
			    	   receivedPictureWindow.setVisible(true);
			    	   receivedPictureWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			    	   JList<ImageIcon> pic_display = new JList<ImageIcon>();
			    	   pic_display.setListData(pictureList);
			    	   receivedPictureWindow.getContentPane().add(pic_display, "Center");
				       
			    	   //last step
			    	   receivedPictureWindow.setTitle(recievedPicture.toString());
			    	   receivedPictureWindow.setSize(recievedPicture.getImage().getWidth(receivedPictureWindow), recievedPicture.getImage().getHeight(receivedPictureWindow)+50);
			    	   receivedPictureWindow.setLocation(chatWindow.getLocation().x, chatWindow.getLocation().y+chatWindow.getHeight());
			       }
			}
		}
		
		catch(Exception e)
		{
			outChatTextArea.setText("Connection to the ChatServer has failed. You must restart to reconnect. " + e.toString());
			//this will be place that will first see error
			//if something happens to socket connecting client/server
			//put error message on GUI to say we're done!
		}
	}
	private void sendChat(String chatMessage){
		System.out.println("Sending " + chatMessage);
		try{
			oos.writeObject(chatMessage);
			inChatTextArea.setText(""); //clear input area
			inChatTextArea.requestFocus(); //put cursor in input area
		}
		catch(IOException ioe)
		{
			outChatTextArea.setText(ioe.toString());
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {//sendButton will call here.
		// TODO Auto-generated method stub
		if(arg0.getSource() == sendButton){
			System.out.println("sendButton pushed");
			String chatMessage = inChatTextArea.getText().trim();
			if(myPicturesList.isSelectionEmpty()){
				if(chatMessage.equals("Enter chat here to be sent.") || chatMessage.equals("No chat entered and no picture selected.") || chatMessage.isEmpty()){ 
					inChatTextArea.setText("No chat entered and no picture selected.");
					return;}
				else{
					sendChat(chatMessage);
				}
				//continue past to sending!
			}
			else{ //there is an image being sent
				//chatMessage = chatMessage + " " + myPicturesList.getSelectedValue();
				try {
					oos.writeObject(myPicturesList.getSelectedValue());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace(); //failed to send the image!
				}
				if(!chatMessage.equals("Enter chat here to be sent.") && !chatMessage.equals("No chat entered and no picture selected.") && !chatMessage.isEmpty()){ 
					sendChat(chatMessage); //I don't want to send blank
				}
			}
			myPicturesList.clearSelection();
			showSelectionLabel.setText("Select a picture to send");
			System.out.println("System: " + chatMessage);
			
		}
		if(arg0.getSource() == previewPicturesButton){
			String[] listOfFiles = localDirectory.list();
			Vector<ImageIcon> pictures = new Vector<ImageIcon>();
			for(int i = 0; i < listOfFiles.length; i++){
				String fileName = listOfFiles[i];
				if(fileName.endsWith(".jpg") ||fileName.endsWith(".png") ||fileName.endsWith(".gif"))
					pictures.add(new ImageIcon(fileName, fileName + " from " + chatName)); //Image file name, descripton
			}
			if(pictures.isEmpty()){
				System.out.println("No pictures are found in the local directory.");
				return;
			}
			myPicturesList.setListData(pictures);
			myPicturesWindow.setVisible(true);
			System.out.println("Pictures in the local directory are " + pictures);
			System.out.println("previewPicturesButton pushed");
		}
		if(arg0.getSource() == clearSelectionButton){
			myPicturesList.clearSelection();
			showSelectionLabel.setText("Select a picture to send");
		}
		
		return; // to the send button
	}

	@Override
	public void valueChanged(ListSelectionEvent lse) {
		// TODO Auto-generated method stub
		if(lse.getValueIsAdjusting()) return; //user is still selecting
		ImageIcon selectedPicture = myPicturesList.getSelectedValue();
		if(selectedPicture == null) return; //selection was removed
		showSelectionLabel.setText("Selected picture is: " + selectedPicture.getDescription());
	}
	

}
