import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.sound.sampled.*;

/**
* 
* @author Thomas Wagenaar & Niek Brekelmans
* @param levelnummer, Clip muziek (voor stop/play), instellingen over sound en music
* @since 04-02-2015
* @version 2.0
*/

//klasse: "Spel"
public class Spel implements KeyListener, Runnable, MouseListener, MouseMotionListener  {
	/**
	 * De attributen van de klasse
	 */
	Tekenaar t;
	JFrame scherm;
	Thread tr;
	Clip sound;
	Clip music;
	ArrayList<Enemy> enemies;
	ArrayList<Rand> randen;
	ArrayList<Rand> menuKnoppen;
	ArrayList<Kogel> kogels;
	ArrayList<Stat> stats;
	ArrayList<PowerUp> powerups;
	int punten = 0, levens = 1, ammo;
	BufferedImage image;
	boolean running;
	Achtergrond bg;
	Enemy vijand;
	Mario mario;
	boolean gebotst;
	int vx; //Alle objecten moeten meebewegen! Mario en achtergrond bewegen niet als enige!
	int g = 2; //Zwaartekracht! MOET NOG VOOR MEER OBJECTEN GEBRUIKT WORDEN
	boolean pressedUp;
	boolean pressedSpace;
	boolean pressedDown;
	boolean pressedLeft;
	boolean pressedRight;
	
	PowerUp deletePowerUp;
	Kogel deleteKogel;
	Enemy deleteEnemy;
	MysteryBox deleteBox;
	ParaKoopaTroopa PKT;
	
	Rand addBox;
	Rand mouse;
	Rand pauseKnop;
	String plaatjes; //Het converteren van een int naar plaatjes bij updateCoins()
	String powerUp;
	int coin; //Save van het aantal dat coins nu op staat (stats)
	int leven; //Save van het aantal dat levens nu op staat (stats) 
	int amm;  //Save van het aantal dat ammo nu op staat (stats)
	long timeIdle;
	boolean changed;
	boolean bounceLeft;
	boolean bounceRight;
	int teller;
	boolean gifSwitch;
	boolean kogelLeft;
	boolean bullet;
	boolean wizardMode;
	boolean musicOn;
	boolean soundOn;
	Rand wizard;

	
	public Spel(int level, Clip music, boolean musicOn, boolean soundOn){
		this.musicOn = musicOn;
		this.soundOn = soundOn;
		this.music = music;
		
		image = laadPlaatje("kijktRechts.gif");
		mario = new Mario(image, 500, 400, this.g);
		changeType(mario, true);
		
		randen = new ArrayList<Rand>();
		
		menuKnoppen = new ArrayList<Rand>();
		
		image = laadPlaatje("pause.png");
        menuKnoppen.add(new Rand(image, 288, 150, 0, 0));
        
        image = laadPlaatje("menu.png");
        menuKnoppen.add(new Rand(image, 430, 300, 0, 0));
        
        image = laadPlaatje("quit.png");
        menuKnoppen.add(new Rand(image, 430, 375, 0, 0));
        
        if(musicOn) {
        	image = laadPlaatje("musicOn.png");
        } else {
        	image = laadPlaatje("musicOff.png");
        }
        menuKnoppen.add(new Rand(image, 950, 10, 25, 25));
        
        if(soundOn) {
        	image = laadPlaatje("soundOn.png");
        } else {
        	image = laadPlaatje("soundOff.png");
        }
        menuKnoppen.add(new Rand(image, 920, 10, 25, 25));
		
		enemies = new ArrayList<Enemy>();
		kogels = new ArrayList<Kogel>();
		
		stats = new ArrayList<Stat>();
		powerups = new ArrayList<PowerUp>();
		
		image = laadPlaatje("wizardMode.jpg");
		
		wizard = new Rand(image, 0, 0, 0, 0);
		mouse = new Rand(image, 0, 0, 0, 0);
		
		createMap(level);
		
		scherm = new JFrame("Mario - Thomas & Niek");
		scherm.setBounds(0, 0, 1000, 600);
		scherm.setLayout(null);
		
		t = new Tekenaar(kogels, bg, enemies, mario, randen, stats, powerups, menuKnoppen, wizard, mouse);
		t.setBounds(0, 0, 1000, 600);		
		scherm.add(t);
		
		image = laadPlaatje("groot.png");
		scherm.setIconImage(image);
		scherm.setVisible(true);
		scherm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		scherm.addKeyListener(this);
		t.addMouseListener(this);
		t.addMouseMotionListener(this);
		
		running = true;
		gebotst = false;
		coin = 0;
		changed = false;
		
		//nieuwe cursor
        image = laadPlaatje("goomba1.gif");
        
        //Verwijder de cursor
        image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "blank cursor");
        scherm.getContentPane().setCursor(blankCursor);
		
