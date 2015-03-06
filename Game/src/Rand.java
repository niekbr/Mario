import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Rand {
	public BufferedImage img;		// Een plaatje van de Enemy (in child gedefineerd)
	public int x,y, breedte, hoogte, xOld;	// De plaats en afmeting van de Enemy in px (in child gedefineerd)
	
	public Rand(BufferedImage image, int xBegin, int yBegin,int b, int h){
		this.img = image;
		this.x = xBegin;
		this.y = yBegin;
		this.breedte = b;
		this.hoogte = h;
		
	}
	
	public void lopen() {
	}
	
	public void teken(Graphics2D tekenObject){
		tekenObject.drawImage(img, x, y, breedte, hoogte, null);
	}

}
