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
	
	private int crateStatus;
	private int crateIndex;
	private boolean explode, destroy, bombSet1, bombSet2;
	
	private int p1x, p1y, b1x, b1y, b1f;
	private int p2x, p2y, b2x, b2y, b2f;
	
	private boolean b1b, b2b, blown;

	private int crateX, crateY, crateI;

	
	
	public GameServer() {
		System.out.println("==== GAME SERVER ====");
		numPlayers = 0;
		maxPlayers = 2;
		
		p1x = 0;
		p1y = 0;
		p2x = 400;
		p2y = 400;
		
		b1x = 0;
		b1y = 0;
		b2x = 400;
		b2y = 400;
		
		crateStatus = 0;
		crateIndex = 0;
		
		try {
			ss = new ServerSocket(51234);
		} catch(IOException ex) {
			System.out.println("IOException from GameServer constructor");
		}
	}
	
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
					if(playerID == 1) {
						p1x = dataIn.readInt();
						p1y = dataIn.readInt();
						b1x = dataIn.readInt();
						b1y = dataIn.readInt();
						b1f = dataIn.readInt();
						b1b = dataIn.readBoolean();
					} else {
						p2x = dataIn.readInt();
						p2y = dataIn.readInt();
						b2x = dataIn.readInt();
						b2y = dataIn.readInt();
						b2f = dataIn.readInt();
						b2b = dataIn.readBoolean();
					}
					
					crateX = dataIn.readInt();
					crateY = dataIn.readInt();
					crateI = dataIn.readInt();
					
					if (b1b) {
						if (
								// checks middle explosion
							b1x + 100 > crateX || // from left side
							b1x < crateX + 100 || // from right side
							b1y + 100 > crateY || // from top side
							b1y < crateY + 100 ) { // from bottom side
							blown = true;
							break;
						} else if (
								// checks right explosion
							b1x+100 + 100 > crateX || // from left side
							b1x+100 < crateX + 100 || // from right side
							b1y + 100 > crateY || // from top side
							b1y < crateY + 100 ) { // from bottom side
							blown = true;
							break;
						} else if ( 
								//  checks left explosion
							b1x-100 + 100 > crateX || // from left side
							b1x-100 < crateX + 100 || // from right side
							b1y + 100 > crateY || // from top side
							b1y < crateY + 100 ) { // from bottom side
							blown = true;
							break;
						} else if ( 
								//  checks top explosion
							b1x + 100 > crateX || // from left side
							b1x < crateX + 100 || // from right side
							b1y-100 + 100 > crateY || // from top side
							b1y-100 < crateY + 100 ) { // from bottom side
							blown = true;
							break;
						} else if ( 
								//  checks bottom explosion
							b1x + 100 > crateX || // from left side
							b1x < crateX + 100 || // from right side
							b1y+100 + 100 > crateY || // from top side
							b1y+100 < crateY + 100 ) { // from bottom side
							blown = true;
							break;
						} else {
							blown = false;
						}
					}
					
					if (b2b) {
						if (
								// checks middle explosion
							b2x + 100 <= crateX || // from left side
							b2x >= crateX + 100 || // from right side
							b2y + 100 <= crateY || // from top side
							b2y >= crateY + 100 ) { // from bottom side
							blown = false;
							System.out.println("Middle part hit");
							break;
						} else if (
								// checks right explosion
							b2x+100 + 100 <= crateX || // from left side
							b2x+100 >= crateX + 100 || // from right side
							b2y + 100 <= crateY || // from top side
							b2y >= crateY + 100 ) { // from bottom side
							blown = false;
							System.out.println("Right part hit");
							break;
						} else if ( 
								//  checks left explosion
							b2x-100 + 100 <= crateX || // from left side
							b2x-100 >= crateX + 100 || // from right side
							b2y + 100 <= crateY || // from top side
							b2y >= crateY + 100 ) { // from bottom side
							blown = false;
							System.out.println("Left part hit");
							break;
						} else if ( 
								//  checks top explosion
							b2x + 100 <= crateX || // from left side
							b2x >= crateX + 100 || // from right side
							b2y-100 + 100 <= crateY || // from top side
							b2y-100 >= crateY + 100 ) { // from bottom side
							blown = false;
							System.out.println("Top part hit");
							break;
						} else if ( 
								//  checks bottom explosion
							b2x + 100 <= crateX || // from left side
							b2x >= crateX + 100 || // from right side
							b2y+100 + 100 <= crateY || // from top side
							b2y+100 >= crateY + 100 ) { // from bottom side
							blown = false;
							break;
						} else {
							blown = true;
							System.out.println("You missed bozo");
						}
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
					if(playerID == 1) {
						dataOut.writeInt(p2x);
						dataOut.writeInt(p2y);
						dataOut.writeInt(b2x);
						dataOut.writeInt(b2y);
						dataOut.writeInt(b2f);
					} else {
						dataOut.writeInt(p1x);
						dataOut.writeInt(p1y);
						dataOut.writeInt(b1x);
						dataOut.writeInt(b1y);
						dataOut.writeInt(b1f);

					}
					
					dataOut.writeBoolean(blown);
					dataOut.writeInt(crateI);
					dataOut.flush();
				
					try {
						Thread.sleep(20);
					} catch(InterruptedException ex) {
						System.out.println("InterruptedException from WTC run()");
					}
				}
			} catch(IOException ex) {
				System.out.println("IOException from WTC run()");
			}
		}
		
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