		tr = new Thread(this, "Thomas & Niek - Mario");
		tr.start();
	}

	public BufferedImage laadPlaatje(String fileName) {
		 BufferedImage img = null;
		 try{
			 img = ImageIO.read(new File("textures/" + fileName));
		 } catch(IOException e){
			 System.out.println("Er is iets fout gegaan bij het laden van het plaatje " + fileName + ".");
		 }
		 return img;
	}
	
	private void createMap(int level){
		if(level == 1) {
			
			image = laadPlaatje("level1/background1.jpg");
			bg = new Achtergrond(image, 0, 0, 1500, 750);

			image = laadPlaatje("level1/ground.png");
			for(int i=0; i<17; i++) {
				randen.add(new Rand(image, 30*i, 540, 32, 32));	
			}
			
			// Eerste buizen
			image = laadPlaatje("buis.jpg");
			randen.add(new Rand(image, 512, 490, 40, 90));
			randen.add(new Rand(image, 612, 455, 40, 110));
			randen.add(new Rand(image, 712, 425, 40, 145));
			randen.add(new Rand(image, 812, 395, 40, 180));
			
			//Het muntje na de laatste buiz
			image = laadPlaatje("coin.png");
			enemies.add(new coinPickup(image, 892, 295));

			// De vijf blokjes na de eerste vier buizen
			image = laadPlaatje("level1/ground.png");
			for(int i=0; i<5; i++) {
				randen.add(new Rand(image, 912 + 30*i, 445, 32, 32));	
			}
				
			//Blokje tussen middelste en rechtse buis
			image = laadPlaatje("mysterybox.jpg");
			randen.add(new MysteryBox(image, 975, 350, 25, 25, "groot"));
			
			//Sprink blokje om over obstakel te komen
			image = laadPlaatje("level1/ground.png");
			randen.add(new Rand(image, 1122, 425, 32, 32));
			
			//Obstakel van buizen
			image = laadPlaatje("buis.jpg");
			randen.add(new Rand(image, 1232, 385, 40, 180));
			randen.add(new Rand(image, 1272, 415, 40, 180));
			randen.add(new Rand(image, 1212, 445, 40, 180));
			randen.add(new Rand(image, 1252, 480, 40, 90));
			
			//Het muntje op een van de buizen in het obstakel
			image = laadPlaatje("coin.png");
			enemies.add(new coinPickup(image, 1205, 419));
			
			//Blokjes na de buizen
			image = laadPlaatje("level1/ground.png");
			for(int i=0; i<20; i++) {
				randen.add(new Rand(image, 1292 + 30*i, 540, 32, 32));	
			}
			
			//Blokje waar je de bullet powerup van krjigt
			image = laadPlaatje("mysterybox.jpg");
			randen.add(new MysteryBox(image, 1442, 450, 25, 25, "groot"));
			
			// De vijand die op de blijk blokjes spawnt
			image = laadPlaatje("prikkelBloem.png");
			enemies.add(new prikkelBloem(image, 1597, 465, 30, 70));
			
			//Buis waar prikkelbloem uit komt
			image = laadPlaatje("buis.jpg");
			randen.add(new Rand(image, 1592, 515, 40, 55));
			
			//Spawn de goomba's in dat gebiedje
			image = laadPlaatje("goomba1.gif");
			enemies.add(new Goomba(image, 1597, 405));
			enemies.add(new ParaKoopaTroopa(image, 1697, 350, 25, 25, 400, 300));
			
			//Buis aan de rechterkant van het gebiedje waar de Goomba's lopen
			image = laadPlaatje("buis.jpg");
			randen.add(new Rand(image, 1892, 490, 40, 90));
			randen.add(new Rand(image, 1932, 455, 40, 110));
			randen.add(new Rand(image, 1972, 425, 40, 145));
			randen.add(new Rand(image, 2012, 395, 40, 180));
			
		} else if(level == 2 || level == 3) { //NIET GEMAAKT! enkel om te laten zien dat meerdere levels mogelijk zijn.
			image = laadPlaatje("underConstruction.jpg");
			bg = new Achtergrond(image, 0, 0, 1000, 600);
			wizardMode = true;
		}
	}
	
	public void playSound(String file) {
		if(soundOn) {
			try {
				sound = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));
		        sound.open(AudioSystem.getAudioInputStream(new File("sounds/"+file+".wav")));
		        sound.start();
		    } catch (Exception e) {
		        e.printStackTrace(System.out);
		    }
		}
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_ESCAPE){
			pause();
		}
		if(e.getKeyCode() == e.VK_RIGHT){
			if(!bounceLeft) {
				this.vx = -2;
			}
			pressedRight = true;
			kogelLeft = false;
		}
		if(e.getKeyCode() == e.VK_LEFT){
			if(!bounceRight) {
				this.vx = 2;
			}
			pressedLeft = true;
			kogelLeft = true;
		}
		if(e.getKeyCode() == e.VK_DOWN){
			pressedDown = true;
		}
		
		if(e.getKeyCode() == e.VK_UP){ 
			if(!pressedUp) {
				if(mario.platform) {
					playSound("jump");
					mario.spring();
				}
			}
			
			pressedUp = true;
		}
		if(e.getKeyCode() == e.VK_SPACE){
			if(ammo > 0 && !pressedSpace && bullet){
				maakKogel();
				ammo--;
				pressedSpace = true;
				
				if(ammo == 0) {
					changeType(mario, false);
				}
			}
		}
		
		if(e.getKeyCode() == e.VK_F5) {
			enemies.clear();
		}
		
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == e.VK_RIGHT){
			this.vx = 0;
			pressedRight = false;
			if(!bullet) {
				image = laadPlaatje("kijktRechts.gif"); //niet bullet
			} else {
				image = laadPlaatje("bulletKijktRechts.gif"); //wel bullet
			}
			mario.img = image;
		}
		if(e.getKeyCode() == e.VK_LEFT){
			this.vx = 0;
			pressedLeft = false;
			
			if(!bullet) {
				image = laadPlaatje("kijktLinks.gif"); //niet bullet
			} else {
				image = laadPlaatje("bulletKijktLinks.gif"); //wel bullet
			}
			
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
		
		if(e.getKeyCode() == e.VK_G){
			wizardMode();
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void maakKogel(){
		if(kogelLeft) {
			image = laadPlaatje("kogelLinks.png");
			kogels.add(new Kogel(image, mario.x - 32, mario.y + mario.hoogte - 35, 32, 26, -3, 0));
		} else {
			image = laadPlaatje("kogelRechts.png");
			kogels.add(new Kogel(image, mario.x + mario.breedte, mario.y + mario.hoogte - 35, 32, 26, 3, 0));
		}
	}
	
	public boolean controleerContact(Mario a, ArrayList<Enemy> enemies) {
		for(Enemy p : enemies){
			
			if(a.x + a.breedte >= p.x && a.x <= p.x + p.breedte && a.y + 2 * a.breedte >= p.y && a.y <= p.y + p.breedte) {
				this.vijand = p;
				if(p instanceof KoopaTroopa) {
					punten++;
				} else if(p instanceof coinPickup){
					punten++;
					playSound("coin");
				} else if (p instanceof prikkelBloem) {
					if (levens > 0){
						if(a.type == 0) {
							levens--;
						} else {
							changeType(a, false);
							ammo = 0;
						}
					if (levens == 0){
						gameOver(true);
					}
						
					}
				} else {
					if(a.yOld + a.hoogte <= p.y) { //komt van boven
						punten++;
					} else {
						if(punten > 0){
							punten--;
						}
						if(levens > 0){
							if(a.type == 0) {
								levens--;
							} else {
								changeType(a, false);
								ammo = 0;
							}
						}
						
						if(levens == 0){
							gameOver(true);
						}
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
				if(e.x + e.breedte >= r.x && e.x <= r.x + r.breedte && e.y + e.hoogte >= r.y && e.y <= r.y + r.hoogte) {
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
	
	public void controleerPowerUp(ArrayList<Rand> randen, ArrayList<PowerUp> powerups, Mario a){
		for(PowerUp e: powerups){
			for(Rand r: randen){
				if(e.x + e.breedte >= r.x && e.x <= r.x + r.breedte && e.y + e.breedte >= r.y && e.y <= r.y + r.breedte) {
					if(e.yOld + e.hoogte <= r.y){
						e.y = e.yOld;
					} else if((e.x + e.breedte <= r.xOld) || (e.x + 1 >= r.xOld + r.breedte) ) {
						e.vx = -e.vx;
					}
				}
			}
			
			if(a.x + a.breedte >= e.x && a.x <= e.x + e.breedte && a.y + 2 * a.breedte >= e.y && a.y <= e.y + e.breedte) {
				deletePowerUp = e;
				
				if(e.powerUp == "lifeUp") {
					levens++;
					playSound("1-up");
					
				}
				
				if(e.powerUp == "groot") {
					changeType(mario, true);
					playSound("powerUp-pick");
				}
			}
		}
		
		powerups.remove(deletePowerUp);
	}
	
	public void controleerMario(Mario a, ArrayList<Rand> rand){
		a.platform = false;
		
		bounceLeft = false;
		bounceRight = false;
		
		for(Rand p : rand) {
			if(a.x + a.breedte >= p.x && a.x <= p.x + p.breedte && a.y + a.hoogte >= p.y && a.y <= p.y + p.hoogte) {
				if(a.yOld + a.hoogte <= p.y) { //komt van boven?
					a.platform = true;
					a.y = a.yOld;
				} else if(a.yOld >= p.y + p.hoogte) { //komt van onder?
					a.platform = true;
					a.y = a.yOld;
					mario.vy = g;
					
					//als mario een mysterybox van onder raakt komt er een powerup uit
					if(p instanceof MysteryBox) {
						playSound("powerUp-appear");
						this.deleteBox = (MysteryBox) p;
						powerUp = deleteBox.powerUp;
						if(powerUp == "groot") {
							if(mario.type == 0) {
								powerUp = "groot";
							}
							
							if(mario.type >= 1) {
								powerUp = "vuurBloem";
							}
						}
						
						image = laadPlaatje("" + powerUp + ".png");
						powerups.add(new PowerUp(image, p.xOld, p.y - 20, 20, 20, 1, this.g, deleteBox.powerUp));
						
						image = laadPlaatje("emptyBlock.png"); //Box wordt leeg
						addBox = new Rand(image, p.x, p.y, p.breedte, p.hoogte);
					}
					
				} else if(a.x + a.breedte <= p.xOld){ //komt van links?
					bounceLeft = true;
					vx = 0;
					
				} else if(a.x + 1 >= p.xOld + p.breedte) { //komt van rechts?
					bounceRight = true;
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
		stats.clear();
	
		//Als de punten nog niet hoger dan 10 zijn dan hoeven er geen twee cijfers getekent te worden
		if(p < 10){
			plaatjes = Integer.toString(p) + ".png";
			image = laadPlaatje("" + plaatjes);
			stats.add(new Stat(image, 50 ,80, 30, 30));
		}
		//Nu moet er een cijfertje extra bij komen
		if(p > 9){
			plaatjes = Integer.toString(p-10) + ".png";
			image = laadPlaatje("1.png");
			stats.add(new Stat(image, 50 ,80, 30, 30));
			image = laadPlaatje("" + plaatjes);
			stats.add(new Stat(image, 70 ,80, 30, 30));
		}
		if(l < 10){
			plaatjes = Integer.toString(l) + ".png";
			image = laadPlaatje("" + plaatjes);
			stats.add(new Stat(image, 50 ,125, 30, 30));
		}
		if(l > 9){
			plaatjes = Integer.toString(l-10) + ".png";
			image = laadPlaatje("1.png");
			stats.add(new Stat(image, 50, 125, 30, 30));
			image = laadPlaatje("" + plaatjes);
			stats.add(new Stat(laadPlaatje("" + plaatjes), 700 ,125, 30, 30));
		}
		if(bullet) {
			if(a < 10){
				plaatjes = Integer.toString(a) + ".png";
				image = laadPlaatje("" + plaatjes);
				stats.add(new Stat(image, 50 ,170, 30, 30));
			}
			if(a > 9){
				plaatjes = Integer.toString(a-10) + ".png";
				image = laadPlaatje("1.png");
				stats.add(new Stat(image, 50, 170, 30, 30));
				image = laadPlaatje("" + plaatjes);
				stats.add(new Stat(laadPlaatje("" + plaatjes), 700 ,170, 30, 30));
			}
		}
		//Tekent standaard de coin en stelt de coins gelijk aan punten (kijk while functie)
		coin = punten;
		leven = levens;
		amm = ammo;
		if(bullet) {
			image = laadPlaatje("ammo.png");
			stats.add(new Stat(laadPlaatje("ammo.png"), 20, 170, 30, 30));
		}
		image = laadPlaatje("levens.png");
		stats.add(new Stat(image, 20, 125, 30,30));
		image = laadPlaatje("coin.png");
		stats.add(new Stat(image, 10,70, 50, 50));
	}
	
	private void gameOver(boolean a) { //Boolean true: open menu; boolean false: terminate program
		running = false;
		scherm.dispose();
		
		if(a) {
			new Menu(false);
		} else {
			System.exit(0);
		}
	}
	
	public void changeType(Mario m, boolean increase) {
		
		if(increase){
			m.type++;
		} else {
			m.type--;
		}
		
		switch(m.type) {
			case 0:
				m.breedte = 20;
				m.hoogte = 40;
				if(!increase) {
					m.x = m.x + 10;
					m.y = m.y + 20;
				}
				bullet = false;
				break;
			case 1:
				m.breedte = 30;
				m.hoogte = 60;
				if(increase) {
					m.x = m.x - 10;
					m.y = m.y - 20;
				}
				bullet = false;
				break;
			case 2:
				bullet = true;
				ammo = 3;
				break;
		}
	}
	
	private void pause() {
        if(running) {
            
            menuKnoppen.get(0).breedte = 424;
            menuKnoppen.get(0).hoogte = 113;
            
            menuKnoppen.get(1).breedte = 140;
            menuKnoppen.get(1).hoogte = 50;
            
            menuKnoppen.get(2).breedte = 140;
            menuKnoppen.get(2).hoogte = 50;
            
        } else {
        	for(int i = 0; i < 3; i++) {
            	menuKnoppen.get(i).breedte = 0;
            	menuKnoppen.get(i).hoogte = 0;
            }
        }
        
        running = !running;
    }
	
	private void wizardMode() {
		wizardMode = !wizardMode;
		if(wizardMode) {
			wizard.breedte = 1000;
			wizard.hoogte = 600;
		} else {
			wizard.breedte = 0;
			wizard.hoogte = 0;
		}
	}
	
	private void music() {
		musicOn = !musicOn;
		if(musicOn) {
			music.start();
			music.loop(Clip.LOOP_CONTINUOUSLY);
			image = laadPlaatje("musicOn.png");
			
		} else {
			music.stop();
			image = laadPlaatje("musicOff.png");
		}
		
		menuKnoppen.get(3).img = image;
	}
	
	private void sound() {
		soundOn = !soundOn;
		if(soundOn) {
			image = laadPlaatje("soundOn.png");
		} else {
			image = laadPlaatje("soundOff.png");
		}
		menuKnoppen.get(4).img = image;
	}

	public void run() {
		while (true){
			try{
				Thread.sleep(10);
			}
			
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			if(running) {
			
				gebotst = controleerContact(mario, enemies);
				if(gebotst){
					enemies.remove(vijand);
				}
				
				mario.yOld = mario.y;
				
				//muis loop animatie (gif effect)
                if(teller == 10) {
                    if(gifSwitch) {
                        image = laadPlaatje("goomba1.gif");   //Goomba 1                      
                    } else {
                        image = laadPlaatje("goomba2.gif"); //Goomba 2
                    }
                    mouse.img = image;
                }
                
				for(Enemy e : enemies) {
					e.yOld = e.y;
	                
					//goomba loop animatie (gif effect)
					if(e instanceof Goomba) {
						if(teller == 10 || teller == 20) {
							if(gifSwitch) {
								image = laadPlaatje("goomba1.gif");	//Goomba 1						
							} else {
								image = laadPlaatje("goomba2.gif"); //Goomba 2
							}
							e.img = image;
						}
					}
				}
				
				if(!bullet) { //mario loop animatie als hij NIET bullet is (gif effect)
					if(teller == 10 || teller == 20) {
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
							mario.img = image;
						}
					}
				} else { //mario loop animatie als hij WEL bullet is (gif effect)
					if(teller == 10 || teller == 20) {
						if(pressedLeft) {
							if(gifSwitch) {
								image = laadPlaatje("bulletLooptLinks1.gif"); //Mario links 1							
							} else {
								image = laadPlaatje("bulletLooptLinks2.gif"); //Mario links 2
							}
							
						}
						
						if(pressedRight) {
							if(gifSwitch) {
								image = laadPlaatje("bulletLooptRechts1.gif"); //Mario rechts 1						
							} else {
								image = laadPlaatje("bulletLooptRechts2.gif"); //Mario rechts 2
							}
						}
						
						if(pressedRight || pressedLeft) {
							mario.img= image;
						}
					}
				}
				
				
				
				for(Rand p : randen) {
					p.xOld = p.x;
					p.x += this.vx;
				}
				
				for(PowerUp p : powerups){
					p.xOld = p.x;
					p.yOld = p.y;
					
					p.x += this.vx;
					p.x += p.vx;
					
					p.y += p.vy;
				}
				
				if(mario.y < mario.spring - 50){
					mario.vy = g;
					mario.spring = 0;
				}
				
				if(!wizardMode) {
					mario.y += mario.vy;
				} else {
					if(pressedDown) {
						mario.y++;
					}
					
					if(pressedUp) {
						mario.y--;
					}
				}
				
				
				for(Enemy e : enemies) {
					e.x += e.vx + vx;
					e.y += e.vy;
					if(e.y < 0 || e.y > scherm.getHeight()) {
						deleteEnemy = e;
					}
					
					if(e instanceof ParaKoopaTroopa) {
						PKT = (ParaKoopaTroopa) e;
						
						if(PKT.y <= PKT.hoog || PKT.y >= PKT.laag) {
							PKT.vy = -PKT.vy;
						}
					}
				}
				
				enemies.remove(deleteEnemy);
				
				
				for(Kogel k : kogels){
					k.x += k.vx + vx;
					if(k.y < 0 || k.y > scherm.getHeight() || k.x < 0 || k.x > scherm.getWidth()){
						deleteKogel = k;
					}
				}
				
				if(mario.y < 0 || mario.y > scherm.getHeight() || mario.x < 0 || mario.x > scherm.getWidth()){
					gameOver(true);
				}
				kogels.remove(deleteKogel);
				
				controleerSchot(kogels, enemies, randen);
				controleerMario(mario, randen);
				controleerEnemies(randen, enemies);
				controleerPowerUp(randen, powerups, mario);
				
				//Checkt of de coins moeten worden geupdate
				if(coin != punten || leven != levens || amm != ammo){
					updateStats(punten,levens,ammo);
				}
				
			}
			
			if(System.currentTimeMillis() - timeIdle > 1500) {
	            mouse.breedte = 0;
	            mouse.hoogte = 0;
			} else {
	            mouse.breedte = 25;
	            mouse.hoogte = 25;
			}
			
            //muis loop animatie (gif effect)
            if(teller == 20) {
                if(gifSwitch) {
                    image = laadPlaatje("goomba1.gif");   //Goomba 1                      
                } else {
                    image = laadPlaatje("goomba2.gif"); //Goomba 2
                }
                mouse.img = image;
            }
            
			
			if(teller++ == 20) {
				teller = 0;
				gifSwitch = !gifSwitch;
			}
			
			t.repaint();
		}
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		if(!running) {
            mouse.breedte = 25;
            mouse.hoogte = 25;
        }
		
	}

	public void mouseExited(MouseEvent e) {
		mouse.breedte = 0;
        mouse.hoogte = 0;
	}

	public void mousePressed(MouseEvent e) {
		
		for(Rand ap : menuKnoppen) {
			if( (e.getX() > ap.x) && (e.getX() < ap.x + ap.breedte) && (e.getY() > ap.y) && (e.getY() < ap.y + ap.hoogte) ){
				//menuKnoppen[0] is het logo, dus [1] is de eerste knop
				if(ap == menuKnoppen.get(1)) { //Knop 1 (Terug naar menu)
					gameOver(true);
				} else if(ap == menuKnoppen.get(2)) { //Knop 2 (Quit)
					gameOver(false);
				} else if(ap == menuKnoppen.get(3)) { //Knop 3 (Muziek aan/uit)
					music();
				} else if(ap == menuKnoppen.get(4)) { //Knop 4 (Sound aan/uit)
					sound();
				}
			}
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		//midden van het plaatje wordt de cursor
        mouse.x = e.getX() - (mouse.breedte / 2);
        mouse.y = e.getY() - (mouse.hoogte / 2 );
        timeIdle = System.currentTimeMillis();
        
	}
}