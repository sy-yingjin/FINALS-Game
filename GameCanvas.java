import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.ArrayList; 

public class GameCanvas extends JComponent {
	private int width;
	private int height;
	private Player chick, spicy;
	private ArrayList<Player> players;
	private Crate c1, c2, c3, c4, c5, c6, c7, c8, c9, b1, b2, b3, b4;
	
	private ArrayList<Object> unMoving;
	
	public GameCanvas(int w, int h) {
		width = w;
		height = h;
		//making players 
		chick = new Player(0,0,1);
		spicy = new Player(400,400,2);
		//adding players to an arraylist so that they are accessible in the GameFrame class
		players = new ArrayList<>();
		players.add(chick);
		players.add(spicy);
		//making crates (breakable) and blocks (not breakable)
		c1 = new Crate(100,0,100,1);
		c2 = new Crate(200,0,100,1);
		c3 = new Crate(300,0,100,1);
		c4 = new Crate(0,200,100,1);
		c5 = new Crate(200,200,100,1);
		c6 = new Crate(400,200,100,1);
		c7 = new Crate(100,400,100,1);
		c8 = new Crate(200,400,100,1);
		c9 = new Crate(300,400,100,1);
		b1 = new Crate(100,100,100,2);
		b2 = new Crate(300,100,100,2);
		b3 = new Crate(100,300,100,2);
		b4 = new Crate(300,300,100,2);
		// adding all crate objects into an arraylist
		unMoving = new ArrayList<>();
		unMoving.add(c1);
		unMoving.add(c2);
		unMoving.add(c3);
		unMoving.add(c4);
		unMoving.add(c5);
		unMoving.add(c6);
		unMoving.add(c7);
		unMoving.add(c8);
		unMoving.add(c9);
		unMoving.add(b1);
		unMoving.add(b2);
		unMoving.add(b3);
		unMoving.add(b4);

		setPreferredSize(new Dimension(w, h));
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		Rectangle2D.Double background = new Rectangle2D.Double(0,0,width,height);
		g2d.setPaint(Color.BLACK);
		g2d.fill(background);

		chick.draw(g2d);
		spicy.draw(g2d); 

		for (Object o : unMoving){
			o.draw(g2d);
		}
		
	}
	
	//returning arraylist for it to be accessible in GameFrame
	public ArrayList getPlayers(){
		return players;
	}

	public ArrayList getUnmoving(){
		return unMoving;
	}
	
}