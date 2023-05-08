import java.awt.*;

public interface Object {

	void draw(Graphics2D g);
	int getX();
	int getY();
	int getWidth();
	int getHeight();
	
	boolean isColliding(Object a, int s);
}