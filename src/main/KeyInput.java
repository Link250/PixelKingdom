package main;

import static main.KeyConfig.keyMapping;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallbackI;

public class KeyInput implements GLFWKeyCallbackI{
	public static int lastKeyCode = 0;

	public KeyInput(long window) {
		glfwSetKeyCallback(window, this);
	}
	
	public void invoke(long window, int key, int scancode, int action, int mods) {
		lastKeyCode = key;
		toggleKey(key, action != 0);
	}
	
	public void toggleKey(int keyCode, boolean isPressed){
		if(keyMapping.containsKey(keyCode)) {
			keyMapping.get(keyCode).toggle(isPressed);
		}
	}
}
