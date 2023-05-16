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

/** This program creates a class for the Game Frame. 
*It has timers for the canvas repaints and animation of the game
*It updates based on the player's actions  (me) and networking from the other player's (enemy).
*It also has the GUI and the Key Listeners for the controls.
*/ 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; 
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GameFrame extends JFrame {
	private Container contentPane;
	private GameCanvas gCanvas;
	private Timer animationTimer;
	private int speed, width, height, crateIndex;
	private boolean up, down, left, right, space, start, restart, choice;
	private boolean max, bombCounter, bombSet, option1, option2;
	private Player me, enemy;
	private ArrayList<Crate> bombable;
	private ArrayList<Wall> unMovable;
	private int playerID;
	private Bomb myBomb, enemyBomb;
	private Timer timer;
	private boolean first1, first2;
	private int oldIndex1, newIndex1, oldIndex2, newIndex2;
	
	//for server
	private Socket socket;
	private ReadFromServer rfsRunnable;
	private WriteToServer wtsRunnable;
	private int port;
	private String ipAddress;
 

	public GameFrame(int w, int h) {
		width = w;
		height = h;
		gCanvas = new GameCanvas(w, h);
		speed = 25;
		bombable = gCanvas.getBombable();
		unMovable = gCanvas.getUnmovable();
		first1 = true;
		first2 = true;
	}
	
	public void setUpGUI() {
		contentPane = this.getContentPane();
		contentPane.setPreferredSize(new Dimension(width,height));
		createPlayers();
		gCanvas.setScreen(1);
		contentPane.add(gCanvas, BorderLayout.CENTER);
		
		this.setTitle("Fry-A-Chick! Player#" + playerID);
		System.out.print("IP Address: ");
		ipAddress = console.nextLine();
		System.out.print("Port Number: ");
		port = Integer.parseInt(console.nextLine());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);	
		
		setUpAnimationTimer();	
		setUpKeyListener();
	}

	/**
		creates players by linking the user to a character from GameCanvas
		who gets who will depend on who connects first
		first to connect will be Chick while the second will be spicy
	**/
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
	
	// Controls are WASD, Space, O K
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
					
					//controls movement and player sprite changes
					//additional conditions are for when the players presses multiple keys at the same time
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
						
						/**
							create a bomb
							bombset is to start the animation for the bomb to change frames
							bombCounter is to check how many bombs the player has dropped
							since the player can only drop 1 bomb at a time.
						**/
						myBomb.setX(me.getX());
						myBomb.setY(me.getY());
						bombSet = true;
						bombCounter = true;
						
						while(bombSet){
							timer = new Timer(80, new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent e){
									
									/**
										check is for the Bomb java file to know how far along the animation
										the bomb dropped is currently in to call the right frame
										bomb detection for collision against crates will only start at frame 3, which
										is when the bomb frame is on a mini explosion
									**/
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
										for (Crate c : bombable) {
											
											/**
												Type 1 is just an animation for a crate being destroyed
												if check == 50 (exact amount so the loop doesn't repeatedly send data)
												it will collect the index of the crate that collided with the bomb explosion
												remove that crate on their arraylist and
												send a confirmation (choice) that the crate was hit
											**/
											if(myBomb.rangeCheck(c)){
												c.setType(1);
												gCanvas.repaint();
												if (check == 50){
												crateIndex = bombable.indexOf(c);
												choice = true;
												gCanvas.removeCrate(crateIndex);
												gCanvas.repaint();	
												}
												break;
											}
										}
										
									} else if (check > 50 && check <= 80) {
										myBomb.setFrame(4);
										choice = false;
										myBomb.addCounter();
										gCanvas.repaint();			
									} else if (check > 80) {
										myBomb.setFrame(5);
										gCanvas.repaint();
										max = true;
									}
		
									/**
										Max is to show whether the bomb animation is done or not
										if yes, it will stop the bomb timer and reset the counter to 0
										for when the next bomb is dropped
									**/
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
					
					/**
						if the player wants to reset the game
						they press K which will undo everything in the map and start from the beginning
						everything will be back to how it was before + back to the original titlescreen
					**/
					if(restart){
						bombable.clear();
						myBomb.resetCounter();
						start = false;
						bombSet = false;
						restart = false;
						myBomb.setFrame(5);
						bombCounter = false;
						timer.stop();
						timer.setRepeats(false);
						gCanvas.restart();
						gCanvas.title();
						bombable = gCanvas.getBombable();
						gCanvas.repaint();
						first1 = true;
						first2 = true;
					}
				}
			}
		};
		animationTimer = new Timer(interval, al);
		animationTimer.start();
	}

	//taken from choobtorials
	//connects a player to the gameserver
	public void connectToServer() {
		try {
 			socket = new Socket(ipAddress, port);
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
					
					/**
						receives information from the server about the other player
						so that they can show up on the GUI
					**/
					int enX = dataIn.readInt();
					int enY = dataIn.readInt();
					int enBX = dataIn.readInt();
					int enBY = dataIn.readInt();
					int enBF = dataIn.readInt();
					
					if (enemy != null) {
						enemy.setX(enX);
						enemy.setY(enY);
						enemyBomb.setX(enBX);
						enemyBomb.setY(enBY);
						enemyBomb.setFrame(enBF);
						
						/**
							bomb collision detector with the player
							it checks what frame the bombs are first and checks
							whether the sprites of the players are hit by the explosion
							before showing the players who wins or loses
							
							if spicy is hit by any bomb, it will show an endscreen of chick winning
							and vice versa
						**/
						if (myBomb.rangeCheck(gCanvas.getUser()) && myBomb.getFrame() == 4) {
							gCanvas.setScreen(3);
							gCanvas.repaint();
							gCanvas.restart();
						} else if (myBomb.rangeCheck(gCanvas.getUser2()) && myBomb.getFrame() == 4) {
							gCanvas.setScreen(2);
							gCanvas.repaint();
							gCanvas.restart();
						} else if (enemyBomb.rangeCheck(gCanvas.getUser()) && enBF == 4) {
							gCanvas.setScreen(3);
							gCanvas.repaint();
							gCanvas.restart();
						} else if (enemyBomb.rangeCheck(gCanvas.getUser2()) && enBF == 4) {
							gCanvas.setScreen(2);
							gCanvas.repaint();
							gCanvas.restart();
						}
						
						/**
							checks whether the index is being repeated/looped
							this was the best solution we could come up with to stop 
							the information being received from repeating twice or thrice
						**/
						if (playerID == 1){
							option1 = dataIn.readBoolean();
							int newIndex1 = dataIn.readInt();
							if (((option1 && first1)||(oldIndex1!=newIndex1))){
								gCanvas.removeCrate(newIndex1);
								gCanvas.repaint();
								option1 = false;
								oldIndex1 = newIndex1;
								first1 = false;
							} 
						} else {
							option2 = dataIn.readBoolean();
							int newIndex2 = dataIn.readInt();
							if (((option2 && first2)||(oldIndex2!=newIndex2))){
								gCanvas.removeCrate(newIndex2);
								gCanvas.repaint();
								option2 = false;
								oldIndex2 = newIndex2;
								first2 = false;
							} 
						}
						
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
					if (me != null) {
						
						/**
							sends player coordinates, bomb coordinates, bomb frame
							collision boolean and index bomb collided with
							to the server
							for the server to send to the other player
						**/
						dataOut.writeInt(me.getX());
						dataOut.writeInt(me.getY());
						dataOut.writeInt(myBomb.getX());
						dataOut.writeInt(myBomb.getY());
						dataOut.writeInt(myBomb.getFrame());
						dataOut.writeBoolean(choice);
						dataOut.writeInt(crateIndex);
						dataOut.flush();
					}
					
					try {
						Thread.sleep(40);
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