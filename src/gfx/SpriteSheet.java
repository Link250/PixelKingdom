package gfx;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;


public class SpriteSheet {

	private int tileWidth;
	private int tileHeight;
	
	private int textureIDs[];
	
	public SpriteSheet() {
		this.tileWidth = 0;
		this.tileHeight = 0;
		
	}
	
	public void setPixels(int[] pixels, int width, int height, int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	public SpriteSheet(String path, int tileWidth, int tileHeight){
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(image == null){ return;}
		
		int width = image.getWidth();
		int height = image.getHeight();
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		for (int tileX = 0; tileX < width/tileWidth; tileX++) {
			for (int tileY = 0; tileY < height/tileHeight; tileY++) {
				ByteBuffer pixelBuffer = BufferUtils.createByteBuffer(tileWidth * tileHeight * 4);
				
				for (int x = 0; x < tileWidth; x++) {
					for (int y = 0; y < tileHeight; y++) {
						int pixel = pixels[(y+tileY*tileHeight)*tileWidth + x+tileX];
						pixelBuffer.put((byte)((pixel >> 16) & 0xFF)); //RED
						pixelBuffer.put((byte)((pixel >> 8) & 0xFF));  //GREEN
						pixelBuffer.put((byte)(pixel & 0xFF));		  //BLUE
						pixelBuffer.put((byte)((pixel >> 24) & 0xFF)); //ALPHA
					}
				}
				pixelBuffer.flip();
			}
		}
	}
	
	public int getID(int tile) {
		return textureIDs[tile];
	}
	
	public int getWidth() {
		return this.tileWidth;
	}
	
	public int getHeight() {
		return this.tileHeight;
	}
}
