package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	
	Game game;
	
	public int lastKeyCode = 0;
	
	public Mouse mouse = new Mouse();
	public Mouse mousel = new Mouse();
	public Mouse mouser = new Mouse();
	public Mouse mousem = new Mouse();
	
	public InputHandler(Game game){
		this.game = game;
		game.addKeyListener(this);
		game.addMouseListener(this);
		game.addMouseMotionListener(this);
		game.addMouseWheelListener(this);
	}
	
	public class Mouse{
		private boolean pressed = false;
		private boolean clickable = true;
		public int scrolled;
		public int x;
		public int y;
		public int xMap;
		public int yMap;
		
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
		
		public void refresh(){
			setPos(x,y);
		}
		
		public void setPos(int x, int y){
			this.x = x;
			this.y = y;
			try{
				xMap = x/Game.SCALE+Game.screen.xOffset;
				yMap = y/Game.SCALE+Game.screen.yOffset;
			}catch(NullPointerException e){}
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
		if(Game.configs.keyConfig.containsKey(keyCode)) {
			Game.configs.keyConfig.get(keyCode).toggle(isPressed);
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
