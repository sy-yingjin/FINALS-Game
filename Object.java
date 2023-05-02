import java.awt.*;

public interface Object {

	void draw(Graphics2D g2d);
	double getX();
	double getY();
	double getWidth();
	double getHeight();
	
	boolean isColliding(Object a);
}