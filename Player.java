/**
This is a template for a Java file.
@author Shaira Sy (226043), Sherrie Del Rosario (222075)
@version May 16, 2023
**/
/*
I have not discussed the Java language code in my program
with anyone other than my instructor or the teaching assistants
assigned to this course.
I have not used Java language code obtained from another student,
or any other unauthorized source, either modified or unmodified.
If any Java language code or documentation used in my program
was obtained from another source, such as a textbook or website,
that has been clearly noted with a proper citation in the comments
of my program.
*/


/**
 * This program creates a class for players. 
*It implements the Thing interface
*It holds information for the walk animation and both character designs of Chick and Spicy
*/ 

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;
import java.awt.*;
    
    
public class Player implements Thing{
    private int x, y, width, height, counter, spriteNum, spriteCount;
    public BufferedImage chick1, chick2, spicy1, spicy2;
    public BufferedImage image = null;
    
    /**
     * This is the constructor that intializes the player object.
     * It takes the x and y coordinates, and the type (which will indicate what character the player will be) as parameters.
     */
    public Player(int x, int y, int i){
        this.x = x;
        this.y = y;
        width = 100;
        height = 100;
        counter = i;
        playerImage();
        spriteNum = 1;
        spriteCount = 0;
        
    }
    /* This method extracts image files from the Sprites folder
    The files are png to allow transparency
    */
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

    /* This method moves the player horizontally by updating the x coordinate
    * It increments the x values with the parameter speed
     */
    public void moveH(int speed){
        this.x += speed;
    }

    /* This method moves the player vertically by updating the y coordinate
    * It increments the y values with the parameter speed
     */
    public void moveV(int speed){
        this.y += speed;
    }

     /* This method moves returns the x coordinate
     */
    public int getX(){
        return x;
    }

    /* This method moves returns the y coordinate
     */
    public int getY(){
        return y;
    }

    /* This method updates the x coordinate
     */
    public void setX(int n){
        x = n;
    }

    /* This method updates the x coordinate
     */
    public void setY(int n){
        y = n;
    }

    /* This method returns the height
     */
    public int getHeight(){
        return height;
    }

    /* This method returns the width
     */
    public int getWidth(){
        return width;
    }

    /** This method allows player walk animation by changing the spriteNum 
     * (which  acts as the frame count) for every time the spriteCount reaches above 10
     * it only works for the player its own game frame
     */
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
    
    /** This returns a boolean that checks for collision
     * this checks that the player is colliding with a Thing object
     */
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

    /** This draws the player. 
    * Depending on the spriteNum it will show a different frame. 
    * Depending on the counter for the player, it will draw a different character
     */
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