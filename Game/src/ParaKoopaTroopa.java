import java.awt.image.BufferedImage;

public class ParaKoopaTroopa extends Enemy {
	
	public ParaKoopaTroopa(BufferedImage image, int xBegin, int yBegin, int b, int h) {
		super(image, xBegin, yBegin, b, h, 0, 1);
		this.vy = 0;
	}
}
