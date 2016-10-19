package gui;



import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import main.Game;

public class Window {
	private long window;
	
	private int width, height;
	private boolean fullscreen;
	private String title;
	
	public Window(int width, int height, boolean fullscreen, String title) {
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.title = title;
	}
	
	public void init() {
		createWindow(title);
		GL.createCapabilities();
		glfwSwapInterval(0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void setCallbacks() {
		glfwSetErrorCallback(new GLFWErrorCallbackI() {
			@Override
			public void invoke(int error, long description) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
			}
		});
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		//TODO automatically create new window with new resolution
	}
	
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
		//TODO automatically create new window with new resolution
	}
	
	public void createWindow(String title) {
		window = glfwCreateWindow(
				width, 
				height, 
				title, 
				fullscreen ? glfwGetPrimaryMonitor() : 0, 
				0);
		
		if(window == 0)
			throw new IllegalStateException("Failed to create window!");
		
		if(!fullscreen) {
			GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, 
					(vid.width()-width)/2, 
					(vid.height()-height)/2);
		}
		
		glfwShowWindow(window);
		
		glfwMakeContextCurrent(window);
	}
	
	public void update() {
		glfwPollEvents();
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(window);
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public boolean isFullscreen() { return fullscreen; }
	public long getWindow() { return window; }
}
