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

	public static double xOffset = 0;
	public static double yOffset = 0;
	
	public static int width;
	public static int height;
	
	private static Shader default_shader;
	private static Shader colored_shader;
	private static Shader map_shader;
	
	private static Matrix4f projection;
	private static Model tileModel;
	private static Model shadowModel;
	
	public static void initialize(int width, int height) {
		Screen.width = width;
		Screen.height = height;
		projection = new Matrix4f().setOrtho2D(-width, width, -height, height);
//		projection.rotate((float)Math.PI/32,0f,0f,1f);
		default_shader = new Shader("default_shader");
		colored_shader = new Shader("colored_shader");
		map_shader = new Shader("map_shader");
		tileModel = new Model();
		
		float[] vertices = new float[] {
				-1f, 1f, 0, //TOP LEFT     0
				1f, 1f, 0,  //TOP RIGHT    1
				1f, -1f, 0, //BOTTOM RIGHT 2
				-1f, -1f, 0,//BOTTOM LEFT  3
		};
		float diff = 1f/((float)(RENDER_CHUNK_SIZE));
		float[] tex_coords = new float[] {
				diff,diff,
				1-diff,diff,
				1-diff,1-diff,
				diff,1-diff,
		};
		
		int[] indices = new int[] {
				0,1,2,
				2,3,0
		};
		shadowModel = new Model(vertices, tex_coords, indices);
	}
	
	/**
	 * will set the centre of the camera to the specified position on the map
	 * @param x
	 * @param y
	 */
	public static void setMapPos(double x, double y) {
		xOffset= x-Screen.width/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;
		yOffset= y-Screen.height/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;
	}
	
	/**
	 * combines two colors
	 * @param colorBack
	 * @param colorFront
	 * @return
	 */
	public static int combineColors(int colorBack, int colorFront){
		float af = (colorFront>>24)&0xff;
		if(af != 0xff){
			int ab = (colorBack>>24)&0xff;
			if(ab != 0){
				int rf = (colorFront>>16)&0xff, gf = (colorFront>>8)&0xff, bf = colorFront&0xff;
				int rb,gb,bb;
				rb = (int)((af/255*rf) + ((255-af)/255*((colorBack>>16)&0xff)));
				gb = (int)((af/255*gf) + ((255-af)/255*((colorBack>> 8)&0xff)));
				bb = (int)((af/255*bf) + ((255-af)/255*((colorBack    )&0xff)));
				if(ab != 255){
					ab = (int) (af+(255-af)/255*((colorBack>>24)&0xff));
				}
				return (ab<<24)|(rb<<16)|(gb<<8)|bb;
			}else{
				return colorFront;
			}
		}else{
			return colorFront;
		}
	}
	
	public static void drawMapSprite(int xPos, int yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY, int color, boolean centered){
		drawSprite(xPos, yPos, sheet, tile, mirrorX, mirrorY, color, true, centered);
	}
	
	public static void drawMapSprite(int xPos, int yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY, int color){
		drawSprite(xPos, yPos, sheet, tile, mirrorX, mirrorY, color, true, false);
	}
	
	public static void drawMapSprite(int xPos, int yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY){
		drawSprite(xPos, yPos, sheet, tile, mirrorX, mirrorY, 0, true, false);
	}
	
	public static void drawMapSprite(int xPos, int yPos, SpriteSheet sheet, int tile){
		drawSprite(xPos, yPos, sheet, tile, false, false, 0, true, false);
	}
	
	public static void drawMapSprite(int xPos, int yPos, SpriteSheet sheet){
		drawSprite(xPos, yPos, sheet, 0, false, false, 0, true, false);
	}
	
	public static void drawGUISprite(int xPos, int yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY, int color, boolean centered){
		drawSprite(xPos, yPos, sheet, tile, mirrorX, mirrorY, color, false, centered);
	}
	
	public static void drawGUISprite(int xPos, int yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY, int color){
		drawSprite(xPos, yPos, sheet, tile, mirrorX, mirrorY, color, false, false);
	}
	
	public static void drawGUISprite(int xPos, int yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY){
		drawSprite(xPos, yPos, sheet, tile, mirrorX, mirrorY, 0, false, false);
	}
	
	public static void drawGUISprite(int xPos, int yPos, SpriteSheet sheet, int tile){
		drawSprite(xPos, yPos, sheet, tile, false, false, 0, false, false);
	}
	
	public static void drawGUISprite(int xPos, int yPos, SpriteSheet sheet){
		drawSprite(xPos, yPos, sheet, 0, false, false, 0, false, false);
	}
	
	public static void drawSprite(float xPos, float yPos, SpriteSheet sheet, int tile, boolean mirrorX, boolean mirrorY, int color, boolean onMap, boolean centered){
			if(onMap) {
				xPos-=xOffset;xPos*=MAP_SCALE*MAP_ZOOM;
				yPos-=yOffset;yPos*=MAP_SCALE*MAP_ZOOM;
				if(!centered) {
					xPos += sheet.getWidth()/2f*MAP_ZOOM;
					yPos += sheet.getHeight()/2f*MAP_ZOOM;
				}
			}else {
				if(!centered) {
					xPos += sheet.getWidth()/2f;
					yPos += sheet.getHeight()/2f;
				}
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
		int textures[] = new int[3];
		Matrix4f target = null;
		float X, Y;
		map_shader.bind();
		map_shader.setUniform("sampler", 0);
		glActiveTexture(GL_TEXTURE0 + 0);

		for (float x = -RENDER_CHUNK_SIZE/2; x < width/2/MAP_ZOOM+RENDER_CHUNK_SIZE; x+=RENDER_CHUNK_SIZE) {
			for (float y = -RENDER_CHUNK_SIZE/2; y < height/2/MAP_ZOOM+RENDER_CHUNK_SIZE; y+=RENDER_CHUNK_SIZE) {
				for (int l : Map.LAYER_ALL_PIXEL) {
					textures[l] = map.getRenderChunk((int)(x+xOffset), (int)(y+yOffset), l);
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
				
				map_shader.setUniform("projection", target);
				for (int l : Map.LAYER_ALL_PIXEL) {
					if(textures[l] == 0)continue;
					map_shader.setUniform("layer", l);
					glBindTexture(GL_TEXTURE_2D, textures[l]);
					tileModel.render();
				}
			}
		}
		
		map.renderSprites();
		
		map_shader.bind();
		map_shader.setUniform("sampler", 0);
		for (float x = -RENDER_CHUNK_SIZE/2; x < width/2/MAP_ZOOM+RENDER_CHUNK_SIZE; x+=RENDER_CHUNK_SIZE) {
			for (float y = -RENDER_CHUNK_SIZE/2; y < height/2/MAP_ZOOM+RENDER_CHUNK_SIZE; y+=RENDER_CHUNK_SIZE) {
				int tex = map.getRenderChunk((int)(x+xOffset), (int)(y+yOffset), Map.LAYER_LIGHT);
				if(tex == 0)continue;
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
				target.scale((RENDER_CHUNK_SIZE)*MAP_SCALE*MAP_ZOOM);
				
				map_shader.setUniform("projection", target);
				map_shader.setUniform("layer", Map.LAYER_LIGHT);
				glBindTexture(GL_TEXTURE_2D, tex);
				shadowModel.render();
			}
		}
	}
}
