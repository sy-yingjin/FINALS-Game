import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Crate implements Thing{
    private int x, y, width, height, type;
    public BufferedImage crate, block, speedDrop, powerDrop, rangeDrop;
    public BufferedImage image = null; 
    public boolean bombed; //not yet sure how to code this...

    public Crate(int x, int y, int size, int type){
        this.x = x;
        this.y = y;
        width = size;
        height = size;
        this.type = type;
        // 1 = empty, 2 = wall, 3 = speed drop, 4 = power drop, 5 = range drop
        crateImage();
    }

    public void crateImage(){
        try{
            crate = ImageIO.read(getClass().getResourceAsStream("/Sprites/crate.png"));
            block = ImageIO.read(getClass().getResourceAsStream("/Sprites/block.png"));
            speedDrop = ImageIO.read(getClass().getResourceAsStream("/Sprites/add speed.png"));
            powerDrop = ImageIO.read(getClass().getResourceAsStream("/Sprites/add power.png"));
            rangeDrop = ImageIO.read(getClass().getResourceAsStream("/Sprites/add range.png"));

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
        switch(type){
            case 1:
                image = crate;
                break;
            case 2:
                image = block;
                break;
			case 3:
				image = null;
				break;
            }
        g.drawImage(image,x,y,width,height,null);

    }


}