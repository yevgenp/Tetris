package tetris;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class App {
	
	public static void main( String[] args )
	{
		new App();
	}

	public App() {
		EventQueue.invokeLater(()->{
			JFrame frame = new JFrame("Tetris Game");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new BorderLayout());
			frame.add(new TetrisPanel());
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.setVisible(true);
		});
	}
}