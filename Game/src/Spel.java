import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

//klasse: "Spel"
public class Spel<MysterBox> implements KeyListener {
	/**
	 * De attributen van de klasse
	 */
	
	ArrayList<Enemy> enemies;
	Tekenaar t;
	ArrayList<Rand> randen;
	ArrayList<Kogel> kogels;
	ArrayList<Stat> stats;
	ArrayList<PowerUp> powerups;
	int punten = 0;
	int levens = 3;
	int ammo = 4;
	BufferedImage image;
	boolean running;
	Achtergrond bg;
	Enemy vijand;
	Mario mario;
	boolean gebotst;
	int vx; //Alle objecten moeten meebewegen! Mario en achtergrond bewegen niet als enige!
	boolean pressedUp;
	boolean pressedSpace;
	boolean pressedDown;
	boolean pressedLeft;
	boolean pressedRight;
	Kogel deleteKogel;
	Enemy deleteEnemy;
	Rand deleteBox;
	Rand addBox;
	String plaatjes; //Het converteren van een int naar plaatjes bij updateCoins()
	int coin; //Save van het aantal dat coins nu op staat (stats)
	int leven; //Save van het aantal dat levens nu op staat (stats) 
	int amm;  //Save van het aantal dat ammo nu op staat (stats)
	boolean changed;
	boolean bounceLeft;
	boolean bounceRight;
	int teller;
	boolean gifSwitch;
	boolean kogelLeft;

	
	public Spel(){
		image = laadPlaatje("kijktRechts.gif");
		mario = new Mario(image, 500, 400, 30, 60);
		
		image = laadPlaatje("background.jpg");
		bg = new Achtergrond(image, 0, 0, 1750, 750);
		
		createEnemies(3, 0, 0);
		
		randen = new ArrayList<Rand>();
		image = laadPlaatje("grass.jpg");
		randen.add(new Rand(image, 1000, 500, 25, 25));
		
		image = laadPlaatje("mysterybox.jpg");
		randen.add(new MysteryBox(image, 300, 430, 25, 25));
		
		stats = new ArrayList<Stat>();
		powerups = new ArrayList<PowerUp>();
		
		createMap();
		
		kogels = new ArrayList<Kogel>();
		
		JFrame scherm = new JFrame("Mario - Thomas & Niek");
		scherm.setBounds(0, 0, 1000, 600);
		scherm.setLayout(null);
		
		t = new Tekenaar(kogels, bg, enemies, mario, randen, stats, powerups);
		t.setBounds(0, 0, 1000, 600);		
		scherm.add(t);
		
		
		scherm.setVisible(true);
		scherm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		scherm.addKeyListener(this);
		
		running = true;
		gebotst = false;
		coin = 0;
		changed = false;
		
		while (running){
			try{ Thread.sleep(10); } 
			catch(InterruptedException e){ e.printStackTrace();}
			gebotst = controleerContact(mario, enemies);
			if(gebotst){
				enemies.remove(vijand);
			}
			
			mario.yOld = mario.y;
			
			for(Enemy e : enemies) {
				e.yOld = e.y;
				
				//goomba loop animatie (gif effect)
				if(e instanceof Goomba) {
					if(teller == 10) {
						if(gifSwitch) {
							image = laadPlaatje("goomba1.gif");	//Goomba 1						
						} else {
							image = laadPlaatje("goomba2.gif"); //Goomba 2
						}
						e.img=(image);
					}
				}
			}
			
			//mario loop animatie (gif effect)
			if(teller == 10) {
				if(pressedLeft) {
					if(gifSwitch) {
						image = laadPlaatje("looptLinks1.gif"); //Mario links 1							
					} else {
						image = laadPlaatje("looptLinks2.gif"); //Mario links 2
					}
					
				}
				
				if(pressedRight) {
					if(gifSwitch) {
						image = laadPlaatje("looptRechts1.gif"); //Mario rechts 1						
					} else {
						image = laadPlaatje("looptRechts2.gif"); //Mario rechts 2
					}
				}
				
				if(pressedRight || pressedLeft) {
					mario.img= image;
				}
			}
			
			
			for(Rand p : randen) {
				p.xOld = p.x;
				p.x += this.vx;
			}
			
			for(PowerUp p : powerups){
				p.x += this.vx;
			}
			
			mario.y += mario.vy;
			
			if(mario.y < mario.spring -35){
				mario.vy=1;
			}
			
			for(Enemy e : enemies) {
				e.x += e.vx + this.vx;
				e.y += e.vy;
				if(e.y < 0 || e.y > scherm.getHeight()) {
					this.deleteEnemy = e;
				}
			}
			
			enemies.remove(deleteEnemy);
			
			
			for(Kogel k : kogels){
				k.x += k.vx + this.vx;
				if(k.y < 0 || k.y > scherm.getHeight() || k.x < 0 || k.x > scherm.getWidth()){
					deleteKogel = k;
				}
			}
			kogels.remove(deleteKogel);
			
			controleerSchot(kogels, enemies, randen);
			controleerMario(mario, randen);
			controleerEnemies(randen, enemies);
			
			//Checkt of de coins moeten worden geupdate
			if(coin != punten || leven != levens || amm != ammo){
				updateStats(punten,levens,ammo);
			}
			
			t.repaint();
			if(teller == 10) {
				teller = 0;
				gifSwitch = !gifSwitch;
			}
			
			teller++;
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
	
	private void createMap(){
		image = laadPlaatje("grass.jpg");
		for(int i=0; i<30; i++) {
			this.randen.add(new Rand(image, 50*i, 525, 50, 50));
		}
	}

	//Hier alle enemies aanmaken --> Constructor Enemy: Enemy(image, x, y, breedte, hoogte);
	//Arguments: aantal van iedere enemy!
	private void createEnemies(int goombas, int koopatroopas, int parakoopatroopas) {
		//Goomba's
		BufferedImage image = laadPlaatje("goomba1.gif");
		enemies = new ArrayList<Enemy>();
		for(int i=0; i<goombas; i++) {
			this.enemies.add(new Goomba(image, 50*i, 0));
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
			if(!bounceLeft) {
				this.vx = -2;
			}
			pressedRight = true;
			this.kogelLeft = false;
		}
		if(e.getKeyCode() == e.VK_LEFT){
			if(!bounceRight) {
				this.vx = 2;
			}
			pressedLeft = true;
			this.kogelLeft = true;
		}
		if(e.getKeyCode() == e.VK_DOWN){
			pressedDown = true;
		}
		if(e.getKeyCode() == e.VK_UP){ 
			if(!pressedUp) {
				if(mario.platform) {
					mario.spring();
					this.pressedUp = true;
				}
			}
		}
		if(e.getKeyCode() == e.VK_SPACE){
			if(ammo > 0 && !pressedSpace){
				maakKogel();
				ammo--;
				this.pressedSpace = true;
			}
		}
		
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == e.VK_RIGHT){
			this.vx = 0;
			pressedRight = false;
			image = laadPlaatje("kijktRechts.gif");
			mario.img = image;
		}
		if(e.getKeyCode() == e.VK_LEFT){
			this.vx = 0;
			pressedLeft = false;
			image = laadPlaatje("kijktLinks.gif");
			mario.img = image;
		}
		if(e.getKeyCode() == e.VK_UP){
			mario.vy = 1;
			pressedUp = false;
		}
		if(e.getKeyCode() == e.VK_SPACE) {
			pressedSpace = false;
		}
		if(e.getKeyCode() == e.VK_DOWN) {
			pressedDown = false;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void maakKogel(){
		
		if(kogelLeft) {
			image = laadPlaatje("kogelLinks.png");
			kogels.add(new Kogel(image, mario.x - 32, mario.y + 20, 32, 26, -3, 0));
		} else {
			image = laadPlaatje("kogelRechts.png");
			kogels.add(new Kogel(image, mario.x + mario.breedte, mario.y + 20, 32, 26, 3, 0));
		}
	}
	
	public boolean controleerContact(Mario a, ArrayList<Enemy> enemies) {
		for(Enemy p : enemies){
			
			if(a.x + a.breedte >= p.x && a.x <= p.x + p.breedte && a.y + a.breedte + 30>= p.y && a.y <= p.y + p.breedte) {
				this.vijand = p;
				if(p instanceof KoopaTroopa) {
					punten++;
				} else {
					if(punten > 0){
						punten--;
					}
					if(levens > 0){
						levens--;
					}
					
					if(levens == 0){
						gameOver();
					}

				}
				return true;
			}
		}
		return false;
	}
	
	public boolean controleerSchot(ArrayList<Kogel> kogels, ArrayList<Enemy> enemies, ArrayList<Rand> randen) {
		for(Kogel k : kogels){
			for(Enemy p : enemies){
				if(k.x + k.breedte >= p.x && k.x <= p.x + p.breedte && k.y + k.breedte >= p.y && k.y <= p.y + p.breedte) {
					kogels.remove(k);
					enemies.remove(p);
					punten++;
					t.repaint();
					return true;
				}
			}
			for(Rand r : randen){
				if(k.x + k.breedte >= r.x && k.x <= r.x + r.breedte && k.y + k.breedte >= r.y && k.y <= r.y + r.breedte) {
					kogels.remove(k);
					return true;
				}
			}
		}
		return false;
	}
	public void controleerEnemies(ArrayList<Rand> randen, ArrayList<Enemy> enemies){
		for(Enemy e: enemies){
			for(Rand r: randen){
				if(e.x + e.breedte >= r.x && e.x <= r.x + r.breedte && e.y + e.breedte >= r.y && e.y <= r.y + r.breedte) {
					if(e instanceof ParaKoopaTroopa) {
						e.vy = -e.vy;
					} else {
						if(e.y + e.hoogte == r.y){
							e.y = e.yOld;
						} else {
							e.vx = -e.vx;
						}
					}
				}
			}
		}
	}
	
	public void controleerMario(Mario a, ArrayList<Rand> rand){
		a.platform = false;
		this.bounceLeft = false;
		this.bounceRight = false;
		for(Rand p : rand) {
			if(a.x + a.breedte >= p.x && a.x <= p.x + p.breedte && a.y + a.breedte + 30 >= p.y && a.y <= p.y + p.breedte) {
				
				if(a.yOld + a.hoogte <= p.y) { //komt van boven?
					a.platform = true;
					a.y = a.yOld;
				} else if(a.yOld >= p.y + p.hoogte) { //komt van onder?
					a.platform = true;
					a.y = a.yOld;
					
					//als mario een mysterybox van onder raakt komt er een powerup uit
					if(p instanceof MysteryBox) {
						image = laadPlaatje("levelUp.jpg");
						powerups.add(new PowerUp(image, p.xOld, p.y - 20, 20, 20, 2));
						image = laadPlaatje("grass.jpg");
						addBox = new Rand(image, p.x, p.y, p.breedte, p.hoogte);
						
						this.deleteBox = p;
					}
				} else if(a.x + a.breedte <= p.xOld){ //komt van links?
					this.bounceLeft = true;
					vx = 0;
					
				} else if(a.x + 1 >= p.xOld + p.breedte) { //komt van rechts?
					this.bounceRight = true;
					vx = 0;
				}
			}
			
		}
		rand.remove(deleteBox);
		//alleen als addBox niet leeg is!
		if(addBox instanceof Rand) {
			rand.add(addBox);
			addBox = null;
		}
		
	}
	
	public void updateStats(int p, int l, int a){
		//Checkt of de code min. 1x is gerunt en cleart dan pas de coins list (anders komt er een error)
		if(changed = true){
			stats.clear();
		}
		changed = true;
		//Als de punten nog niet hoger dan 10 zijn dan hoeven er geen twee cijfers getekent te worden
		if(p < 10){
			plaatjes = Integer.toString(p) + ".png";
			image = laadPlaatje(plaatjes);
			stats.add(new Stat(image, 50 ,20, 30, 30));
		}
		//Nu moet er een cijfertje extra bij komen
		if(p > 9){
			plaatjes = Integer.toString(p-10) + ".png";
			image = laadPlaatje("1.png");
			stats.add(new Stat(image, 50 ,20, 30, 30));
			image = laadPlaatje(plaatjes);
			stats.add(new Stat(image, 70 ,20, 30, 30));
		}
		if(l <10){
			plaatjes = Integer.toString(l) + ".png";
			image = laadPlaatje(plaatjes);
			stats.add(new Stat(image, 50 ,65, 30, 30));
		}
		if(l > 9){
			plaatjes = Integer.toString(l-10) + ".png";
			image = laadPlaatje("1.png");
			stats.add(new Stat(image, 50, 65, 30, 30));
			image = laadPlaatje(plaatjes);
			stats.add(new Stat(laadPlaatje(plaatjes), 70 ,65, 30, 30));
		}
		if(a <10){
			plaatjes = Integer.toString(a) + ".png";
			image = laadPlaatje(plaatjes);
			stats.add(new Stat(image, 50 ,110, 30, 30));
		}
		if(a > 9){
			plaatjes = Integer.toString(a-10) + ".png";
			image = laadPlaatje("1.png");
			stats.add(new Stat(image, 50, 110, 30, 30));
			image = laadPlaatje(plaatjes);
			stats.add(new Stat(laadPlaatje(plaatjes), 70 ,110, 30, 30));
		}
		//Tekent standaard de coin en stelt de coins gelijk aan punten (kijk while functie)
		coin = punten;
		leven = levens;
		amm = ammo;
		image = laadPlaatje("ammo.png");
		stats.add(new Stat(laadPlaatje("ammo.png"), 20, 110, 30, 30));
		image = laadPlaatje("levens.png");
		stats.add(new Stat(image, 20, 65, 30,30));
		image = laadPlaatje("coin.png");
		stats.add(new Stat(image, 10,10, 50, 50));
	}
	
	public void gameOver() {
		running = false;
	}
	
	public void openBox(MysteryBox b) {
		
	}
}