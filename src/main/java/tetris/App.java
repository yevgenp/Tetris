package tetris;

import javax.swing.*;
import java.awt.*;

public class App {
	
	public static void main( String[] args )
	{
		new App();
	}

	public App() {
		EventQueue.invokeLater(()->{
			JFrame frame = new JFrame("Tetris Game");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new TetrisPanel());
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.setVisible(true);
		});
	}
}