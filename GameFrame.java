import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame {
	private int width;
	private int height;
	private JFrame frame;
	private GameCanvas gCanvas;
	private JButton Start;
	private JButton Controls;
	
	public GameFrame(int w, int h) {
		width = w;
		height = h;
		frame = new JFrame();
		sCanvas = new SceneCanvas(width, height);
		Start = new JButton("START");
		Controls = new JButton("CONTROLS");
	}
	
	public void setUpGUI() {
		Container contentPane = frame.getContentPane();
		JPanel buttonsPanel = new JPanel();
		
		/*Setting up the actual GUI was pretty fun when you know what you can mess with without messing up the program*/
		buttonsPanel.setLayout(new GridLayout(1,2));
		buttonsPanel.add(Start);
		buttonsPanel.add(Controls);
	
		contentPane.add(buttonsPanel, BorderLayout.CENTER);
		contentPane.add(gCanvas, BorderLayout.CENTER);
		
		frame.setTitle("Bomb-A Man");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);		
	}
}