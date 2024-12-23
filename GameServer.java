/**
This is a template for a Java file.
@author Shaira Sy (226043), Sherrie Del Rosario (222075)
@version May 16, 2023
**/
/*
I have not discussed the Java language code in my program
with anyone other than my instructor or the teaching assistants
assigned to this course.
I have not used Java language code obtained from another student,
or any other unauthorized source, either modified or unmodified.
If any Java language code or documentation used in my program
was obtained from another source, such as a textbook or website,
that has been clearly noted with a proper citation in the comments
of my program.
*/

/**
	The Server file passes data and coordinates between players
	the most important data the server passes is the boolean to check
	if the bomb has collided with a crate for one player and
	passes that information to the other player
	because each player has their own arraylist of crates
	
	it also passes the frame and state of a player's bomb
	to the other player.
**/

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GameServer {
	private ServerSocket ss;
	private int numPlayers;
	private int maxPlayers;
	
	private Socket p1Socket;
	private Socket p2Socket;
	private ReadFromClient p1ReadRunnable;
	private ReadFromClient p2ReadRunnable;
	private WriteToClient p1WriteRunnable;
	private WriteToClient p2WriteRunnable;
	
	private int p1x, p1y, b1x, b1y, b1f;
	private int p2x, p2y, b2x, b2y, b2f;

	private boolean b1b, b2b;
	private int counter, index1, index2;

	public GameServer() {
		System.out.println("==== GAME SERVER ====");
		numPlayers = 0;
		maxPlayers = 2;
		
		//original coordinates of p1 and p2
		p1x = 0;
		p1y = 0;
		p2x = 400;
		p2y = 400;
		
		//original coordinates of bomb 1 and bomb 2
		b1x = 0;
		b1y = 0;
		b2x = 400;
		b2y = 400;
		
		counter = 0;
		
		//server socket
		try {
			ss = new ServerSocket(51234);
		} catch(IOException ex) {
			System.out.println("IOException from GameServer constructor");
		}
	}
	
	/**
		starts the readthreads and writethreads and accepts players into the server
	**/
	public void acceptConnection() {
		try {
			System.out.println("Waiting for connections. . .");
			
			while(numPlayers < maxPlayers) {
				Socket s = ss.accept();
				DataInputStream in = new DataInputStream(s.getInputStream());
				DataOutputStream out = new DataOutputStream(s.getOutputStream());
				
				numPlayers++;
				out.writeInt(numPlayers);
				System.out.println("Player #" + numPlayers + " has connected.");
				
				ReadFromClient rfc = new ReadFromClient(numPlayers, in);
				WriteToClient wtc = new WriteToClient(numPlayers, out);
				
				if (numPlayers == 1) {
					p1Socket = s;
					p1ReadRunnable = rfc;
					p1WriteRunnable = wtc;
				} else {
					p2Socket = s;
					p2ReadRunnable = rfc;
					p2WriteRunnable = wtc;
					
					//once player 2 joins everything will start and the startmsg will be sent
					p1WriteRunnable.sendStartMsg();
					p2WriteRunnable.sendStartMsg();
					Thread readThread1 = new Thread(p1ReadRunnable);
					Thread readThread2 = new Thread(p2ReadRunnable);
					readThread1.start();
					readThread2.start();
					
					Thread writeThread1 = new Thread(p1WriteRunnable);
					Thread writeThread2 = new Thread(p2WriteRunnable);
					writeThread1.start();
					writeThread2.start();
				}
			}
			
			System.out.println("No longer accepting connections");
			
		} catch(IOException ex) {
			System.out.println("IOIException from acceptConnection()");
		}
	}
	
	private class ReadFromClient implements Runnable {
		private int playerID;
		private DataInputStream dataIn;
		
		public ReadFromClient(int pid, DataInputStream in) {
			playerID = pid;
			dataIn = in;
			System.out.println("RFC" + playerID + " Runnable created");
		}
		
		public void run() {
			try {
				while(true) {
					
					/**
						Server receives player 1 and player 2's information 
						It needs player coordinates, bomb coordinates, bomb frame for
						it to show up on the other player's screen
						it needs a boolean and index to show whether the bomb has collided with
						a crate or not
					**/
					if (playerID == 1) {
						p1x = dataIn.readInt();
						p1y = dataIn.readInt();
						b1x = dataIn.readInt();
						b1y = dataIn.readInt();
						b1f = dataIn.readInt();
						b1b = dataIn.readBoolean();
						index1 = dataIn.readInt();
					} else {
						p2x = dataIn.readInt();
						p2y = dataIn.readInt();
						b2x = dataIn.readInt();
						b2y = dataIn.readInt();
						b2f = dataIn.readInt();
						b2b = dataIn.readBoolean();
						index2 = dataIn.readInt();
					}
				}
			} catch(IOException ex) {
				System.out.println("IOException from RFC run()");
			}
		}
	}
	
	private class WriteToClient implements Runnable {
		private int playerID;
		private DataOutputStream dataOut;
		
		public WriteToClient(int pid, DataOutputStream out) {
			playerID = pid;
			dataOut = out;
			System.out.println("WTC" + playerID + " Runnable created");
		}
		
		public void run() {
			try {
				while(true) {
					
					/**
						passes player 1's information to player 2
					**/
					if(playerID == 1) {
						dataOut.writeInt(p2x);
						dataOut.writeInt(p2y);
						dataOut.writeInt(b2x);
						dataOut.writeInt(b2y);
						dataOut.writeInt(b2f);
						dataOut.writeBoolean(b2b);
						dataOut.writeInt(index2);
					} else {
						dataOut.writeInt(p1x);
						dataOut.writeInt(p1y);
						dataOut.writeInt(b1x);
						dataOut.writeInt(b1y);
						dataOut.writeInt(b1f);
						dataOut.writeBoolean(b1b);
						dataOut.writeInt(index1);
					}
					dataOut.flush();
					
					/**
						counter is mainly here to make sure b1b and b2b don't loop
						and continuously give a true to the other player after
						it's done its job
						b1b/b2b only needs to  be true once to remove the index that
						collided with the bomb
						
						after entering the loop, counter will always be >= 1 so it will
						only be true once
					**/
					counter ++;
					
					if (counter >= 1) {
						b1b = false;
						b2b = false;
					}
					
					
					/**
						stops the thread from running too frequently and too fast
					**/
					try {
						Thread.sleep(40);
					} catch(InterruptedException ex) {
						System.out.println("InterruptedException from WTC run()");
					}
				}
			} catch(IOException ex) {
				System.out.println("IOException from WTC run()");
			}
		}
		
		/**
			start message to let both players know that two players have connected to the server
		**/
		public void sendStartMsg() {
			try {
				dataOut.writeUTF("We now have 2 players, Go!");
			} catch(IOException ex) {
				System.out.println("IOException from sendStartMsg()");
			}
		}
	}

	public static void main(String[] args) {
		GameServer gs = new GameServer();
		gs.acceptConnection();
	}

}