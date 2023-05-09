public class GameStarter {
	
	public static void main(String[] args) {
		GameFrame gFrame = new GameFrame(500, 500);
		gFrame.connectToServer();
		gFrame.setUpGUI();
	}
}