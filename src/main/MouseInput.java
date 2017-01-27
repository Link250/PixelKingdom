package main;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

import gfx.Screen;

public class MouseInput{

	public static Mouse mouse = new Mouse();
	public static Mouse mousel = new Mouse();
	public static Mouse mouser = new Mouse();
	public static Mouse mousem = new Mouse();
	
	public MouseInput(long window) {
		
		glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallbackI(){
			public void invoke(long window, int button, int action, int mods) {
				invokeKey(window, button, action, mods);
			}
		});
		glfwSetScrollCallback(window, new GLFWScrollCallbackI(){
			public void invoke(long window, double xoffset, double yoffset) {
				invokeScroll(window, xoffset, yoffset);
			}
		});
		glfwSetCursorPosCallback(window, new GLFWCursorPosCallbackI() {
			public void invoke(long window, double xpos, double ypos) {
				invokeMove(window, xpos, ypos);
			}
		});
	}

	public void invokeKey(long window, int button, int action, int mods) {
		switch(button) {
		case GLFW_MOUSE_BUTTON_LEFT:
			mousel.toggle(action==1);
			if(mousel.clickable && action==1){
				mousel.setPos(mouse.x, mouse.y);
			}
			break;
		case GLFW_MOUSE_BUTTON_MIDDLE:
			mousem.toggle(action==1);
			if(mousem.clickable && action==1){
				mousem.setPos(mouse.x, mouse.y);
			}
			break;
		case GLFW_MOUSE_BUTTON_RIGHT:
			mouser.toggle(action==1);
			if(mouser.clickable && action==1){
				mouser.setPos(mouse.x, mouse.y);
			}
			break;
		}
	}
	
	public void invokeScroll(long window, double xoffset, double yoffset) {
		mouse.scrolled = (int)-yoffset;
//		System.out.println("xoffset"+xoffset);
//		System.out.println("yoffset"+yoffset+"\n");
	}
	
	public void invokeMove(long window, double xpos, double ypos) {
		mouse.setPos((int)xpos, (int)ypos);
	}
	
	public static class Mouse{
		//TODO change back to private later
		public boolean pressed = false;
		public boolean clickable = true;
		public int scrolled;
		public int x;
		public int y;
		
		public boolean isPressed(){
			return pressed;
		}
		public boolean isClickable(){
			return clickable;
		}
		
		public int getScroll(){
			int i = scrolled;
			scrolled = 0;
			return i;
		}
		
		public int getMapX() {
			return (int)(((double)x)/Screen.MAP_SCALE/Screen.MAP_ZOOM+Screen.xOffset);
		}
		
		public int getMapY() {
			return (int)(((double)y)/Screen.MAP_SCALE/Screen.MAP_ZOOM+Screen.yOffset);
		}
		
		public void refresh(){
			setPos(x,y);
		}
		
		public void setPos(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public void toggle(boolean isPressed){
			pressed = isPressed;
		}
		public boolean click(){
			if(clickable && pressed){
				clickable = false;
				return pressed;
			}else{
				if(!pressed){
					clickable = true;
				}
			}
			return false;
		}
	}
}
