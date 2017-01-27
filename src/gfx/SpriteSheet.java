package gfx;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class SpriteSheet {

	private int tileWidth;
	private int tileHeight;
	private int width, height;
	
	private int textureIDs[];
	private int[][] pixels;
	
	private String pathBuffer;
	private int[] pixelBuffer;
	
	public SpriteSheet(String path){
		this.tileWidth = -1;
		this.tileHeight = -1;
		this.pathBuffer = path;
	}
	
	public SpriteSheet(String path, int tileWidth, int tileHeight){
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.pathBuffer = path;
	}
	
	public SpriteSheet(int[] pixels, int width, int height, int tileWidth, int tileHeight) {
		this.pixelBuffer = pixels;
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	private void load() {
		if(this.pathBuffer!=null) {loadFromPath();}else {loadFromPixels();}
	}
	
	private void loadFromPixels() {
		setPixels(pixelBuffer, width, height, tileWidth, tileHeight);
	}
	
	private void loadFromPath() {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(pathBuffer));
		} catch (Exception e) {
			System.err.println(pathBuffer);
			e.printStackTrace();
		}
		if(image == null){ return;}
		
		int width = image.getWidth();
		int height = image.getHeight();
		if(this.tileWidth<0)this.tileWidth = width;
		if(this.tileHeight<0)this.tileHeight = height;
		
		genTextures(image.getRGB(0, 0, width, height, null, 0, width), width, height);
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
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP );
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
				
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tileWidth, tileHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);
				texIndex++;
			}
		}
	}
	
	public int[] getPixels(int tile) {
		if(this.pixels==null)load();
		return this.pixels[tile];
	}
	
	public int getID(int tile) {
		if(this.textureIDs==null)load();
		return textureIDs[tile];
	}
	
	public int getWidth() {
		if(this.tileWidth<0)load();
		return this.tileWidth;
	}
	
	public int getHeight() {
		if(this.tileWidth<0)load();
		return this.tileHeight;
	}
	
	protected void finalize() throws Throwable {
		if(this.textureIDs!=null && this.textureIDs.length>0) {
			glDeleteTextures(textureIDs);
		}
		super.finalize();
	}
}
