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
	
	private static Shader default_shader;
	private static Shader colored_shader;
	private static Shader map_shader;
	
	private static Matrix4f projection;
	private static Model tileModel;
	
	public static void initialize(int width, int height) {
		Screen.width = width;
		Screen.height = height;
		projection = new Matrix4f().setOrtho2D(-width, width, -height, height);
		default_shader = new Shader("default_shader");
		colored_shader = new Shader("colored_shader");
		map_shader = new Shader("map_shader");
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
	
	public static void drawMapSprite(int xPos, int yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY, int color){
		drawSprite(xPos, yPos, sheet, tile, mirrorX, mirrorY, color, true);
	}
	
	public static void drawMapSprite(int xPos, int yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY){
		drawSprite(xPos, yPos, sheet, tile, mirrorX, mirrorY, 0, true);
	}
	
	public static void drawMapSprite(int xPos, int yPos, SpriteSheet sheet, int tile){
		drawSprite(xPos, yPos, sheet, tile, false, false, 0, true);
	}
	
	public static void drawMapSprite(int xPos, int yPos, SpriteSheet sheet){
		drawSprite(xPos, yPos, sheet, 0, false, false, 0, true);
	}
	
	public static void drawGUISprite(int xPos, int yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY, int color){
		drawSprite(xPos, yPos, sheet, tile, mirrorX, mirrorY, color, false);
	}
	
	public static void drawGUISprite(int xPos, int yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY){
		drawSprite(xPos, yPos, sheet, tile, mirrorX, mirrorY, 0, false);
	}
	
	public static void drawGUISprite(int xPos, int yPos, SpriteSheet sheet, int tile){
		drawSprite(xPos, yPos, sheet, tile, false, false, 0, false);
	}
	
	public static void drawGUISprite(int xPos, int yPos, SpriteSheet sheet){
		drawSprite(xPos, yPos, sheet, 0, false, false, 0, false);
	}
	
	public static void drawSprite(float xPos, float yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY, int color, boolean onMap){
		if(onMap) {
			xPos-=xOffset;xPos*=MAP_SCALE*MAP_ZOOM;
			yPos-=yOffset;yPos*=MAP_SCALE*MAP_ZOOM;
			xPos += sheet.getWidth()/2f*MAP_ZOOM;
			yPos += sheet.getHeight()/2f*MAP_ZOOM;
		}else {
			xPos += sheet.getWidth()/2f;
			yPos += sheet.getHeight()/2f;
		}
		yPos = height - yPos;
		
		glActiveTexture(GL_TEXTURE0 + 0);
		glBindTexture(GL_TEXTURE_2D, sheet.getID(tile));
		Matrix4f target = projection.mul(new Matrix4f().translate(new Vector3f(xPos*2-width, yPos*2-height, 0)), new Matrix4f());
		
		float ratio = (((float)sheet.getHeight())/((float)sheet.getWidth()));
		target.mul(new Matrix4f().ortho2D(ratio*(mirrorX ? 1.0f : -1.0f), ratio*(mirrorX ? -1.0f : 1.0f), (mirrorY ? 1.0f : -1.0f), (mirrorY ? -1.0f : 1.0f)));
		target.scale(sheet.getHeight()*(onMap ? MAP_ZOOM : 1));
		if(color!=0) {
			colored_shader.bind();
			colored_shader.setUniform("color",
					((color>>24)&0xff)/255.0f,
					((color>>16)&0xff)/255.0f,
					((color>>8 )&0xff)/255.0f,
					((color    )&0xff)/255.0f);
			colored_shader.setUniform("sampler", 0);
			colored_shader.setUniform("projection", target);
		}else {
			default_shader.bind();
			default_shader.setUniform("sampler", 0);
			default_shader.setUniform("projection", target);
		}
		tileModel.render();
	}
	
	public static void drawMap(Map map){
		int textures[] = new int[4];
		Matrix4f target = null;
		float X, Y;
		map_shader.bind();
		map_shader.setUniform("sampler", 0);
		glActiveTexture(GL_TEXTURE0 + 0);

//		int nx = 0, ny = 0;
//		float ox = 0, oy = 0;
		for (float x = -RENDER_CHUNK_SIZE/2; x < width/2/MAP_ZOOM+RENDER_CHUNK_SIZE; x+=RENDER_CHUNK_SIZE) {
//			ny=0;
			for (float y = -RENDER_CHUNK_SIZE/2; y < height/2/MAP_ZOOM+RENDER_CHUNK_SIZE; y+=RENDER_CHUNK_SIZE) {
				for (int l : Map.LAYER_ALL) {
					textures[l] = map.getRenderChunk((int)(x+xOffset), (int)(y+yOffset), l);
				}
//				if(target == null) {
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
//					ox = target.m30();
//					oy = target.m31();
//				}
				
				map_shader.setUniform("projection", target);
				for (int l : Map.LAYER_ALL) {
					if(textures[l] == 0)continue;
					map_shader.setUniform("layer", l);
					glBindTexture(GL_TEXTURE_2D, textures[l]);
					tileModel.render();
				}
//				Game.ccFont.render((int)(x*2), (int)(y*2), nx+" "+ny, 0, 0xffff0000);
//				ny++;
//				target.m30(ox+target.m00()*2*nx);
//				target.m31(oy-target.m11()*2*ny);
			}
//			nx++;
		}
//		System.out.println(nx+" "+ny);
	}
}
