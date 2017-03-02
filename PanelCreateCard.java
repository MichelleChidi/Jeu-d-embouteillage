package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PanelCreateCard extends JPanel {

	public PanelCreateCard() {
		final int height = 720;
		final int width  = 1280;
		
		setLayout(null);
		setPreferredSize(new Dimension(width, height-100));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// board
		Image image = null;
		try {
			image = ImageIO.read(new File("images/board.png"));
		} catch (IOException e) { }
		
	    g.drawImage(image, 260, 10, this);
	    
	    // carrés de gauche
	    // camion horizontal
	    g.setColor(Color.WHITE);
	    g.fillRect(10, 10, 225, 85);	
	    g.setColor(Color.BLACK);
	    g.drawRect(10, 10, 225, 85);
	    
	    // camion vertical
	    g.setColor(Color.WHITE);
	    g.fillRect(80, 100, 85, 225);
	    g.setColor(Color.BLACK);
	    g.drawRect(80, 100, 85, 225);
	    
	    // voiture horizontal
	    g.setColor(Color.WHITE);
	    g.fillRect(40, 335, 160, 85);
	    g.setColor(Color.BLACK);
	    g.drawRect(40, 335, 160, 85);
	    
	    // voiture vertical
	    g.setColor(Color.WHITE);
	    g.fillRect(80, 430, 85, 160);
	    g.setColor(Color.BLACK);
	    g.drawRect(80, 430, 85, 160);
	    
	    // ligne séparatrice
	    g.setColor(Color.BLACK);
	    g.drawLine(245, 10, 245, 590);
	   
	    g.setColor(Color.WHITE);
	    g.fillRect(800, 10, 450, 585);
	   
	    image = null;
		try {
			image = ImageIO.read(new File("images/recycle.png"));
		} catch (IOException e) { }
		
		g.drawImage(image, 875, 150, this);
	    

	    g.finalize();
	}
}
