package gameHub;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class GameHubWebServer implements Runnable {
	private ServerSocket websocket; 
	private String html_String = ""; 
	private String http_response_part1 = "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: ";
	private String http_response_part2 = "\r\n\r\n";
	public GameHubWebServer() throws IOException {
		FileReader fr = new FileReader("gamehub.html");
		BufferedReader br = new BufferedReader(fr);
		
		while(true){
			String temp = br.readLine();
			if(temp == null)
				break;
			else
				html_String += temp;
		}
		
		//System.out.println(html_String);
		websocket = new ServerSocket(80);
		new Thread(this).start();	
		}

	@Override
	public void run() {
		while(true){
		try {
			Socket s = websocket.accept();
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(http_response_part1+html_String.length()+http_response_part2+html_String);
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

}
