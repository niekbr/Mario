import java.awt.image.BufferedImage;

public class MysteryBox extends Rand {
	
	public String powerUp;

	public MysteryBox(BufferedImage image, int xBegin, int yBegin, int b, int h, String powerUp) {
		super(image, xBegin, yBegin, b, h);
		this.powerUp = powerUp;
	}

}