import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

//klasse: "Spel"
public class Spel implements KeyListener, MouseListener, MouseMotionListener {
	/**
	 * De attributen van de klasse
	 */
	
	public ArrayList<Peer> peren;
	public ArrayList<Enemy> enemies;
	public Tekenaar t;
	public ArrayList<Rand> randen;
	int punten = 5;
	JLabel score;
	boolean running;
	Achtergrond bg;
	Enemy vijand;
	Mario mario;
	boolean gebotst;
	
	
	public Spel(){
		BufferedImage image = laadPlaatje("mario.gif");
		mario = new Mario(image, 500, 100, 40, 40);
		
		image = laadPlaatje("background.jpg");
		bg = new Achtergrond(image, 0, 0, 1750, 750);
		
		createEnemies(1, 0, 0);
		
		randen = new ArrayList<Rand>();
		image = laadPlaatje("mysteryBox.jpg");
		randen.add(new Rand(image, 500, 200, 50, 50));
		
		JFrame scherm = new JFrame("Mario - Thomas & Niek");
		scherm.setBounds(0, 0, 1000, 600);
		scherm.setLayout(null);
		
		t = new Tekenaar(bg, enemies, mario, randen);
		t.setBounds(0, 0, 1000, 600);		
		score = new JLabel("Score: " + punten);	//maak een nieuw JLabel object
		t.add(score);				// en voeg deze aan je JPanel(Tekenaar) toe
		
		scherm.add(t);
		
		scherm.setVisible(true);
		scherm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		scherm.addKeyListener(this);
		
		running = true;
		gebotst = false;
		bg.vx = -1;
		
		t.addMouseListener(this);
		t.addMouseMotionListener(this);
		
		while (running){
			try{ Thread.sleep(10); } 
			catch(InterruptedException e){ e.printStackTrace();}
			gebotst = controleerContact(mario, enemies);
			if(gebotst){
				enemies.remove(vijand);
				score.setText("Score: " + punten);
			}
			mario.xOld = mario.x;
			mario.yOld = mario.y;
			
			if(bg.x < -750) {
				bg.x = 0;
			}
			bg.x += bg.vx;
			mario.x += mario.vx;
			mario.y += mario.vy;
			//System.out.println("vx: " + mario.vx + " vy: " + mario.vy);
			System.out.println(bg.x);
			controleerRanden(mario, randen);
			t.repaint();
		}
		scherm.dispose();
		System.out.println("QUIT");
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
	
	//Hier alle enemies aanmaken --> Constructor Enemy: Enemy(image, x, y, breedte, hoogte);
	//Arguments: aantal van iedere enemy!
	private void createEnemies(int goombas, int koopatroopas, int parakoopatroopas) {
		//Goomba's
		BufferedImage image = laadPlaatje("goomba.png");
		enemies = new ArrayList<Enemy>();
		for(int i=0; i<goombas; i++) {
			this.enemies.add(new Goomba(image, 50*i, 0, 25, 25));
		}
		
		//Koopa Troopa's
		image = laadPlaatje("koopatroopa.png");
		for(int i=0; i<koopatroopas; i++) {
			this.enemies.add(new KoopaTroopa(image, 50*i, 50, 25, 25));
		}
		
		//Para Koopa Troopa's
		image = laadPlaatje("parakoopatroopa.jpg");
		for(int i=0; i<parakoopatroopas; i++) {
			this.enemies.add(new ParaKoopaTroopa(image, 50*i, 100, 25, 25));
		}
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_ESCAPE){
			running = false;
		}
		if(e.getKeyCode() == e.VK_RIGHT){
			mario.vx = 1;
		}
		if(e.getKeyCode() == e.VK_LEFT){
			mario.vx = -1;
		}
		if(e.getKeyCode() == e.VK_DOWN){
			mario.vy = 1;
		}
		if(e.getKeyCode() == e.VK_UP){
			mario.vy = -1;
		}
		
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == e.VK_RIGHT){
			mario.vx = 0;
		}
		if(e.getKeyCode() == e.VK_LEFT){
			mario.vx = 0;
		}
		if(e.getKeyCode() == e.VK_DOWN){
			mario.vy = 0;
		}
		if(e.getKeyCode() == e.VK_UP){
			mario.vy = 0;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	
	public void mouseDragged(MouseEvent e) {
		if(vijand != null){
			vijand.x = (int) (e.getX() - 0.5 * vijand.breedte);
			vijand.y = (int) (e.getY() - 0.5 * vijand.hoogte);
		}
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
		for(Enemy p : enemies){
				if(	(e.getX() > p.x) 
					&& (e.getX() < p.x + p.breedte) 
					&& (e.getY() > p.y) 
					&& (e.getY() < p.y + p.hoogte)
				){
					vijand = p;
					System.out.println("Ik heb op een enemy geklikt: " + vijand.getClass());
				}
		}
	}

	
	public void mouseReleased(MouseEvent e) {
		vijand = null;
	}
	
	public boolean controleerContact(Mario a, ArrayList<Enemy> enemies) {
		for(Enemy p : enemies){
			
			if(a.x + a.breedte >= p.x && a.x <= p.x + p.breedte && a.y + a.breedte >= p.y && a.y <= p.y + p.breedte) {
				this.vijand = p;
				if(p instanceof KoopaTroopa) {
					punten++;
				} else {
					punten--;
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean controleerRanden(Mario a, ArrayList<Rand> rand){
		for(Rand p : rand){
			if(a.x + a.breedte >= p.x && a.x <= p.x + p.breedte && a.y + a.breedte >= p.y && a.y <= p.y + p.breedte) {
				a.x = a.xOld;
				a.y = a.yOld;
				return true;
			}
		}
		return false;
	}
}