package gfx;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SpriteSheet {

	public String path;
	public int width;
	public int height;
	public int tileWidth;
	public int tileHeight;
	
	public int pixels[];
	
	public SpriteSheet(String path){
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(image == null){ return;}
		
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.tileWidth = image.getWidth();
		this.tileHeight = image.getHeight();
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
/*		for(int i = 0; i < pixels.length; i ++){
			pixels[i] = (pixels[i] & 0xffffffff);
		}
				
		for (int i = 0; i < 10; i++){
			int Alpha =		(pixels[i] & 0xff000000)/16777216;		if(Alpha <0) Alpha = 256+Alpha;
			int Red = 		(pixels[i] & 0xff0000)/65536;
			int Green = 	(pixels[i] & 0xff00)/256;
			int Blue = 		(pixels[i] & 0xff);
			System.out.println(pixels[i] + " = A " + Alpha + " ; R " + Red + " ; G " + Green + " ; B " + Blue);
		}*/
	}
}
