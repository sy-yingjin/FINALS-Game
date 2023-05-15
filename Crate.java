import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Crate implements Thing{
    private int x, y, width, height, type;
    public BufferedImage crate, destroy;
	public BufferedImage speedDrop, powerDrop, rangeDrop;
    public BufferedImage image = null; 
    public boolean bombed; //not yet sure how to code this...

    public Crate(int x, int y){
        this.x = x;
        this.y = y;
        width = 100;
        height = 100;
        crateImage();
    }

    public void crateImage(){
        try{
            crate = ImageIO.read(getClass().getResourceAsStream("/Sprites/crate.png"));
            speedDrop = ImageIO.read(getClass().getResourceAsStream("/Sprites/add speed.png"));
            powerDrop = ImageIO.read(getClass().getResourceAsStream("/Sprites/add power.png"));
            rangeDrop = ImageIO.read(getClass().getResourceAsStream("/Sprites/add range.png"));
            destroy = ImageIO.read(getClass().getResourceAsStream("/Sprites/broke.png"));

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
	
	public void setType(int i) {
		type = i;
	}
	
	public int getType() {
		return type;
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
        switch(type){
            case 1:
                image = destroy;
                break;
            case 2:
                image = null;
                break;
			default:
				image = crate;
				break;
            }
        g.drawImage(image,x,y,width,height,null);

    }
}