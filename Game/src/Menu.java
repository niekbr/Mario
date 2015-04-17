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
	
	private TekenaarMenu t;
	private ArrayList<Rand> menuknoppen;
	private ArrayList<Rand> randen;
	private Achtergrond bg;
	private BufferedImage image;
	private JFrame scherm;
	private int vx = -1;
	
	public Menu() {
		image = laadPlaatje("background.jpg");
		bg = new Achtergrond(image, 0, 0, 1750, 750);
		
		menuknoppen = new ArrayList<Rand>();
		randen = new ArrayList<Rand>();
		
		image = laadPlaatje("world1.png");
		menuknoppen.add(new Rand(image, 430, 200, 140, 50));
		
		image = laadPlaatje("world2.png");
		menuknoppen.add(new Rand(image, 430, 275, 140, 50));
		
		image = laadPlaatje("world3.png");
		menuknoppen.add(new Rand(image, 430, 350, 140, 50));
		
		image = laadPlaatje("logo.gif");
		randen.add(new Rand(image, 325, 25, 350, 138));
		
		image = laadPlaatje("copyright.png");
		randen.add(new Rand(image, 335, 425, 330, 50));
		
		scherm = new JFrame("Mario (Menu) - Thomas & Niek");
		scherm.setBounds(0, 0, 1000, 600);
		scherm.setLayout(null);
		
		t = new TekenaarMenu(bg, menuknoppen, randen);
		t.setBounds(0, 0, 1000, 600);		
		scherm.add(t);
		
		
		scherm.setVisible(true);
		scherm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		t.addMouseListener(this);
		t.addMouseMotionListener(this);
		
		
		while (true){
			try{
				Thread.sleep(20);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			bg.x = bg.x + vx;
			if(scherm.getWidth() >= (bg.breedte + bg.x) || bg.x > 0 ) { //Bewegende achtergrond omdraaien
				vx = -vx;
			}
			scherm.repaint();
		}
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
		for(Rand ap : menuknoppen) {
			if( (e.getX() > ap.x) && (e.getX() < ap.x + ap.breedte) && (e.getY() > ap.y) && (e.getY() < ap.y + ap.hoogte) ){
					scherm.dispose();
					
					if(ap == menuknoppen.get(0)) new Spel(1); //Level 1
					if(ap == menuknoppen.get(1)) new Spel(2); //Level 2
					if(ap == menuknoppen.get(1)) new Spel(3); //Level 3
					
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
