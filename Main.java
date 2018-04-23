package snakeGame;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame h = new JFrame("Snake");
		h.setContentPane(new GamePlay());
		h.setDefaultCloseOperation(h.EXIT_ON_CLOSE);
		h.setResizable(false);
		h.pack();
		
		h.setPreferredSize(new Dimension(GamePlay.wight, GamePlay.height));
		h.setLocationRelativeTo(null);
		h.setVisible(true);
	}
}
