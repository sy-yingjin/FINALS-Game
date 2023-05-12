import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Player implements Thing{
    private int x, y, width, height, HP, counter, spriteNum, spriteCount;
    public BufferedImage chick1, chick2, spicy1, spicy2;
    public BufferedImage image = null;
    
    public Player(int x, int y, int i){
        this.x = x;
        this.y = y;
        width = 100;
        height = 100;
        HP = 1;
        counter = i;
        playerImage();
        spriteNum = 1;
        spriteCount = 0;
        
    }

    public void playerImage(){
        try{
            chick1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/Chick 1.png"));
            chick2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/Chick 2.png"));
            spicy1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/Spicy 1.png"));
            spicy2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/Spicy 2.png"));

        }catch(IOException e){
            e.printStackTrace();
        }
    }    

    public void moveH(int speed){
        this.x += speed;
    }

    public void moveV(int speed){
        this.y += speed;
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

    public void getHit(int h){
        HP -= h;

    }

    public void spriteChange(){
        spriteCount ++;
        if(spriteCount > 10){
            if (spriteNum == 1){
                spriteNum = 2;
            } 
            else if (spriteNum == 2){
                spriteNum = 1;
            }
            spriteCount = 0;
        }

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
        switch(counter) {
        case 1:
            if(spriteNum == 1){
                image = chick1;
            }
            if(spriteNum == 2){
                image = chick2;
            }
            break;

        case 2:
            if(spriteNum == 1){
                image = spicy1;
            }
            if(spriteNum == 2){
                image = spicy2;
            }
            break;
        }
        g.drawImage(image,x,y,width,height,null);
    }

}