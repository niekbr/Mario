import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Stat {
	public BufferedImage img;		// Een plaatje van de Enemy (in child gedefineerd)
	public int x, y, breedte, hoogte;	// De plaats en afmeting van de Enemy in px (in child gedefineerd)
	
	
	public Stat(BufferedImage image, int x, int y, int breedte, int hoogte){
		this.img = image;
		this.x = x;
		this.y = y;
		this.breedte = breedte;
		this.hoogte = hoogte;
	}
	
	public void teken(Graphics2D tekenObject){
		tekenObject.drawImage(img, x, y, breedte, hoogte, null);
	}

}
