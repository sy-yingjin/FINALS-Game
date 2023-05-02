import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class GameCanvas extends JComponent {
	private int width;
	private int height;
	
	private ArrayList<DrawingObject> unMoving;
	
	public GameCanvas(int w, int h) {
		width = w;
		height = h;
		
		setPreferredSize(new Dimension(width, height));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		Rectangle2D.Double background = new Rectangle2D.Double(0,0,width,height);
		g2d.setPaint(wallpaperColor);
		g2d.fill(background);
		
		for(DrawingObject a : unMoving){
			a.draw(g2d);
		}	
		
	}
	
}