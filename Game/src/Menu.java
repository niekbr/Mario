import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class Menu implements MouseListener, MouseMotionListener {
	
	TekenaarMenu t;
	private Spel spel;
	ArrayList<Rand> randen;
	Achtergrond bg;
	BufferedImage image;
	JFrame scherm;
	Rand r;
	
	public Menu() {
		image = laadPlaatje("background.jpg");
		bg = new Achtergrond(image, 0, 0, 1750, 750);
		
		randen = new ArrayList<Rand>();
		image = laadPlaatje("peer.jpg");
		randen.add(new Rand(image, 10, 10, 50, 50));
		randen.add(new Rand(image, 60, 10, 50, 50));
		
		scherm = new JFrame("Mario - Thomas & Niek");
		scherm.setBounds(0, 0, 1000, 600);
		scherm.setLayout(null);
		
		t = new TekenaarMenu(bg, randen);
		t.setBounds(0, 0, 1000, 600);		
		scherm.add(t);
		
		
		scherm.setVisible(true);
		scherm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		t.addMouseListener(this);
		t.addMouseMotionListener(this);
		
		
	}

	public void mouseDragged(MouseEvent e) {
		
	}


	public void mouseMoved(MouseEvent e) {
		
	}


	public void mouseClicked(MouseEvent e) {
	}


	public void mouseEntered(MouseEvent e) {
		
	}


	public void mouseExited(MouseEvent e) {
		
	}


	public void mousePressed(MouseEvent e) {
		for(Rand ap : randen) {
			if( (e.getX() > ap.x) 
					&& (e.getX() < ap.x + ap.breedte) 
					&& (e.getY() > ap.y) 
					&& (e.getY() < ap.y + ap.hoogte)
				){
					scherm.dispose();
					if(ap == randen.get(0)) spel = new Spel(0);
					if(ap == randen.get(1)) spel = new Spel(1);
				}
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}
	
	public BufferedImage laadPlaatje(String fileName) {
		 BufferedImage img = null;
		 try{
			 img = ImageIO.read(new File(fileName));
		 } catch(IOException e){
			 System.out.println("Er is iets fout gegaan bij het laden van het plaatje " + fileName + ".");
		 }
		 return img;
	}

}
