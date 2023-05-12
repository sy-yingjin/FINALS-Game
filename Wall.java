import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Wall implements Thing{
	private int x, y, width, height;
    public BufferedImage block;

    public Wall(int x, int y){
        this.x = x;
        this.y = y;
        width = 100;
        height = 100;
        wallImage();
    }

    public void wallImage(){
        try{
            block = ImageIO.read(getClass().getResourceAsStream("/Sprites/block.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
	
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int n){
        x = n;
    }

    public void setY(int n){
        y = n;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }
	
	public boolean isColliding(Thing r) {
		
        if ( this.x + this.width <= r.getX() || // from left side
			this.x >= r.getX() + r.getWidth() || // from right side
			this.y + this.height <= r.getY() || // from top side
			this.y >= r.getY() + r.getHeight() ) // from bottom side
        {
            return false;
        } else {
            return true;
        }
    }

    public void draw(Graphics2D g){
        g.drawImage(block,x,y,width,height,null);

    }
}