import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Enemy {
	public BufferedImage img;		// Een plaatje van de Enemy (in child gedefineerd)
	public int x,y, breedte, hoogte, xOld, yOld, vx, vy = 1;	// De plaats en afmeting van de Enemy in px (in child gedefineerd)
	
	public Enemy(BufferedImage image, int xBegin, int yBegin,int b, int h, int vx, int vy){
		this.img = image;
		this.x = xBegin;
		this.y = yBegin;
		this.breedte = b;
		this.hoogte = h;
		this.vx = vx;
		this.vy = vy;
	}
	
	public void lopen() {
	}
	
	public void teken(Graphics2D tekenObject){
		tekenObject.drawImage(img, x, y, breedte, hoogte, null);
	}

}
