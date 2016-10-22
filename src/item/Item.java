package item;

import java.util.ArrayList;
import java.util.List;

import entities.Player;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;
import gfx.Mouse.MouseType;
import main.Game;
import map.Map;

public abstract class Item {
	
	protected int ID;
	protected String name;
	protected String displayName;
	protected List<String> tooltip = new ArrayList<>();
	protected int stack;
	protected int stackMax;
	protected SpriteSheet gfx;
	protected SpriteSheet gfxs;
	protected int col;
	protected int anim;
	
	public Item(){
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	/**
	 * 
	 * @return displayName +  all tooltips as a List
	 */
	public List<String> getTooltip() {
		List<String> tempList = new ArrayList<>();
		tempList.add(displayName!=null ? displayName : (name != null ? name : "null"));
		tempList.addAll(tooltip);
		return tempList;
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
		screen.drawTileOGL(x, y, 0, gfx);
//		screen.drawGUITile(x, y, 0, 0x00, gfx, col);
		if(showstack & stackMax != 1){
			if(stack>=2000){Game.mfont.render(x, y+17, false, false, Integer.toString(stack), 0, 0xff000000);}
			if(stack>=1000){Game.mfont.render(x-5, y+17, false, false, Integer.toString(stack), 0, 0xff000000);}
			else if(stack>=100){Game.mfont.render(x+4, y+17, false, false, Integer.toString(stack), 0, 0xff000000);}
			else if(stack>=10){Game.mfont.render(x+13, y+17, false, false, Integer.toString(stack), 0, 0xff000000);}
			else if(stack>1){Game.mfont.render(x+22, y+17, false, false, Integer.toString(stack), 0, 0xff000000);}
		}
	}
	
	public void render(Screen screen, int x, int y){
		screen.drawTileOGL(x, y, 0, gfx);
//		screen.drawGUITile(x, y, 0, 0x00, gfx, col);
	}

	public void render(Screen screen, int x, int y, byte alpha){
		screen.drawTileOGL(x, y, 0, gfx);
//		screen.drawGUITile(x, y, 0, alpha, gfx, col);
	}

	public void renderOnMap(Screen screen, int x, int y, int mirror){
		screen.drawTileOGL(x, y, 0, gfx);
//		screen.drawMapTile(x, y, 0, mirror, gfxs, col);
	}

	public int getAnim(){
		return anim;
	}

	public abstract void useItem(Player plr, Map map, Screen screen);

	public void setMouse() {
		Mouse.mouseType=MouseType.DEFAULT;
	}
	
	public abstract void save(ArrayList<Byte> file);

	public abstract void load(ArrayList<Byte> file);

}
