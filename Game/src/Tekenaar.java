import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Tekenaar extends JPanel{
	private ArrayList<Enemy> enemies;
	private ArrayList<Rand> randen;
	private ArrayList<Kogel> kogels;
	private Mario mario;
	private Achtergrond bg;
	
	//Constructor voor Tekenaar: array van 'Peer' en 'Goomba' klasses
	public Tekenaar(ArrayList<Kogel> kogels, Achtergrond bg, ArrayList<Enemy> enemies, Mario mario, ArrayList<Rand> randen){
		this.enemies = enemies;
		this.mario = mario;
		this.randen = randen;
		this.bg = bg;
		this.kogels = kogels;
	}
	
	//this.s.teken
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		bg.teken(g2d);
		mario.teken(g2d);
		for(Enemy i:enemies){
			i.teken(g2d);
		}
		for(Kogel i:kogels){
			i.teken(g2d);
		}
		for(Rand i:randen){
			i.teken(g2d);
		}
	}
}