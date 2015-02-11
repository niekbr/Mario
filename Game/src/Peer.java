import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//klasse: "Peer", 
public class Peer {
	/*
	 * Hier zet je de eigenschappen van de appel neer
	 */
	public BufferedImage img;		// Een plaatje van de Appel
	public int x,y, breedte, hoogte;	// De plaats en afmeting van de Appel in px
	public boolean rijp;
	public double prijs;
	public String soort;
	/*
	 * Hier zet je de Constructor neer
	 */
	public Peer(BufferedImage image, int xBegin, int yBegin,int b, int h, String soort, boolean rijp, double prijs){
		this.img = image;
		this.x = xBegin;
		this.y = yBegin;
		this.breedte = b;
		this.hoogte = h;
		this.rijp = rijp;
		this.prijs = prijs;
		this.soort = soort;
	}
	
	/*
	 * Maak een methode die het plaatje van de Appel op de juiste plaats tekent
	 */
	public void teken(Graphics2D tekenObject){
		tekenObject.drawImage(img, x, y, breedte, hoogte, null);
	}
	
}
