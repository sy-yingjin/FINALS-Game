import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class Bomb implements Thing{
    private int x, y, width, height, range, bombFrame, frame;
    public BufferedImage bomb1, bomb2, explode1, explode2;
    public BufferedImage image = null;
    private boolean explosion;
    private Timer timer;

    public Bomb(int x, int y){
        this.x = x;
        this.y = y; 
        width = 100;
        height = 100;
        bombImage();
        range = 1;
        bombFrame = 0;
        frame = 0;

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

    public void bombTime(int i){
        Timer timer = new Timer(10000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                int i = 0;
                while(i <=4){
                    if(i == 1){
                        frame = 1;
                        System.out.println("1");
                        }
                    else if(i == 2){
                        frame = 2;
                        System.out.println("2");
                        }      
                    else if(i == 3){
                        frame = 3;
                        System.out.println("3");
                    }
                    else if(i == 4){
                        frame = 4;
                        System.out.println("4");
                    }
                    i++;
                }
            }
        });	
        // 
        timer.setInitialDelay(300);
        timer.start(); 
        
        // else if(i ==5){
        //     frame = 5;
        //     System.out.println("5");
        // }

    }

    public boolean explodeCheck(){
        if(frame == 5){
            explosion = true;
        }
        return explosion;
    }

    // public void bombTime(){
    //    while(bombFrame <= 35){
            
    //         if(bombFrame <= 5){
    //             frame = 1;
    //             System.out.println("1");
    //             bombFrame ++; 
    //             }
    //         else if(bombFrame > 5){
    //             frame = 2;
    //             System.out.println("2");
    //             bombFrame ++;
    //             }      
    //         else if(bombFrame <= 15){
    //             frame = 3;
    //             System.out.println("3");
    //         }
    //         else if(bombFrame > 15 && bombFrame <= 20){
    //             frame = 4;
    //             System.out.println("4");
    //         }
    //         else if(bombFrame > 20 && bombFrame <= 29){
    //             frame = 5;
    //             System.out.println("5");
    //         }
    //         else if(bombFrame == 30){
    //             frame = 6;
                
    //         }
    //    }
    //     bombFrame = 0;
        // Thread countdownThread = new Thread(new Runnable() {
        //     @Override
        //     public void run(){
        //         for (int i = 8; i >= 0; i --){
        //             bombFrame = i;

        //             if(bombFrame == 8 || bombFrame == 7){
        //                 frame = 1;
        //             }
        //             else if(bombFrame == 6){
        //                 frame = 2;
        //             }
        //             else if(bombFrame == 5){
        //                 frame = 3;
        //             }
        //             else if(bombFrame <= 4 && bombFrame >= 1){
        //                 frame = 5;
        //             }
        //             else if(bombFrame == 0){
        //                 frame = 6;
        //             }
        //             try{
        //                 Thread.sleep(1000);
        //             }catch(Exception e){
        //                 System.out.println("Error " + e);
        //             }
        //         }
        //     }
        // });
        // countdownThread.start();
   // }

    public void draw(Graphics2D g){
        //image = bomb1;
        // while(bombFrame <= 35){
        //     try{
        //         Thread.sleep(200);
        //     } catch(Exception e){
        //         e.printStackTrace();
        //     }
        //     if(bombFrame <= 5){
        //         image = bomb1;
        //         System.out.println("1");
        //         g.drawImage(image,x,y,width,height,null);
        //         bombFrame ++; 
        //         }
        //     else if(bombFrame > 5 && bombFrame <= 15){
        //         image = bomb2;
        //         System.out.println("2");
        //         g.drawImage(image,x,y,width,height,null);
        //         bombFrame ++;
        //         }      
        //     else if(bombFrame > 15 && bombFrame <= 20){
        //         image = explode1;
        //         System.out.println("3");
        //         g.drawImage(image,x,y,width,height,null);
        //         bombFrame ++; 
        //         }
        //     else if(bombFrame > 20 && bombFrame <= 29){
        //         image = explode2;
        //         System.out.println("4");
        //         g.drawImage(image,x,y,width,height,null);
        //         bombFrame ++; 
        //         }
        //     else if(bombFrame == 30){
        //         image = null;
        //         g.drawImage(image,x,y,width,height,null);
        //         bombFrame ++;  
        //         }
        switch(frame){
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


        
    }

}