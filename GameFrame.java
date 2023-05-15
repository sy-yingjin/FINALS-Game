import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; 
import java.io.*;
import java.net.*;

public class GameFrame extends JFrame {
	private Container contentPane;
	private GameCanvas gCanvas;
	private Timer animationTimer;
	private int speed, width, height, crateIndex;
	private boolean up, down, left, right, space, start, restart;
	private boolean max, bombCounter, bombSet, gameOver;
	private boolean explode, destroy1, destroy2;
	private Player me, enemy;
	private ArrayList<Crate> bombable;
	private ArrayList<Wall> unMovable;
	private int playerID;
	private Bomb myBomb, enemyBomb;
	private Timer timer, timerEnemy;
	private int crateX, crateY, crateI;
	
	//for server
	private Socket socket;
	private ReadFromServer rfsRunnable;
	private WriteToServer wtsRunnable;

	public GameFrame(int w, int h) {
		width = w;
		height = h;
		gCanvas = new GameCanvas(w, h);
		speed = 25;
		
		bombable = gCanvas.getBombable();
		unMovable = gCanvas.getUnmovable();
	}
	
	public void setUpGUI() {
		contentPane = this.getContentPane();
		contentPane.setPreferredSize(new Dimension(width,height));
		createPlayers();
		gCanvas.setScreen(1);
		contentPane.add(gCanvas, BorderLayout.CENTER);
		
		this.setTitle("Fry-A-Chick! Player#" + playerID);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);	
		
		setUpAnimationTimer();	
		setUpKeyListener();
	}

	//creates players by accessing the player arraylist from GameCanvas
	//some lines are commented since no networking stuffs yet
	private void createPlayers(){
		if(playerID == 1){
			me = gCanvas.getUser();
			enemy = gCanvas.getUser2();
			myBomb = gCanvas.getBomb1();
			enemyBomb = gCanvas.getBomb2();
		} else{
			enemy = gCanvas.getUser();
			me = gCanvas.getUser2();
			enemyBomb = gCanvas.getBomb1();
			myBomb = gCanvas.getBomb2();
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
					case KeyEvent.VK_O :
						start = true;
						gCanvas.setScreen(0);
						break;
					case KeyEvent.VK_K :
						restart = true;
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
				if(start){
					gCanvas.repaint();
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
						
					} else if(space && bombCounter == false) {
						//create a bomb
						myBomb.setX(me.getX());
						myBomb.setY(me.getY());
						bombSet = true;
						bombCounter = true;
						
						while(bombSet){
							timer = new Timer(80, new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent e){
									int check = myBomb.checkCounter();
									if (check <= 20) {
										myBomb.setFrame(1);
										myBomb.addCounter();
										gCanvas.repaint();
									} else if (check > 20 && check <= 40) {
										myBomb.setFrame(2);
										myBomb.addCounter();
										gCanvas.repaint();
									} else if (check > 40 && check <= 50) {
										myBomb.setFrame(3);
										myBomb.addCounter();
										gCanvas.repaint();
										
										for (Crate c : bombable){
											crateX = c.getX();
											crateY = c.getY();
											crateI = bombable.indexOf(c);
										}
										
										// for (Crate c : bombable) {
										// 	if(myBomb.rangeCheck(c)){
										// 		c.setType(status);
										// 		explode = true;
										// 		gCanvas.repaint();
										// 		if (check == 50){
										// 		crateIndex = bombable.indexOf(c);
										// 		System.out.println("Index: " + crateIndex);
													
										// 			gCanvas.removeCrate(bombable.indexOf(c));
										// 			System.out.println("ArraySize = "+ bombable.size());
										// 			explode = false;
										// 			destroy1 = true;
										// 			gCanvas.repaint();			
										// 		}
										// 		break;
										// 	}
										// }
										
										if (myBomb.rangeCheck(gCanvas.getUser())) {
											gameOver = true; 
											gCanvas.setScreen(3);
											gCanvas.repaint();
										} else if (myBomb.rangeCheck(gCanvas.getUser2())) {
											gameOver = true; 
											gCanvas.setScreen(2);
											gCanvas.repaint();
										}
										
									} else if (check > 50 && check <= 80) {
										myBomb.setFrame(4);
										myBomb.addCounter();
										gCanvas.repaint();									
									} else if (check > 80) {
										myBomb.setFrame(5);
										gCanvas.repaint();
										max = true;
									}
		
									if (max){
										timer.stop();
										timer.setRepeats(false);					
										myBomb.resetCounter();
										max = false;
										bombCounter = false;
									}
								}
							});	
							timer.start(); 
							bombSet = false;	
						}
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
					
					// Goes through ArrayList of Collideable Crates
					for ( Thing o : bombable ) {
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
					// Goes through ArrayList of Collideable Walls
					for ( Thing o : unMovable ) {
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
					if(restart){
						bombable.clear();
						gCanvas.restart();
						myBomb.resetCounter();
						start = false;
						bombSet = false;
						restart = false;
						myBomb.setFrame(5);
						bombCounter = false;
						timer.stop();
						timer.setRepeats(false);
						bombable = gCanvas.getBombable();
						gCanvas.repaint();
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
				while(!gameOver) {
					int enX = dataIn.readInt();
					int enY = dataIn.readInt();
					int enBX = dataIn.readInt();
					int enBY = dataIn.readInt();
					int enBF = dataIn.readInt();
					boolean collided = dataIn.readBoolean();
					int index = dataIn.readInt();
					
					if (enemy != null) {
						enemy.setX(enX);
						enemy.setY(enY);
						enemyBomb.setX(enBX);
						enemyBomb.setY(enBY);
						enemyBomb.setFrame(enBF);
					}
					
					if (collided) {
						gCanvas.removeCrate(index);
						gCanvas.repaint();
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
				while(!gameOver) {
					if (me != null) {
						dataOut.writeInt(me.getX());
						dataOut.writeInt(me.getY());
						dataOut.writeInt(myBomb.getX());
						dataOut.writeInt(myBomb.getY());
						dataOut.writeInt(myBomb.getFrame());
						dataOut.writeBoolean(space);
						dataOut.writeInt(crateX);
						dataOut.writeInt(crateY);
						dataOut.writeInt(crateI);
						dataOut.flush();
					}
					
					try {
						Thread.sleep(20);
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