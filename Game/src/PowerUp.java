import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PowerUp {
	public BufferedImage img;		// Een plaatje van de Enemy (in child gedefineerd)
	public int x,y, breedte, hoogte, vx, vy, xOld, yOld;	// De plaats en afmeting van de Enemy in px (in child gedefineerd)
	public String powerUp;
	
	public PowerUp(BufferedImage image, int xBegin, int yBegin,int b, int h, int vx, int vy, String powerUp){
		this.img = image;
		this.x = xBegin;
		this.y = yBegin;
		this.yOld = yBegin;
		this.breedte = b;
		this.hoogte = h;
		this.vx = vx;
		this.vy = vy;
		this.powerUp = powerUp;
	}
	
	
	public void teken(Graphics2D tekenObject){
		tekenObject.drawImage(img, x, y, breedte, hoogte, null);
	}

}