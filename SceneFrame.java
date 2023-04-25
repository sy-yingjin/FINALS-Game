import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SceneFrame {
	private int width, height;
	private JFrame frame;
	private BombCanvas bCanvas;
	private JButton Start, Controls;
	
	public SceneFrame(int w, int h) {
		/* many of their brethrens have been created and destroyed here*/
		width = w;
		height = h;
		
		frame = new JFrame();
		bCanvas = new BombCanvas(width, height);
		Start = new JButton("Start");
		Controls = new JButton("Controls");
	}
	
	public void setUpGUI() {
		Container contentPane = frame.getContentPane();
		Container contentPane2 = frame.getContentPane();
		JPanel buttonsPanel = new JPanel();
		
		/*Setting up the actual GUI was pretty fun when you know what you can mess with without messing up the program*/
		buttonsPanel.setLayout(new GridLayout(1, 3));
		buttonsPanel.add(Start);
		buttonsPanel.add(new JLabel());
		buttonsPanel.add(Controls);
	
		contentPane.add(buttonsPanel, BorderLayout.SOUTH);
		contentPane.add(bCanvas, BorderLayout.CENTER);
		
		frame.setTitle("Finals Project - Sy, Shaira - 226043 - Del Rosario, Sherrie - 222075");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);		
	}
}