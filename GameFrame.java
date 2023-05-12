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
	private boolean up, down, left, right, space;
	private Player me, enemy;
	private ArrayList<Crate> unMoving;
	private int playerID;
	private Bomb bomb;

	//for server
	private Socket socket;
	private ReadFromServer rfsRunnable;
	private WriteToServer wtsRunnable;

	public GameFrame(int w, int h) {
		width = w;
		height = h;
		frame = new JFrame();
		gCanvas = new GameCanvas(w, h);
		Start = new JButton("START");
		Controls = new JButton("CONTROLS");
		speed = 25;
	}
	
	public void setUpGUI() {
		contentPane = frame.getContentPane();
		buttonsPanel = new JPanel();
		createPlayers();

		/*Setting up the actual GUI was pretty fun when you know what you can mess with without messing up the program*/
		/** buttonsPanel.setLayout(new GridLayout(1,2));
		buttonsPanel.add(Start);
		buttonsPanel.add(Controls);
	
		contentPane.add(buttonsPanel, BorderLayout.SOUTH); **/
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
		if(playerID == 1){
			me = gCanvas.getUser();
			enemy = gCanvas.getUser2();
		} else{
			enemy = gCanvas.getUser();
			me = gCanvas.getUser2();
		}	
	}
	
	private void setUpKeyListener() {
		KeyListener kl = new KeyListener() {
			public void keyTyped(KeyEvent ke) {
			}
			
			public void keyPressed(KeyEvent ke) {
				int keyCode = ke.getKeyCode();
				
				switch(keyCode) {
					case KeyEvent.VK_W :
						up = true;
						break;
					case KeyEvent.VK_S :
						down = true;
						break;
					case KeyEvent.VK_A :
						left = true;
						break;
					case KeyEvent.VK_D :
						right = true;
						break;
					case KeyEvent.VK_SPACE :
						space = true;
						break;
				}
			}
			
			public void keyReleased(KeyEvent ke) {
				int keyCode = ke.getKeyCode();
				
				switch(keyCode) {
					case KeyEvent.VK_W :
						up = false;
						break;
					case KeyEvent.VK_S :
						down = false;
						break;
					case KeyEvent.VK_A :
						left = false;
						break;
					case KeyEvent.VK_D :
						right = false;
						break;
					case KeyEvent.VK_SPACE :
						space = false;
						break;
				}
			}
		};
		contentPane.addKeyListener(kl);
		contentPane.setFocusable(true);
	}
	
	
	private void setUpAnimationTimer() {
		int interval = 30;
		
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if(up && !down && !left && !right) {
					me.moveV(-speed);
					me.spriteChange();
					gCanvas.repaint();
				} else if(down && !up && !left && !right) {
					me.moveV(speed);
					me.spriteChange();
					gCanvas.repaint();	
				} else if(left && !up && !down && !right) {
					me.moveH(-speed);
					me.spriteChange();
					gCanvas.repaint();
				} else if(right && !up && !down && !left) {
					me.moveH(speed);
					me.spriteChange();
					gCanvas.repaint();
					
				} else if(space) { //create a bomb
					Bomb bomb = gCanvas.newBomb(me.getX(), me.getY());
				//	for(int i=0; i<=6; i++){
						 //bomb.bombTime(1);
						 
						// gCanvas.repaint();	
						// // gCanvas.repaint();	
						// // try{
						// // 	Thread.sleep(3000);
						// // } catch(Exception e){
						// // 	e.printStackTrace();
						// // }
						//  bomb.bombTime(2);
						// // gCanvas.repaint();
						// Timer time1 = new Timer(1000, new ActionListener(){
						// 	@Override
						// 	public void actionPerformed(ActionEvent e){
						// 		// for (int i=1; i<=6; i++){
						// 		// 	bomb.bombTime(i);
						// 			gCanvas.repaint();
						// 		// }
						// 	}
						// });	
							bomb.bombTime(1);
							gCanvas.repaint();
							Timer timer = new Timer(1000, new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent e){
									//for (int i=1; i<=6; i++){
										//
										gCanvas.repaint();
									// }
								}
							});	
							// 
							timer.setInitialDelay(300);
							timer.start(); 
						
						
						// 	
						
						
						
						
						
						
					//}
					
					System.out.println("boom");
				}
				
				//border collision to the edge of the frame
				if (me.getX() + me.getWidth() > width) { //if player is too right
					me.moveH(-speed);
					me.spriteChange();
					gCanvas.repaint();	
				} else if (me.getX() < 0) { //if player too left
					me.moveH(speed);
					me.spriteChange();
					gCanvas.repaint();	
				} else if (me.getY() + me.getHeight() > height) { //if player too down
					me.moveV(-speed);
					me.spriteChange();
					gCanvas.repaint();	
				} else if (me.getY() < 0) { // if player too up
					me.moveV(speed);
					me.spriteChange();
					gCanvas.repaint();	
				}
				unMoving = gCanvas.getUnmoving();
				// Goes through ArrayList of Collideable
				for ( Thing o : unMoving ) {
					// checks for collision
					// won't go through this if False
					// will bug out if u press keys at the same time
					if ( me.isColliding(o) ) {
						if ( right ) {
							// collision on left
							me.moveH(-speed);
							me.spriteChange();
							gCanvas.repaint();
						} else if ( left ) {
							// collision on right
							me.moveH(speed);
							me.spriteChange();
							gCanvas.repaint();
						} else if ( down ) {
							// collision on up
							me.moveV(-speed);
							me.spriteChange();
							gCanvas.repaint();
						} else if ( up ) {
							// collision on down
							me.moveV(speed);
							me.spriteChange();
							gCanvas.repaint();
						}
					}
				}	
			}
		};
		animationTimer = new Timer(interval, al);
		animationTimer.start();
	}

	//taken from choobtorials
	public void connectToServer() {
		try {
 			socket = new Socket("localhost", 51234);
 			DataInputStream in = new DataInputStream(socket.getInputStream());
 			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
 			playerID = in.readInt();
 			System.out.println("You are player #" + playerID);
 			if(playerID == 1) {
 				System.out.println("Waiting for Player #2 to connect. . .");
		}
 			rfsRunnable = new ReadFromServer(in);
 			wtsRunnable = new WriteToServer(out);
 			rfsRunnable.waitForStartMsg();
 		} catch(IOException ex) {
 			System.out.println("IOException from connectToServer()");
 		}
 	}
	
 	private class ReadFromServer implements Runnable {
		private DataInputStream dataIn;
		
		public ReadFromServer(DataInputStream in) {
			dataIn = in;
			System.out.println("RFS Runnable created");
		}
		
		public void run() {
			try {
				while(true) {
					int enemyX = dataIn.readInt();
					int enemyY = dataIn.readInt();
 					if (enemy != null) {
						enemy.setX(enemyX);
						enemy.setY(enemyY);
					}
				}
			} catch(IOException ex) {
				System.out.println("IOException from RFS run()");
			}
		}
		
		public void waitForStartMsg() {
			try {
				String startMsg = dataIn.readUTF();
				System.out.println("Message from server: " + startMsg);
				Thread readThread = new Thread(rfsRunnable);
				Thread writeThread = new Thread(wtsRunnable);
				readThread.start();
				writeThread.start();
			} catch(IOException ex) {
				System.out.println("IOException from waitForStartMsg()");
			}
		}
	}
	
	private class WriteToServer implements Runnable {
		private DataOutputStream dataOut;
		
		public WriteToServer(DataOutputStream out) {
			dataOut = out;
			System.out.println("WTS Runnable created");
		}
		
		public void run() {
			try {
				while(true) {
					if (me != null)  {
						dataOut.writeDouble(me.getX());
						dataOut.writeDouble(me.getY());
						dataOut.flush();
					}
					
					try {
						Thread.sleep(25);
					} catch(InterruptedException ex) {
						System.out.println("InterruptedException from WTS run()");
					}
				}
			} catch(IOException ex) {
				System.out.println("IOException from WTS run()");
			}
		}
	}
}