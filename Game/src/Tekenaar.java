import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Tekenaar extends JPanel{
	private ArrayList<Enemy> enemies;
	private ArrayList<Rand> randen;
	private ArrayList<Rand> menuKnoppen;
	private ArrayList<PowerUp> powerups;
	private ArrayList<Kogel> kogels;
	private ArrayList<Stat> stats;
	private Mario mario;
	private Achtergrond bg;
	private Rand wizard;
	
	//Constructor voor Tekenaar: array van 'Peer' en 'Goomba' klasses
	public Tekenaar(ArrayList<Kogel> kogels, Achtergrond bg, ArrayList<Enemy> enemies, Mario mario, ArrayList<Rand> randen, ArrayList<Stat> stats, ArrayList<PowerUp> power, ArrayList<Rand> menuKnoppen, Rand wizard){
		this.enemies = enemies;
		this.mario = mario;
		this.randen = randen;
		this.bg = bg;
		this.kogels = kogels;
		this.stats = stats;
		this.powerups = power;
		this.menuKnoppen = menuKnoppen;
		this.wizard = wizard;
	}
	
	//this.s.teken
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		bg.teken(g2d);
	
		for(Stat i:stats){
			i.teken(g2d);
		}
		
		wizard.teken(g2d);
		
		mario.teken(g2d);
		
		for(Enemy i:enemies){
			i.teken(g2d);
		}
		for(Kogel i:kogels){
			i.teken(g2d);
		}
		
		for(PowerUp i:powerups){
			i.teken(g2d);
		}
		
		for(Rand i:menuKnoppen){
			i.teken(g2d);
		}
		
		for(Rand i:randen){
			i.teken(g2d);
		}
	}
}