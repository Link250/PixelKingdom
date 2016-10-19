package main;

import static main.KeyConfig.keyMapping;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import gfx.Screen;

public class InputHandler_OLD implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	
	Game game;
	
	public int lastKeyCode = 0;
	
	public MouseInput.Mouse mouse = new Mouse();
	public MouseInput.Mouse mousel = new Mouse();
	public MouseInput.Mouse mouser = new Mouse();
	public MouseInput.Mouse mousem = new Mouse();
	
	public InputHandler_OLD(Game game){
		this.game = game;
		game.addKeyListener(this);
		game.addMouseListener(this);
		game.addMouseMotionListener(this);
		game.addMouseWheelListener(this);
		mouse = MouseInput.mouse;
		mousel = MouseInput.mousel;
		mousem = MouseInput.mousem;
		mouser = MouseInput.mouser;
	}
	
	public class Mouse extends MouseInput.Mouse{
		private boolean pressed = false;
		private boolean clickable = true;
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
			return x/Screen.MAP_SCALE/Screen.MAP_ZOOM+Game.screen.xOffset;
		}
		
		public int getMapY() {
			return y/Screen.MAP_SCALE/Screen.MAP_ZOOM+Game.screen.yOffset;
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
	
	public void keyPressed(KeyEvent e){
		this.lastKeyCode = e.getKeyCode();
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e){
		this.lastKeyCode = 0;
		toggleKey(e.getKeyCode(), false);
	}
	
	public void keyTyped(KeyEvent e){
		
	}
	
	public void toggleKey(int keyCode, boolean isPressed){
		if(keyMapping.containsKey(keyCode)) {
			keyMapping.get(keyCode).toggle(isPressed);
		}
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		int key = e.getButton();
		if(key == 1){
			mousel.toggle(true);
			if(mousel.clickable){
				mousel.setPos(e.getX(), e.getY());
			}
		}
		if(key == 2){
			mousem.toggle(true);
			if(mousem.clickable){
				mousem.setPos(e.getX(), e.getY());
			}
		}
		if(key == 3){
			mouser.toggle(true);
			if(mouser.clickable){
				mouser.setPos(e.getX(), e.getY());
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		int key = e.getButton();
		if(key == 1){
			mousel.toggle(false);
		}
		if(key == 2){
			mousem.toggle(false);
		}
		if(key == 3){
			mouser.toggle(false);
		}
	}

	public void mouseDragged(MouseEvent e) {
		mouse.setPos(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		mouse.setPos(e.getX(), e.getY());
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		mouse.scrolled = e.getWheelRotation();
	}
}
