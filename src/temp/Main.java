package temp;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;


public class Main {
	
	public Main() {
		Window.setCallbacks();
		
		int tileSize = 64;
		
		if(!glfwInit()) {
			System.err.println("GLFW Failed to initialize!");
			System.exit(1);
		}
		
		int width = 1900, height = 1000;
		
		Window window = new Window();
		window.setSize(width, height);
		window.setFullscreen(false);
		window.createWindow("Game");
		
		GL.createCapabilities();
		glfwSwapInterval(0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		Camera camera = new Camera(window.getWidth(), window.getHeight());
		glEnable(GL_TEXTURE_2D);
		
//		TileRenderer tiles = new TileRenderer();
		Shader shader = new Shader("shader");
		
		Screen screen = new Screen();
		
		Matrix4f world;
		world = new Matrix4f().setTranslation(new Vector3f(0));
//		world.scale(scale);

		int[] ids1 = new int[(width/tileSize) * (height/tileSize)];
		for (int i = 0; i < ids1.length; i++) {
			ids1[i] = Screen.genTexture(tileSize, tileSize);
		}
		int[] posX = new int[1000];
		for(int i = 0; i < posX.length; i++) {posX[i] = (int)(Math.random()*width);}
		int[] posY = new int[1000];
		for(int i = 0; i < posY.length; i++) {posY[i] = (int)(Math.random()*height);}
		int id2 = Screen.genTexture(50, 50);
		
		double frame_cap = 1000000000/60;
		
		double frame_time = 0;
		int frames = 0;
		int ticks = 0;
		
		double time = System.nanoTime();
		double unprocessed = 0;
		
		while(!window.shouldClose()) {
			boolean can_render = true;
			
			double time_2 = System.nanoTime();
			double passed = time_2 - time;
			unprocessed+=passed;
			frame_time +=passed;
			time = time_2;
			
			while(unprocessed >= frame_cap) {
				unprocessed-=frame_cap;
				
				if(window.getInput().isKeyReleased(GLFW_KEY_ESCAPE)) {
					glfwSetWindowShouldClose(window.getWindow(), true);
				}
				ticks++;
//				id2 = Screen.genTexture(width, height);
				//world update
				int i = (int)(Math.random()*(ids1.length-5));
				ids1[i] = Screen.genTexture(tileSize, tileSize);
				ids1[i+1] = Screen.genTexture(tileSize, tileSize);
				ids1[i+2] = Screen.genTexture(tileSize, tileSize);
				ids1[i+3] = Screen.genTexture(tileSize, tileSize);
				ids1[i+4] = Screen.genTexture(tileSize, tileSize);
			}
			window.update();
			if(frame_time >= 1000000000) {
				frame_time = 0;
				System.out.println("FPS: " + frames);
				frames = 0;
				System.out.println("ticks: " + ticks);
				ticks = 0;
			}
			
			if(can_render) {
				glClear(GL_COLOR_BUFFER_BIT);
				screen.renderMap(tileSize, tileSize, width, height, shader, world, camera, ids1);
				for (int i = 0; i < 100; i++) {
					screen.renderTile(posX[i], posY[i], 50, 50, shader, world, camera, id2);
				}
				//world.render(tiles, shader, camera, window);
//				screen.renderTile(0, 0, width, height, shader, world, camera, id2);
//				for (int i = 0; i < 100; i++) {
//					screen.renderTile((float)(Math.random()*width-width/2), (float)(Math.random()*height-height/2), 50, 50, shader, world, camera, id1);
//				}
				
				window.swapBuffers();
				frames++;
			}
		}
		
		glfwTerminate();
	}

	public static void main(String[] args) {
		new Main();
	}

}
