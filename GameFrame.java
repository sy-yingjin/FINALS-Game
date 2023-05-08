import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; 
import java.io.*;
import java.net.*;


public class GameFrame {
	private Container contentPane;
	private JFrame frame;
	private GameCanvas gCanvas;
	private JButton Start;
	private JButton Controls;
	private JPanel buttonsPanel;
	private Timer animationTimer;
	private int speed, width, height;
	private boolean up, down, left, right, free;
	private int playerID;
	private Player me,enemy;
	private ArrayList<Player> players;
	private ArrayList<Player> unMoving;
	

	//for server
	// private Socket socket;
	// private ReadFromServer rfsRunnable;
	// private WriteToServer wtsRunnable;

	public GameFrame(int w, int h) {
		width = w;
		height = h;
		frame = new JFrame();
		gCanvas = new GameCanvas(w, h);
		Start = new JButton("START");
		Controls = new JButton("CONTROLS");
		speed = 25;
		free = true;
	}
	
	public void setUpGUI() {
		contentPane = frame.getContentPane();
		buttonsPanel = new JPanel();
		createPlayers();

		/*Setting up the actual GUI was pretty fun when you know what you can mess with without messing up the program*/
		buttonsPanel.setLayout(new GridLayout(1,2));
		buttonsPanel.add(Start);
		buttonsPanel.add(Controls);
	
		contentPane.add(buttonsPanel, BorderLayout.SOUTH);
		contentPane.add(gCanvas, BorderLayout.CENTER);
		
		frame.setTitle("Bomb-A Man");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);	
		setUpAnimationTimer();	
		setUpKeyListener();
	}

	//creates players by accessing the player arraylist from GameCanvas
	//some lines are commented since no networking stuffs yet
	private void createPlayers(){
		players = gCanvas.getPlayers();
		Player chickenNuggets = players.get(0);
		me = chickenNuggets;
		//Player spicyNuggets = players.get(1);

		if(playerID == 1){
			me = chickenNuggets;
			//enemy = spicyNuggets;
			
		}
		// else{
		// 	enemy = chickenNuggets;
		// 	me = spicyNuggets;
		// }	
	}

	// private void getObjects(){
	// 	unMoving = gCanvas.getUnmoving();
	// 	for (Object o : unMoving){

	// 	}

	// }
	
	private void setUpKeyListener() {
		KeyListener kl = new KeyListener() {
			public void keyTyped(KeyEvent ke) {
			}
			
			public void keyPressed(KeyEvent ke) {
				int keyCode = ke.getKeyCode();
				
				switch(keyCode) {
					case KeyEvent.VK_UP :
						up = true;
						break;
					case KeyEvent.VK_DOWN :
						down = true;
						break;
					case KeyEvent.VK_LEFT :
						left = true;
						break;
					case KeyEvent.VK_RIGHT :
						right = true;
						break;
				}
			}
			
			public void keyReleased(KeyEvent ke) {
				int keyCode = ke.getKeyCode();
				
				switch(keyCode) {
					case KeyEvent.VK_UP :
						up = false;
						break;
					case KeyEvent.VK_DOWN :
						down = false;
						break;
					case KeyEvent.VK_LEFT :
						left = false;
						break;
					case KeyEvent.VK_RIGHT :
						right = false;
						break;
				}
			}
		};
		contentPane.addKeyListener(kl);
		contentPane.setFocusable(true);
	}
	
	private void setUpAnimationTimer() {
		int interval = 50;
		
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				unMoving = gCanvas.getUnmoving();
				if(up) {
					me.moveV(-speed);
					me.spriteChange();
				} else if(down) {
					me.moveV(speed);
					me.spriteChange();
				} else if(left) {
					me.moveH(-speed);
					me.spriteChange();
				} else if(right) {
					me.moveH(speed);
					me.spriteChange();
				}
				gCanvas.repaint();				
			}
		};
		animationTimer = new Timer(interval, al);
		animationTimer.start();
	}

// //taken from choobtorials
// 	private void connectToServer() {
// 		try {
// 			socket = new Socket("localhost", 51234);
// 			DataInputStream in = new DataInputStream(socket.getInputStream());
// 			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
// 			playerID = in.readInt();
// 			System.out.println("You are player #" + playerID);
// 			if(playerID == 1) {
// 				System.out.println("Waiting for Player #2 to connect. . .");
// 			}
// 			rfsRunnable = new ReadFromServer(in);
// 			wtsRunnable = new WriteToServer(out);
// 			rfsRunnable.waitForStartMsg();
// 		} catch(IOException ex) {
// 			System.out.println("IOException from connectToServer()");
// 		}
// 	}
	
// 	private class ReadFromServer implements Runnable {
// 		private DataInputStream dataIn;
		
// 		public ReadFromServer(DataInputStream in) {
// 			dataIn = in;
// 			System.out.println("RFS Runnable created");
// 		}
		
// 		public void run() {
// 			try {
// 				while(true) {
// 					double enemyX = dataIn.readDouble();
// 					double enemyY = dataIn.readDouble();
// 					if (enemy != null) {
// 						enemy.setX(enemyX);
// 						enemy.setY(enemyY);
// 					}
// 				}
// 			} catch(IOException ex) {
// 				System.out.println("IOException from RFS run()");
// 			}
// 		}
		
// 		public void waitForStartMsg() {
// 			try {
// 				String startMsg = dataIn.readUTF();
// 				System.out.println("Message from server: " + startMsg);
// 				Thread readThread = new Thread(rfsRunnable);
// 				Thread writeThread = new Thread(wtsRunnable);
// 				readThread.start();
// 				writeThread.start();
// 			} catch(IOException ex) {
// 				System.out.println("IOException from waitForStartMsg()");
// 			}
// 		}
// 	}
	
// 	private class WriteToServer implements Runnable {
// 		private DataOutputStream dataOut;
		
// 		public WriteToServer(DataOutputStream out) {
// 			dataOut = out;
// 			System.out.println("WTS Runnable created");
// 		}
		
// 		public void run() {
// 			try {
// 				while(true) {
// 					if (me != null)  {
// 						dataOut.writeDouble(me.getX());
// 						dataOut.writeDouble(me.getY());
// 						dataOut.flush();
// 					}
					
// 					try {
// 						Thread.sleep(25);
// 					} catch(InterruptedException ex) {
// 						System.out.println("InterruptedException from WTS run()");
// 					}
// 				}
// 			} catch(IOException ex) {
// 				System.out.println("IOException from WTS run()");
// 			}
// 		}
// 	}
}