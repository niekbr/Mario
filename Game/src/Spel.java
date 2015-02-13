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
public class Spel implements KeyListener {
	/**
	 * De attributen van de klasse
	 */
	
	public ArrayList<Enemy> enemies;
	public Tekenaar t;
	public ArrayList<Rand> randen;
	int punten = 5;
	int ammo = 5;
	JLabel score;
	JLabel ammunitie;
	boolean running;
	Achtergrond bg;
	Enemy vijand;
	Mario mario;
	boolean gebotst;
	int vx; //Alle objecten moeten meebewegen! Mario en achtergrond bewegen niet!
	int tellerUp;
	Kogel deleteKogel;
	Kogel standaardKogel;
	
	
	public Spel(){
		BufferedImage image = laadPlaatje("mario.gif");
		mario = new Mario(image, 500, 100, 40, 40);
		
		image = laadPlaatje("background.jpg");
		bg = new Achtergrond(image, 0, 0, 1750, 750);
		
		createEnemies(2, 0, 1);
		
		randen = new ArrayList<Rand>();
		image = laadPlaatje("mysteryBox.jpg");
		randen.add(new Rand(image, 500, 0, 50, 50));
		randen.add(new Rand(image, 500, 200, 50, 50));
		
		kogels = new ArrayList<Kogel>();
		image = laadPlaatje("kogel.png");
		standaardKogel = new Kogel(image, mario.x + 30, mario.y, 32, 26, 2, 0);
		
		JFrame scherm = new JFrame("Mario - Thomas & Niek");
		scherm.setBounds(0, 0, 1000, 600);
		scherm.setLayout(null);
		
		t = new Tekenaar(bg, enemies, mario, randen);
		t.setBounds(0, 0, 1000, 600);		
		score = new JLabel("Score: " + punten);	//maak een nieuw JLabel object
		t.add(score);				// en voeg deze aan je JPanel(Tekenaar) toe
		
		ammunitie = new JLabel("                                                                                                                        Ammo: " + ammo);
		t.add(ammunitie);
		scherm.add(t);
		
		
		scherm.setVisible(true);
		scherm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		scherm.addKeyListener(this);
		
		running = true;
		gebotst = false;
		
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
			
			for(Rand p : randen){
				p.x += this.vx;
			}
			
			mario.y += mario.vy;
			//System.out.println(enemies.get(0).vx);
			if(mario.y < mario.spring -25) {
				mario.vy=1;
			}
			
			for(Enemy e : enemies) {
				e.x += e.vx + this.vx;
				e.y += e.vy;
			}
			
			controleerRanden(mario, randen, enemies);
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
			this.vx = -2;
		}
		if(e.getKeyCode() == e.VK_LEFT){
			this.vx = 2;
		}
		if(e.getKeyCode() == e.VK_DOWN){
		}
		if(e.getKeyCode() == e.VK_UP){ 
			if(tellerUp == 0) {
				mario.spring();
				tellerUp++;
			}
		}
		
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == e.VK_RIGHT){
			this.vx = 0;
		}
		if(e.getKeyCode() == e.VK_LEFT){
			this.vx = 0;
		}
		if(e.getKeyCode() == e.VK_UP){
			mario.vy = 1;
			tellerUp = 0;
		}
	}

	public void keyTyped(KeyEvent e) {
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
	
	public void controleerRanden(Mario a, ArrayList<Rand> rand, ArrayList<Enemy> b){
		for(Rand p : rand){
			if(a.x + a.breedte >= p.x && a.x <= p.x + p.breedte && a.y + a.breedte >= p.y && a.y <= p.y + p.breedte) {
				a.x = a.xOld;
				a.y = a.yOld;
			}
			
			for(Enemy e : b) {
				if(e.x + e.breedte >= p.x && e.x <= p.x + p.breedte && e.y + e.breedte >= p.y && e.y <= p.y + p.breedte) {
					e.vx = -e.vx;
					if(e instanceof ParaKoopaTroopa) {
						e.vy = -e.vy;
					} else {
						e.vx = -e.vx;
						System.out.println(e.vx);
					}
				}
			}
		}
	}
}