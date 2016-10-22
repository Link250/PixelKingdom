package gfx;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import map.Map;

public class Screen {

	public static final int SHADOW_SCALE = 2;
	public static final int MAP_SCALE = 2;
	public static int MAP_ZOOM = 2;
	
	public static int RENDER_CHUNK_SIZE = 128;

	public static int xOffset = 0;
	public static int yOffset = 0;
	
	public static int width;
	public static int height;
	/**== width*height*/
	public int length;
	public int lengthMap;
	public int lengthShadow;
	
	public static ColorSheet[] csheets = new ColorSheet[3];
	
	private static Shader shader;
	private static Matrix4f projection;
	private static Model tileModel;
	
	public static void initialize(int width, int height, ColorSheet f, ColorSheet l, ColorSheet b) {
		Screen.width = width;
		Screen.height = height;
		csheets[Map.LAYER_BACK] = b;
		csheets[Map.LAYER_LIQUID] = l;
		csheets[Map.LAYER_FRONT] = f;
	
		projection = new Matrix4f().setOrtho2D(-width, width, -height, height);
		shader = new Shader("shader");
		tileModel = new Model();
	}
	
	/*
	private void drawPixelArea(int xPos, int yPos, int xSize, int ySize, int color, boolean gui){
		int width = this.width;
		int height = this.height;
		if(!gui) { width /= MAP_ZOOM; height /= MAP_ZOOM;}
		double a = (color>>24)&0xff;
		if(a != 0xff){
			int r = (color>>16)&0xff, g = (color>>8)&0xff, b = color&0xff;
			int col,ro,go,bo,ao;
			for(int x = 0; x < xSize; x++){
				for(int y = 0; y < ySize; y++){
					if(xPos+x >= 0 && xPos+x < width && yPos+y >= 0 && yPos+y < height){
						col = gui ? GUI[xPos+x + (yPos+y)*width] : pixels[xPos+x + (yPos+y)*width];
						ao = (col>>24)&0xff;
						if(ao != 0){
							ro = (int)((a/255*r) + ((255-a)/255*((col>>16)&0xff)));
							go = (int)((a/255*g) + ((255-a)/255*((col>> 8)&0xff)));
							bo = (int)((a/255*b) + ((255-a)/255*((col    )&0xff)));
							if(ao != 255){
								ao = (int) (a+(255-a)/255*((col>>24)&0xff));
							}
							if(gui) GUI[xPos+x + (yPos+y)*width] = (ao<<24)|(ro<<16)|(go<<8)|bo;
							else pixels[xPos+x + (yPos+y)*width] = (ao<<24)|(ro<<16)|(go<<8)|bo;
						}else{
							if(gui) GUI[xPos+x + (yPos+y)*width] = color;
							else pixels[xPos+x + (yPos+y)*width] = color;
						}
					}
				}
			}
		}else{
			for(int x = 0; x < xSize; x++){
				for(int y = 0; y < ySize; y++){
					if(xPos+x >= 0 && xPos+x < width && yPos+y >= 0 && yPos+y < height){
						if(gui) GUI[xPos+x + (yPos+y)*width] = color;
						else pixels[xPos+x + (yPos+y)*width] = color;
					}
				}
			}
		}
	}
	
	private void drawPixel(int x, int y, int color, boolean gui){
		int width = this.width;
		int height = this.height;
		if(!gui) { width /= MAP_ZOOM; height /= MAP_ZOOM;}
		double a = (color>>24)&0xff;
		if(a != 0xff){
			int r = (color>>16)&0xff, g = (color>>8)&0xff, b = color&0xff;
			int col,ro,go,bo,ao;
			if(x >= 0 && x < width && y >= 0 && y < height){
				col = gui ? GUI[x + (y)*width] : pixels[x + (y)*width];
				ao = (col>>24)&0xff;
				if(ao != 0){
					ro = (int)((a/255*r) + ((255-a)/255*((col>>16)&0xff)));
					go = (int)((a/255*g) + ((255-a)/255*((col>> 8)&0xff)));
					bo = (int)((a/255*b) + ((255-a)/255*((col    )&0xff)));
					if(ao != 255){
						ao = (int) (a+(255-a)/255*((col>>24)&0xff));
					}
					if(gui) GUI[x + (y)*width] = (ao<<24)|(ro<<16)|(go<<8)|bo;
					else pixels[x + (y)*width] = (ao<<24)|(ro<<16)|(go<<8)|bo;
				}else{
					if(gui) GUI[x + (y)*width] = color;
					else pixels[x + (y)*width] = color;
				}
			}
		}else{
			if(x >= 0 && x < width && y >= 0 && y < height){
				if(gui) GUI[x + (y)*width] = color;
				else pixels[x + (y)*width] = color;
			}
		}
	}
	
	public void drawMaterial(int xPos, int yPos, int tile, int layer){
		int col = csheets[layer].pixels[tile];
		drawMapPixelScaled(xPos, yPos, col);
	}*/
	
	public static int getMaterialPixel(int tile, int layer) {
		return csheets[layer].pixels[tile];
	}

	/**
	 * 
	 * @param xPos on the Screen
	 * @param yPos on the Screen
	 * @param tile
	 * @param mirrorXY values : 0x00 => no mirror ; 0x10 => X-mirror ; 0x01 => Y-mirror ; 0x11 => X- and Y-mirror ; 
	 * @param sheet
	 * @param color
	 */
//	public void drawGUITile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color){
//		drawTile(xPos,yPos,tile,mirrorXY,sheet,color,true);
//	}
	
