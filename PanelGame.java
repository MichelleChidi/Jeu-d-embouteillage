package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PanelGame extends JPanel {

	public PanelGame() {
		final int height = 720;
		final int width  = 1280;
		
		setLayout( null );
		setPreferredSize(new Dimension(width, height-100));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// board
		Image image = null;
		try {
			image = ImageIO.read(new File("images/board.png"));
		} catch (IOException e) { }
		
	    g.drawImage(image, 230, 10, this);
	}
}
