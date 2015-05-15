import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class TekenaarMenu extends JPanel{
	private ArrayList<Rand> randen;
	private ArrayList<Rand> menuknoppen;
	private Achtergrond bg;
	
	//Constructor voor Tekenaar: array van 'Peer' en 'Goomba' klasses
	public TekenaarMenu(Achtergrond bg, ArrayList<Rand> menuknoppen, ArrayList<Rand> randen){
		this.randen = randen;
		this.menuknoppen = menuknoppen;
		this.bg = bg;
	}
	
	//this.s.teken
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		bg.teken(g2d);
		
		for(Rand i:menuknoppen){
			i.teken(g2d);
		}
		
		for(Rand i:randen){
			i.teken(g2d);
		}
	}
}