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
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.swing.JFrame;


public class Menu implements MouseListener, MouseMotionListener, Runnable {
	
	private TekenaarMenu t;
	private ArrayList<Rand> menuKnoppen;
	private ArrayList<Rand> randen;
	private Achtergrond bg;
	private BufferedImage image;
	private JFrame scherm;
	private Thread tr;
	private int vx = -1;
	private Rand mouse;
	private Boolean gifSwitch;
	private int teller;
	private Clip clip;
	private boolean musicOn = true;
	private boolean soundOn = true;
	
	public Menu(boolean firstTime) {
		if(firstTime) {
			playMainMusic();
		}
		
		image = laadPlaatje("background.jpg");
		bg = new Achtergrond(image, 0, 0, 1750, 750);
		
		menuKnoppen = new ArrayList<Rand>();
		randen = new ArrayList<Rand>();
		
		image = laadPlaatje("world1.png");
		menuKnoppen.add(new Rand(image, 430, 190, 140, 50));
		
		image = laadPlaatje("world2.png");
		menuKnoppen.add(new Rand(image, 430, 250, 140, 50));
		
		image = laadPlaatje("world3.png");
		menuKnoppen.add(new Rand(image, 430, 310, 140, 50));
		
		image = laadPlaatje("quit.png");
		menuKnoppen.add(new Rand(image, 430, 370, 140, 50));
		
		image = laadPlaatje("musicOn.png");
		menuKnoppen.add(new Rand(image, 950, 10, 25, 25));
		
		image = laadPlaatje("soundOn.png");
		menuKnoppen.add(new Rand(image, 920, 10, 25, 25));
		
		image = laadPlaatje("logo.gif");
		randen.add(new Rand(image, 325, 25, 340, 138));
		
		image = laadPlaatje("copyright.png");
		randen.add(new Rand(image, 335, 425, 330, 50));
		
		//nieuwe cursor
		image = laadPlaatje("goomba1.gif");
		mouse = new Rand(image, 0, 0, 0, 0);
		randen.add(mouse);
		
		scherm = new JFrame("Mario (Menu) - Thomas & Niek");
		scherm.setBounds(0, 0, 1000, 600);
		scherm.setLayout(null);
		
		t = new TekenaarMenu(bg, menuKnoppen, randen);
		t.setBounds(0, 0, 1000, 600);		
		scherm.add(t);
		
		image = laadPlaatje("groot.png");
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
					image = laadPlaatje("goomba1.gif");	//Goomba 1						
				} else {
					image = laadPlaatje("goomba2.gif"); //Goomba 2
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
		for(Rand ap : menuKnoppen) {
			if( (e.getX() > ap.x) && (e.getX() < ap.x + ap.breedte) && (e.getY() > ap.y) && (e.getY() < ap.y + ap.hoogte) ){
					if(ap != menuKnoppen.get(4) && ap != menuKnoppen.get(5)) scherm.dispose();
					
					if(ap == menuKnoppen.get(0)) new Spel(1, clip, musicOn, soundOn); //Level 1
					if(ap == menuKnoppen.get(1)) new Spel(2, clip, musicOn, soundOn); //Level 2
					if(ap == menuKnoppen.get(2)) new Spel(3, clip, musicOn, soundOn); //Level 3
					if(ap == menuKnoppen.get(3)) System.exit(0); //Quit
					if(ap == menuKnoppen.get(4)) music();
					if(ap == menuKnoppen.get(5)) sound();
					
					
				}
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}
	
	private BufferedImage laadPlaatje(String fileName) {
		 BufferedImage img = null;
		 try{
			 img = ImageIO.read(new File("textures/" + fileName));
		 } catch(IOException e){
			 System.out.println("Er is iets fout gegaan bij het laden van het plaatje " + fileName + ".");
		 }
		 return img;
	}
	
	public void playMainMusic() {
		try {
			clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));
	        clip.open(AudioSystem.getAudioInputStream(new File("sounds/main.wav")));
	        
	        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	        gainControl.setValue(-20.0f); // Muziek moet niet boven soundeffects uitkomen, dus 20dB zachter
	        
	        clip.start();
	        clip.loop(Clip.LOOP_CONTINUOUSLY);
	    }
	    catch (Exception exc) {
	        exc.printStackTrace(System.out);
	    }
      }
	
	private void music() {
		musicOn = !musicOn;
		if(musicOn) {
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			image = laadPlaatje("musicOn.png");
			
		} else {
			clip.stop();
			image = laadPlaatje("musicOff.png");
		}
		
		menuKnoppen.get(4).img = image;
	}
	
	private void sound() {
		soundOn = !soundOn;
		if(soundOn) {
			image = laadPlaatje("soundOn.png");
		} else {
			image = laadPlaatje("soundOff.png");
		}
		menuKnoppen.get(5).img = image;
	}

}
