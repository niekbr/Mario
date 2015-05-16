import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class Menu implements MouseListener, MouseMotionListener, Runnable {
	
	private TekenaarMenu t;
	private ArrayList<Rand> menuknoppen;
	private ArrayList<Rand> randen;
	private Achtergrond bg;
	private BufferedImage image;
	private JFrame scherm;
	private Thread tr;
	private int vx = -1;
	private Rand mouse;
	private Boolean gifSwitch;
	private int teller;
	
	public Menu() {
		image = laadPlaatje("textures/background.jpg");
		bg = new Achtergrond(image, 0, 0, 1750, 750);
		
		menuknoppen = new ArrayList<Rand>();
		randen = new ArrayList<Rand>();
		
		image = laadPlaatje("textures/world1.png");
		menuknoppen.add(new Rand(image, 430, 190, 140, 50));
		
		image = laadPlaatje("textures/world2.png");
		menuknoppen.add(new Rand(image, 430, 250, 140, 50));
		
		image = laadPlaatje("textures/world3.png");
		menuknoppen.add(new Rand(image, 430, 310, 140, 50));
		
		image = laadPlaatje("textures/quit.png");
		menuknoppen.add(new Rand(image, 430, 370, 140, 50));
		
		image = laadPlaatje("textures/logo.gif");
		randen.add(new Rand(image, 325, 25, 340, 138));
		
		image = laadPlaatje("textures/copyright.png");
		randen.add(new Rand(image, 335, 425, 330, 50));
		
		//nieuwe cursor
		image = laadPlaatje("textures/goomba1.gif");
		mouse = new Rand(image, 0, 0, 0, 0);
		randen.add(mouse);
		
		scherm = new JFrame("Mario (Menu) - Thomas & Niek");
		scherm.setBounds(0, 0, 1000, 600);
		scherm.setLayout(null);
		
		t = new TekenaarMenu(bg, menuknoppen, randen);
		t.setBounds(0, 0, 1000, 600);		
		scherm.add(t);
		
		image = laadPlaatje("textures/groot.png");
		scherm.setIconImage(image);
		
		scherm.setVisible(true);
		scherm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		t.addMouseListener(this);
		t.addMouseMotionListener(this);
		gifSwitch = false;
		
		//Verwijder de cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		scherm.getContentPane().setCursor(blankCursor);
		
		tr = new Thread(this, "Thomas & Niek - Mario");
		tr.start();
	}
	
	public void run() {
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
			
			if(teller == 10) {
				if(gifSwitch) {
					image = laadPlaatje("textures/goomba1.gif");	//Goomba 1						
				} else {
					image = laadPlaatje("textures/goomba2.gif"); //Goomba 2
				}
				gifSwitch = !gifSwitch;
				teller = 0;
				mouse.img = image;
			}
			
			teller++;
			scherm.repaint();
		}
		
	}

	public void mouseDragged(MouseEvent e) {
		
	}


	public void mouseMoved(MouseEvent e) {
		
		//midden van het plaatje wordt de cursor
		mouse.x = e.getX() - (mouse.breedte / 2);
		mouse.y = e.getY() - (mouse.hoogte / 2 );
	}


	public void mouseClicked(MouseEvent e) {
	}


	public void mouseEntered(MouseEvent e) {
		mouse.breedte = 25;
		mouse.hoogte = 25;
	}


	public void mouseExited(MouseEvent e) {
		mouse.breedte = 0;
		mouse.hoogte = 0;
	}


	public void mousePressed(MouseEvent e) {
		for(Rand ap : menuknoppen) {
			if( (e.getX() > ap.x) && (e.getX() < ap.x + ap.breedte) && (e.getY() > ap.y) && (e.getY() < ap.y + ap.hoogte) ){
					scherm.dispose();
					
					if(ap == menuknoppen.get(0)) new Spel(1); //Level 1
					if(ap == menuknoppen.get(1)) new Spel(2); //Level 2
					if(ap == menuknoppen.get(2)) new Spel(3); //Level 3
					if(ap == menuknoppen.get(3)) {
						scherm.dispose();
						System.exit(0);
					}
					
				}
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}
	
	private BufferedImage laadPlaatje(String fileName) {
		 BufferedImage img = null;
		 try{
			 img = ImageIO.read(new File(fileName));
		 } catch(IOException e){
			 System.out.println("Er is iets fout gegaan bij het laden van het plaatje " + fileName + ".");
		 }
		 return img;
	}

}
