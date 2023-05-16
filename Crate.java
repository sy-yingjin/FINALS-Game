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

/** This program creates a class for crates. 
*It implements the Thing interface
*It holds methods for collisions and images for the crates
*/

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

     /**
     * This is the constructor that intializes the crate object.
     * It takes the x and y coordinates as parameters.
     */
    public Crate(int x, int y){
        this.x = x;
        this.y = y;
        width = 100;
        height = 100;
        crateImage();
    }

    /* This method extracts image files from the Sprites folder
    * This is the animation squence for the crate
     */
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

     /* This method updates the y coordinate
     */
    public void setY(int n){
        y = n;
    }
	
     /* This method sets the type or condition of the crate
     * If it's destroyed or not
     */
	public void setType(int i) {
		type = i;
	}
	
    /* This method returns the type or condition of the crate
     * If it's destroyed or not
     */
	public int getType() {
		return type;
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
	
    /** This returns a boolean that checks for collision
     * this checks that the crate is colliding with a Thing object
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

     /** This draws the crate. 
        * Depending on the type it will show a different frame. 
     */
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