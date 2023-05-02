import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class Player implements Object {
	private double x;
	private double y;
	private double sizea;
	private double sizeb;
	
	public Chopsticks(double x, double y, double sizea, double sizeb) {
		this.x = x;
		this.y = y;
		this.sizea = sizea;
		this.sizeb = sizeb;
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		AffineTransform restart = g2d.getTransform();
		g2d.setTransform(restart);
	}
	
	@Override
	public double getX() {
		return x;
	}
	
	@Override
	public double getY() {
		return y;
	}
	
	@Override
	public double getWidth() {
		return sizea;
	}
	
	@Override
	public double getHeight() {
		return sizeb;
	}
	
	@Override
	public boolean isColliding(Object a) {
		return!(this.x + this.width <= a.getX() || this.x >= a.getX() + a.getWidth() || this.y + this.height <= a.getY() || this.y >= a.getY() + a.getHeight() );
	}
}