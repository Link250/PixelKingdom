package main;

import static main.KeyConfig.keyMapping;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

public class KeyInput{
	public static int lastKeyCode = 0;
	private static Focusable currentFocus;

	public KeyInput(long window) {
		glfwSetKeyCallback(window, new GLFWKeyCallbackI(){
			public void invoke(long window, int key, int scancode, int action, int mods) {
				invokeKey(window, key, scancode, action, mods);
			}
		});
		glfwSetCharCallback(window, new GLFWCharCallbackI(){
			public void invoke(long window, int codepoint) {
				invokeChar(window, codepoint);
			}
		});
	}
	
	public void invokeKey(long window, int key, int scancode, int action, int mods) {
//		System.out.println(glfwGetKeyName(key, scancode)+" "+mods);
		if(currentFocus!=null) {
			if(currentFocus.hasFocus()) {
				if(action==GLFW_PRESS || action==GLFW_REPEAT) {
					if(key == GLFW_KEY_ENTER)invokeChar(window, '\n');
					if(key == GLFW_KEY_BACKSPACE)invokeChar(window, -1);
				}
				if(key != GLFW_KEY_ESCAPE) return;
			}
			currentFocus.setFocus(false);
			currentFocus = null;
			return;
		}
		lastKeyCode = action != 0 ? key : 0;
		toggleKey(key, action != 0);
	}
	
	public void invokeChar(long window, int codepoint) {
		if(currentFocus!=null)currentFocus.useInput((char) codepoint);
	}
	
	public void toggleKey(int keyCode, boolean isPressed){
		if(keyMapping.containsKey(keyCode)) {
			keyMapping.get(keyCode).toggle(isPressed);
		}
	}
	
	public static void setFocus(Focusable newFocus) {
		currentFocus = newFocus;
	}
	
	public static interface Focusable{
		public void useInput(char c);
		public boolean hasFocus();
		public void setFocus(boolean focus);
	}
}
