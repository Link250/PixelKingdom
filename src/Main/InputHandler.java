package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	
	Game game;
	
	public InputHandler(Game game){
		this.game = game;
		game.addKeyListener(this);
		game.addMouseListener(this);
		game.addMouseMotionListener(this);
		game.addMouseWheelListener(this);
	}
	
	public class Key{
		private boolean pressed = false;
		private boolean clickable = true;
		
		public boolean isPressed(){
			return pressed;
		}
		public boolean isClickable(){
			return clickable;
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
				xMap = x/Game.SCALE+game.screen.xOffset;
				yMap = y/Game.SCALE+game.screen.yOffset;
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
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key space = new Key();
	public Key shift = new Key();
	public Key inv = new Key();
	public Key equip = new Key();
	public Key craft = new Key();
	public Key A = new Key();
	public Key B = new Key();
	public Key C = new Key();
	public Key D = new Key();
	public Key E = new Key();
	public Key F = new Key();
	public Key G = new Key();
	public Key H = new Key();
	public Key I = new Key();
	public Key J = new Key();
	public Key K = new Key();
	public Key L = new Key();
	public Key M = new Key();
	public Key N = new Key();
	public Key O = new Key();
	public Key P = new Key();
	public Key Q = new Key();
	public Key R = new Key();
	public Key S = new Key();
	public Key T = new Key();
	public Key U = new Key();
	public Key V = new Key();
	public Key W = new Key();
	public Key X = new Key();
	public Key Y = new Key();
	public Key Z = new Key();
	public Key Esc = new Key();
	public Key F3 = new Key();
	public Key F5 = new Key();
	public Key F12 = new Key();
	public Mouse mouse = new Mouse();
	public Mouse mousel = new Mouse();
	public Mouse mouser = new Mouse();
	public Mouse mousem = new Mouse();
	
	public void keyPressed(KeyEvent e){
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e){
		toggleKey(e.getKeyCode(), false);
	}
	
	public void keyTyped(KeyEvent e){
		
	}
	
	public void toggleKey(int keyCode, boolean isPressed){
		if(keyCode == KeyEvent.VK_W){up.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_S){down.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_A){left.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_D){right.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_SPACE){space.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_SHIFT){shift.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_ESCAPE){Esc.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_E){equip.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_C){craft.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_I){inv.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_F3){F3.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_F5){F5.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_F12){F12.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_H){H.toggle(isPressed);mousem.x=Game.WIDTH/2*Game.SCALE;mousem.y=Game.HEIGHT/2*Game.SCALE;}
		if(keyCode == KeyEvent.VK_B){B.toggle(isPressed);}
		if(keyCode == KeyEvent.VK_X){X.toggle(isPressed);}
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
