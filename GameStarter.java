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

/** This program starts the game. It is a bomberman inspired game where two kinds of chickens try to fry each other with bombs.
*This is a two-player game.
*/

//import java.util.Scanner;

public class GameStarter {
	
	public static void main(String[] args) {
		
		GameFrame gFrame = new GameFrame(500, 500);
		gFrame.connectToServer();
		gFrame.setUpGUI();

	}
}