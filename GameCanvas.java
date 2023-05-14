import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.ArrayList; 
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;


public class GameCanvas extends JComponent {
	private int width;
	private int height;
	private Player chick, spicy;
	//game obstacles
	private Crate c1, c2, c3, c4, c5, c6, c7, c8, c9;
	private Wall b1, b2, b3, b4;
	private Bomb bomb1, bomb2;
	//title screen
	private BufferedImage screen, title;
    public BufferedImage explode2, boom;
	private boolean titleScreen;
	private ArrayList<Crate> bombable;
	private ArrayList<Thing> unMovable;
	
	public GameCanvas(int w, int h) {
		width = w;
		height = h;
		//making players 
		chick = new Player(0,0,1);
		spicy = new Player(400,400,2);
		
		//making crates (breakable) and blocks (not breakable)
		c1 = new Crate(200,0);
		c2 = new Crate(300,0);
		c3 = new Crate(400,0);
		c4 = new Crate(0,200);
		c5 = new Crate(200,200);
		c6 = new Crate(400,200);
		c7 = new Crate(0,400);
		c8 = new Crate(100,400);
		c9 = new Crate(200,400);
		b1 = new Wall(100,100);
		b2 = new Wall(300,100);
		b3 = new Wall(100,300);
		b4 = new Wall(300,300);
		
		//making bombs for every player, add to bombs list
		bomb1 = new Bomb(0,0);
		bomb2 = new Bomb(400,400);
		
		// adding all crate objects into an arraylist
		bombable = new ArrayList<>();
		bombable.add(c1);
		bombable.add(c2);
		bombable.add(c3);
		bombable.add(c4);
		bombable.add(c5);
		bombable.add(c6);
		bombable.add(c7);
		bombable.add(c8);
		bombable.add(c9);
		
		//adding all wall objects into an arraylist
		unMovable = new ArrayList<>();
		unMovable.add(b1);
		unMovable.add(b2);
		unMovable.add(b3);
		unMovable.add(b4);

		getScreen();
		//getRange();
		screen = title;
		boom = null;
		setPreferredSize(new Dimension(w, h));
		
	}
	
	public void getScreen(){
		try{
            title = ImageIO.read(getClass().getResourceAsStream("/Sprites/Title Screen.jpg"));
        }catch(IOException e){
            e.printStackTrace();
        }
	}
	
	// public void getRange(){
	// 	try{
    //         explode2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/explode 2.jpg"));
    //     }catch(IOException e){
    //         e.printStackTrace();
    //     }
	// }

	public void setScreen(boolean s){
		titleScreen = s;
	}

	public void drawScreen(Graphics2D g){
        if (titleScreen){
			screen = title;
		} else {
			screen = null;
		}
        g.drawImage(screen,0,0,width,height,null);
    }
	
	// public void drawRange(Graphics2D g) {
	// 	int x1 = bomb1.getX();
	// 	int y1 = bomb1.getY();
	// 	int x2 = bomb2.getX();
	// 	int y2 = bomb2.getY();
		
	// 	if (bomb1.getFrame() == 4) {
	// 		boom = explode2;
	// 		g.drawImage(boom,x1+100,y1,100,100,null);
	// 		g.drawImage(boom,x1-100,y1,100,100,null);
	// 		g.drawImage(boom,x1,y1+100,100,100,null);
	// 		g.drawImage(boom,x1,y1-100,100,100,null);
	// 	}
			
	// 	if (bomb2.getFrame() == 4) {
	// 		boom = explode2;
	// 		g.drawImage(boom,x2+100,y2,100,100,null);
	// 		g.drawImage(boom,x2-100,y2,100,100,null);
	// 		g.drawImage(boom,x2,y2+100,100,100,null);
	// 		g.drawImage(boom,x2,y2-100,100,100,null);
	// 	}
		
	// 	boom = null;
	// }

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		
		Rectangle2D.Double background = new Rectangle2D.Double(0,0,width,height);
		g2d.setPaint(Color.BLACK);
		g2d.fill(background);

		chick.draw(g2d);
		spicy.draw(g2d);
		
		bomb1.draw(g2d);
		bomb2.draw(g2d);
		
		//drawRange(g2d);

		for (Thing o : bombable){
			o.draw(g2d);
		}
		
		for (Thing r : unMovable) {
			r.draw(g2d);
		}

		drawScreen(g2d);
	}

	//returning playersprites and arraylist for it to be accessible in GameFrame
	public Player getUser() {
		return chick;
	}
	
	public Player getUser2() {
		return spicy;
	}

	public ArrayList getBombable(){
		return bombable;
	}
	
	public ArrayList getUnmovable() {
		return unMovable;
	}

	public Bomb getBomb1(){
		return bomb1;
	}

	public Bomb getBomb2(){
		return bomb2;
	}
	
	public void removeCrate(Thing o) {
		int item = bombable.indexOf(o);
		bombable.remove(item);
	}
	

	public void restart(){
		titleScreen = true;
		
		bombable.add(0, c1);
		bombable.add(1, c2);
		bombable.add(2, c3);
		bombable.add(3, c4);
		bombable.add(4, c5);
		bombable.add(5, c6);
		bombable.add(6, c7);
		bombable.add(7, c8);
		bombable.add(8, c9);
		
		chick.setX(0);
		chick.setY(0);
		spicy.setX(400);
		spicy.setY(400);
		for(Crate o : bombable){
			o.setType(0);
		}
		bomb1.setFrame(5);
		bomb2.setFrame(5);
		
	}
}