package gui;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import gfx.SpriteSheet;
import main.Game;

public class Window {
	private long window;
	
	private int width, height;
	private boolean fullscreen;
	private String title;
	private String iconPath;
	
	public Window(int width, int height, boolean fullscreen, String title, String iconPath) {
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.title = title;
		this.iconPath = iconPath;
	}
	
	public void init() {
		createWindow(title);
		GL.createCapabilities();
		glfwSwapInterval(1);
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
		
		glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallbackI() {
			public void invoke(long window, int width, int height) {
				setSize(width, height);
				Game.resizeWindow(width, height);
			}
		});
		
		if(iconPath != null) setIcon(iconPath);
		
		glfwShowWindow(window);
		
		glfwMakeContextCurrent(window);
	}
	
	public void setIcon(String iconPath) {
		ByteBuffer buffer16 = getIconBuffer(iconPath + "_16x16.png");
		ByteBuffer buffer32 = getIconBuffer(iconPath + "_32x32.png");
		ByteBuffer buffer48 = getIconBuffer(iconPath + "_48x48.png");
		
		GLFWImage.Buffer icon = GLFWImage.malloc((buffer16!=null ? 1 : 0) + (buffer32!=null ? 1 : 0) + (buffer48!=null ? 1 : 0));
		int i = 0;
		if(buffer16 != null) {
			icon.position(i).width(16).height(16).pixels(buffer16);
			i++;
		}
		if(buffer32 != null) {
			icon.position(i).width(32).height(32).pixels(buffer32);
			i++;
		}
		if(buffer48 != null) {
			icon.position(i).width(48).height(48).pixels(buffer48);
			i++;
		}
		
		glfwSetWindowIcon(window, icon);
		
		icon.free();
	}
	
	private ByteBuffer getIconBuffer(String iconPath) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(iconPath));
		} catch (Exception e) {
			Game.logWarning("Icon missing at " + iconPath);
		}
		if(image == null){ return null;}
		
		int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		ByteBuffer buffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4);
		
		for (int i : pixels) {
			buffer.put((byte) ((i>>16)&0xff)).put((byte) ((i>>8)&0xff)).put((byte) ((i)&0xff)).put((byte) ((i>>24)&0xff));
		}
		
		buffer.flip();
		
		return buffer;
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
	
	public boolean isFullscreen() { return fullscreen; }
	public long getWindow() { return window; }
}
