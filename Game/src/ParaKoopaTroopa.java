import java.awt.image.BufferedImage;

public class ParaKoopaTroopa extends Enemy {
	int laag, hoog;
	
	public ParaKoopaTroopa(BufferedImage image, int xBegin, int yBegin, int b, int h, int laag, int hoog) {
		super(image, xBegin, yBegin, b, h, 0, 1);
		this.laag = laag;
		this.hoog = hoog;
	}
}
