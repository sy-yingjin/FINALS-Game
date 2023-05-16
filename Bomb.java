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
 * This program creates a class for bombs. 
*It implements the Thing interface
*It holds methods for collision checks and explosion frames
*/

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

    /* This method extracts image files from the Sprites folder
    * This is the animation squence for the bomb to explode
     */

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
	
    /* This method returns the width
     */
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
	
	public boolean rangeCheck(Thing c) {
		if (
			// checks middle explosion
			x + width > c.getX() && // from left side
			x < c.getX() + c.getWidth() && // from right side
			y + height > c.getY() && // from top side
			y < c.getY() + c.getHeight() ) { // from bottom side
				return true;
		}
		
		if (
			// checks right explosion
			x+100 + width > c.getX() && // from left side
			x+100 < c.getX() + c.getWidth() && // from right side
			y + height > c.getY() && // from top side
			y < c.getY() + c.getHeight() ) { // from bottom side
				return true;
		}
		
        if ( 
			//  checks left explosion
			x-100 + width > c.getX() && // from left side
			x-100 < c.getX() + c.getWidth() && // from right side
			y + height > c.getY() && // from top side
			y < c.getY() + c.getHeight() ) { // from bottom side
				return true;
		}
		
        if ( 
			// checks top explosion
			x + width > c.getX() && // from left side
			x < c.getX() + c.getWidth() && // from right side
			y-100 + height > c.getY() && // from top side
			y-100 < c.getY() + c.getHeight() ) { // from bottom side
				return true;
		} 
		
        if ( 
			// checks bottom explosion
			x + width > c.getX() && // from left side
			x < c.getX() + c.getWidth() && // from right side
			y+100 + height > c.getY() && // from top side
			y+100 < c.getY() + c.getHeight() ) {// from bottom side
				return true;
		} else {
				return false;
		}
	}

	 /** This draws the bomb. 
     * Depending on the frame it will show a different frame. 
     * It will also draw the explosion if the conditions are met
     */
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