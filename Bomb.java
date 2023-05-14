import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Bomb implements Thing{
    private int x, y, width, height, frame;
	private int counter;
    public BufferedImage bomb1, bomb2, explode1, explode2;
    public BufferedImage image = null;
	public BufferedImage boomB;

    public Bomb(int x, int y){
        this.x = x;
        this.y = y; 
        width = 100;
        height = 100;
        bombImage();
        frame = 0;
		counter = 0;
    }

    public void bombImage(){
        try{
            bomb1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/bomb 1.png"));
            bomb2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/bomb 2.png"));
            explode1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/explode 1.png"));
            explode2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/explode 2.png"));

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
	
	public void setFrame(int i) {
		frame = i;
	}
	
	public int getFrame() {
		return frame;
	}
	
	public void addCounter() {
		counter+= 2;
	}
	
	public int checkCounter() {
		return counter;
	}
	
	public void resetCounter() {
		counter = 0;
	}
	
	// public String rangeCheck(int x, int y) {
		// if (
			// // checks right explosion
			// this.x+100 + this.width <= x || // from left side
			// this.x+100 > x+100 || // from right side
			// this.y + this.height <= y || // from top side
			// this.y >= y+100 ) { // from bottom side
				// return "right hit";
		// } else if ( 
			// //  checks left explosion
			// this.x-100 + this.width <= x || // from left side
			// this.x-100 >=  x+100  || // from right side
			// this.y + this.height <= y || // from top side
			// this.y >= y+100 ) { // from bottom side
				// return "left hit";
		// } else if ( 
			// // checks top explosion
			// this.x + this.width <= x || // from left side
			// this.x >= x+100 || // from right side
			// this.y-100 + this.height <= y || // from top side
			// this.y-100 >= y+100 ) { // from bottom side
				// return "top hit";
		// } else if ( 
			// // checks bottom explosion
			// this.x + this.width <= x || // from left side
			// this.x >= x+100 || // from right side
			// this.y+100 + this.height <= y || // from top side
			// this.y+100 >= y+100 ) {// from bottom side
				// return "bottom hit";
		// } else {
				// return "no";
		// }
	// }

    public void draw(Graphics2D g){
        switch(frame){
            //changes the image of the bomb shown
            case 1:
            image = bomb1;
            break;
            case 2:
            image = bomb2;
            break;
            case 3:
            image = explode1;
            break;
            case 4:
            image = explode2;
            break;
            case 5:
            image = null;
            break;
        }
        g.drawImage(image,x,y,width,height,null);
		
		switch(frame) {
			case 4:
			boomB = explode2;
			break;
			default:
			boomB = null;
			break;
		}
		g.drawImage(boomB,x+100,y,width,height,null); //right
		g.drawImage(boomB,x-100,y,width,height,null); //left
		g.drawImage(boomB,x,y+100,width,height,null); //down
		g.drawImage(boomB,x,y-100,width,height,null); //up
		
    }
	

}