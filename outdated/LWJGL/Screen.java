package temp;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.nio.ByteBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class Screen {
		private Model tileModel;
		
		int textureID = 0;
		private Matrix4f proj;
		
		public Screen(int width, int height) {
			proj = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
			float[] vertices = new float[] {
					-1f, 1f, 0, //TOP LEFT     0
					1f, 1f, 0,  //TOP RIGHT    1
					1f, -1f, 0, //BOTTOM RIGHT 2
					-1f, -1f, 0,//BOTTOM LEFT  3
			};
			
			float[] texture = new float[] {
					0,0,
					1,0,
					1,1,
					0,1,
			};
			
			int[] indices = new int[] {
					0,1,2,
					2,3,0
			};
			
			tileModel = new Model(vertices, texture, indices);
		}
		
		public void renderTile(float x, float y, int w, int h, Shader shader, int id) {
			shader.bind();
			
			glActiveTexture(GL_TEXTURE0 + 0);
			glBindTexture(GL_TEXTURE_2D, id);
			
			Matrix4f target = proj.mul(new Matrix4f().translate(new Vector3f(x, y, 0)), new Matrix4f());
			if(h!=w)target.mul(new Matrix4f().ortho2D(-(h/w), (h/w), -1, 1));
			target.scale(w);
			
			shader.setUniform("sampler", 0);
			shader.setUniform("projection", target);
			
			tileModel.render();
		}
		
		public void renderMap(int w, int h, int screenW, int screenH, Shader shader, int[] ids) {
			shader.bind();
			int i = 0;
			
			glActiveTexture(GL_TEXTURE0 + 0);
			
			for (int x = -screenW/2; x < screenW/2+128; x+=w) {
				for (int y = -screenH/2; y < screenH/2+128; y+=h) {
					glBindTexture(GL_TEXTURE_2D, ids[i]);
					
					Matrix4f target = proj.mul(new Matrix4f().translate(new Vector3f(x, y, 0)), new Matrix4f());
					target.scale(w/2);
					
					shader.setUniform("sampler", 0);
					shader.setUniform("projection", target);
					
					tileModel.render();
					i++;if(i>=ids.length)i = 0;
				}
			}
			
		}
		
		public static int genTexture(int x, int y) {
			int width = x, height = y;
			
			int[] pixels_raw = new int[width * height * 4];
			
			int color = 0xffff0000 | (int)(Math.random()*0x10000);
			for (int i = 0; i < pixels_raw.length; i++) {
				pixels_raw[i] = color;
			}
			
			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					int pixel = pixels_raw[i*width + j];
					pixels.put((byte)((pixel >> 16) & 0xFF)); //RED
					pixels.put((byte)((pixel >> 8) & 0xFF));  //GREEN
					pixels.put((byte)(pixel & 0xFF));		  //BLUE
					pixels.put((byte)((pixel >> 24) & 0xFF)); //ALPHA
				}
			}
			
			pixels.flip();
			
			int textureObject = glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, textureObject);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
			return textureObject;
		}
	}
