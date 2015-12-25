package item;

import java.util.ArrayList;

import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import main.Game;
import main.InputHandler;
import map.Map;

public abstract class Item {
	
	protected int ID;
	protected String name;
	protected int stack;
	protected int stackMax;
	protected SpriteSheet gfx;
	protected SpriteSheet gfxs;
	protected int col;
	protected int anim;
	
	public Item(){
	}
	
	public String getName(){
		return name;
	}

	public int getID(){
		return ID;
	}

	public int getStack(){
		return stack;
	}

	public int getStackMax(){
		return stackMax;
	}

	public void setStack(int n) {
		if(n >= 0 && n <= this.stackMax)this.stack = n;
	}
	
	/**
	 * 
	 * @param n
	 * @return the number of items left, that did not fit inside the stack
	 */
	public int addStack(int n){
		while(stack < stackMax){
			if(n > 0) {
				stack ++;
				n--;
			}else {
				return n;
			}
		}return n;
	}

	/**
	 * removes n or as many as possible from <code>item</code> and returns the number left
	 * @param item
	 * @param n
	 * @return
	 */
	public int addStack(Item item, int n){
		while(stack < stackMax){
			if(n > 0) {
				this.stack++;
				item.stack--;
				n--;
			}else {
				return n;
			}
		}return n;
	}
	
	public int delStack(int n){
		while(stack > 0){
			if(n > 0) {
				stack --;
				n--;
			}else {
				return n;
			}
		}
		return n;
	}

	public void render(Screen screen, int x, int y, boolean showstack){
		screen.drawGUITile(x, y, 0, 0x00, gfx, col);
		if(showstack & stackMax != 1){
			switch(Game.configs.stacktype){
			case 1:
				y+=6;
				if(stack>999){Game.mfont.render(x-2, y, Integer.toString(stack), 0, 0xff000000, screen);break;}
				if(stack>99){Game.mfont.render(x, y, Integer.toString(stack), 0, 0xff000000, screen);break;}
				if(stack>9){Game.mfont.render(x+2, y, Integer.toString(stack), 0, 0xff000000, screen);break;}
				if(stack>1){Game.mfont.render(x+4, y, Integer.toString(stack), 0, 0xff000000, screen);break;}
				break;
			case 2:
				x-=1;y+=8;
				int r=255,g=0;
				for(int i = 1; i <= 8; i++){
					if(stack >= 128*i){screen.drawGUIScaled(x, y, 0xff000000 + (r<<16) + (g<<8));screen.drawGUIScaled(x+11, y, 0xff000000 + (r<<16) + (g<<8));}y--;
					if(g < 255) g+= 64;
					if(g > 255) g = 255;
					if(g == 255) r-= 64;
					if(r < 0) r = 0;
				}
				break;
			case 3:
				x-=1;y-=1;
				int n = stack;
				for(int i = 10; i >= 0; i--){
					if(n-Math.pow(2, i) >= 0){
						if(i == 10){
							screen.drawGUIScaled(x+(10-i), y, 0xffffffff);
						}else{
							if(i%2==0){
								screen.drawGUIScaled(x+(10-i), y, 0xffffff00);
							}else{
								screen.drawGUIScaled(x+(10-i), y, 0xffff00ff);
							}
						}
						n -= Math.pow(2, i);
					}
				}
				break;
			}
		}
	}
	
	public void render(Screen screen, int x, int y){
		screen.drawGUITile(x, y, 0, 0x00, gfx, col);
	}

	public void render(Screen screen, int x, int y, byte alpha){
		screen.drawGUITile(x, y, 0, alpha, gfx, col);
	}

	public void render(Screen screen, int x, int y, int mirror){
		screen.drawTile(x, y, 0, mirror, gfxs, col);
	}

	public int getAnim(){
		return anim;
	}

	public abstract void useItem(InputHandler input, Player plr, Map map, Screen screen);

	public abstract void setMouse();

	public abstract void save(ArrayList<Byte> file);

	public abstract void load(ArrayList<Byte> file);

}
