import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class BombCanvas extends JComponent {
	private int width, height;
	
	public BombCanvas(int w, int h) {
		width = w;
		height = h;
		
		setPreferredSize(new Dimension(width, height));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
	}
}