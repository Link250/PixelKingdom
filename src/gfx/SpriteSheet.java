package gfx;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class SpriteSheet {

	private int tileWidth;
	private int tileHeight;
	
	private int textureIDs[];
	
	private int[][] pixels;
	
	public SpriteSheet(String path){
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(image == null){ return;}
		
		int width = image.getWidth();
		int height = image.getHeight();
		this.tileWidth = width;
		this.tileHeight = height;
		
		genTextures(image.getRGB(0, 0, width, height, null, 0, width), width, height);
	}
	
	public SpriteSheet(String path, int tileWidth, int tileHeight){
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(image == null){ return;}
		
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		int width = image.getWidth();
		int height = image.getHeight();
		genTextures(image.getRGB(0, 0, width, height, null, 0, width), width, height);
	}
	
	public SpriteSheet(int[] pixels, int width, int height, int tileWidth, int tileHeight) {
		setPixels(pixels, width, height, tileWidth, tileHeight);
	}

	public void setPixels(int[] pixels, int width, int height, int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		genTextures(pixels, width, height);
	}
	
	private void genTextures(int[] pixels, int width, int height){
		if(this.textureIDs!=null && this.textureIDs.length>0) {
			glDeleteTextures(textureIDs);
		}
		this.textureIDs = new int[(width/tileWidth)*(height/tileHeight)];
		this.pixels = new int[this.textureIDs.length][tileWidth*tileHeight];
		int texIndex = 0;
		for (int tileY = 0; tileY < height/tileHeight; tileY++) {
			for (int tileX = 0; tileX < width/tileWidth; tileX++) {
				ByteBuffer pixelBuffer = BufferUtils.createByteBuffer(tileWidth * tileHeight * 4);
				for (int y = 0; y < tileHeight; y++) {
					for (int x = 0; x < tileWidth; x++) {
						int pixel = pixels[(y+tileY*tileHeight)*width + x+tileX*tileWidth];
						this.pixels[tileX+tileY*(width/tileWidth)][x+y*tileWidth] = pixel;
						pixelBuffer.put((byte)((pixel >> 16) & 0xFF)); //RED
						pixelBuffer.put((byte)((pixel >> 8) & 0xFF));  //GREEN
						pixelBuffer.put((byte)(pixel & 0xFF));		  //BLUE
						pixelBuffer.put((byte)((pixel >> 24) & 0xFF)); //ALPHA
					}
				}
				pixelBuffer.flip();
				this.textureIDs[texIndex] = glGenTextures();
				
				glBindTexture(GL_TEXTURE_2D, this.textureIDs[texIndex]);
				
				glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tileWidth, tileHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);
				texIndex++;
			}
		}
	}
	
	public int[] getPixels(int tile) {
		return this.pixels[tile];
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
	
	protected void finalize() throws Throwable {
		if(this.textureIDs!=null && this.textureIDs.length>0) {
			glDeleteTextures(textureIDs);
		}
		super.finalize();
	}
}
