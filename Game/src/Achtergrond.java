import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Achtergrond {
	public int x, y, breedte, hoogte;
	public BufferedImage img;
	
	public Achtergrond(BufferedImage image, int x, int y, int b, int h) {
		this.x = x;
		this.y = y;
		this.breedte = b;
		this.hoogte = h;
		this.img = image;
	}
	
	public void teken(Graphics2D tekenObject){
		tekenObject.drawImage(img, x, y, breedte, hoogte, null);
	}
}