	/**
	 * 
	 * @param xPos on the Map
	 * @param yPos on the Map
	 * @param tile
	 * @param mirrorXY values : 0x00 => no mirror ; 0x10 => X-mirror ; 0x01 => Y-mirror ; 0x11 => X- and Y-mirror ; 
	 * @param sheet
	 * @param color
	 */
//	public void drawMapTile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color){
//		drawTile(xPos-xOffset,yPos-yOffset,tile,mirrorXY,sheet,color,false);
//	}

/*	private void drawTile(int xPos, int yPos, int tile, int mirrorXY, SpriteSheet sheet, int color, boolean GUI){
		if(!GUI) {xPos*=MAP_SCALE;yPos*=MAP_SCALE;}
		int xTile = tile % (sheet.width/sheet.tileWidth);
		int yTile = tile / (sheet.width/sheet.tileWidth);
		int ysheet,xsheet,sheetc;
		
		for(int y = 0; y < sheet.tileHeight; y++){
			if(y + yPos > 0 & y + yPos < height){
				ysheet = y;
				if((mirrorXY & 0x01) > 0) ysheet = sheet.tileHeight-1-y;
				for(int x = 0; x < sheet.tileWidth; x++){
					if(x + xPos > 0 & x + xPos <= width){
						xsheet = x;
						if((mirrorXY & 0x10) > 0) xsheet = sheet.tileWidth-1-x;
						sheetc = sheet.pixels[(xTile*sheet.tileWidth+xsheet) + (yTile*sheet.tileHeight+ysheet)*sheet.width];
						if(sheetc>>24 != 0){
							drawPixel(xPos+x, yPos+y, ((sheetc & 0xffffff)== 0xff00ff) ? color : sheetc, GUI);
						}
					}
				}	
			}
		}
	}*/
	
	public static void drawTileOGLMap(float xPos, float yPos, int tile, SpriteSheet sheet){
		shader.bind();
		yPos = (yPos-yOffset)*MAP_SCALE*MAP_ZOOM +sheet.getHeight()/2;
		yPos = height - yPos;
		xPos = (xPos-xOffset)*MAP_SCALE*MAP_ZOOM +sheet.getWidth()/2;
		glActiveTexture(GL_TEXTURE0 + 0);
		glBindTexture(GL_TEXTURE_2D, sheet.getID(tile));
		 
		Matrix4f target = projection.mul(new Matrix4f().translate(new Vector3f(xPos*2-width, yPos*2-height, 0)), new Matrix4f());
		if(sheet.getWidth()!=sheet.getHeight())target.mul(new Matrix4f().ortho2D(-(((float)sheet.getHeight())/((float)sheet.getWidth())), (((float)sheet.getHeight())/((float)sheet.getWidth())), -1, 1));
		target.scale(sheet.getHeight()*MAP_ZOOM);
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", target);
		
		tileModel.render();
	}
	
	public static void drawTileOGL(float xPos, float yPos, int tile, SpriteSheet sheet){
		shader.bind();
		yPos += sheet.getHeight()/2;
		yPos = height - yPos;
		xPos += sheet.getWidth()/2;
		glActiveTexture(GL_TEXTURE0 + 0);
		glBindTexture(GL_TEXTURE_2D, sheet.getID(tile));
		 
		Matrix4f target = projection.mul(new Matrix4f().translate(new Vector3f(xPos*2-width, yPos*2-height, 0)), new Matrix4f());
		if(sheet.getWidth()!=sheet.getHeight())target.mul(new Matrix4f().ortho2D(-(((float)sheet.getHeight())/((float)sheet.getWidth())), (((float)sheet.getHeight())/((float)sheet.getWidth())), -1, 1));
		target.scale(sheet.getHeight());
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", target);
		
		tileModel.render();
	}
	
	public static void drawMapOGL(Map map){
		int textures[] = new int[4];
		Matrix4f target;
		int X, Y;
		shader.bind();
		glActiveTexture(GL_TEXTURE0 + 0);

		for (int x = -width/2-RENDER_CHUNK_SIZE; x < width/2+RENDER_CHUNK_SIZE; x+=RENDER_CHUNK_SIZE) {
			for (int y = -height/2-RENDER_CHUNK_SIZE; y < height/2+RENDER_CHUNK_SIZE; y+=RENDER_CHUNK_SIZE) {
				for (int l : Map.LAYER_ALL) {
					textures[l] = map.getRenderChunk(x+xOffset, y+yOffset, l);
				}
				X = x;
				Y = y;
				X -= (X+xOffset)%RENDER_CHUNK_SIZE;
				Y -= (Y+yOffset)%RENDER_CHUNK_SIZE;
				Y+=RENDER_CHUNK_SIZE/2;
				X+=RENDER_CHUNK_SIZE/2;
				X*=MAP_SCALE*MAP_ZOOM;
				Y*=MAP_SCALE*MAP_ZOOM;
				Y = height - Y;
				target = projection.mul(new Matrix4f().translate(new Vector3f(X*2-width, Y*2-height, 0)), new Matrix4f());
				target.scale(RENDER_CHUNK_SIZE*MAP_SCALE*MAP_ZOOM);
				shader.setUniform("sampler", 0);
				shader.setUniform("projection", target);
				
				for (int l : Map.LAYER_ALL) {
					if(textures[l] == 0)continue;
					glBindTexture(GL_TEXTURE_2D, textures[l]);
					tileModel.render();
				}
			}
		}
	}
}
