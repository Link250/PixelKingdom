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

	public void render(int x, int y, boolean showstack){
		Screen.drawGUISprite(x, y, gfx, 0, false, false, col);
		if(showstack & stackMax != 1){
			if(stack>=2000){Game.mfont.render(x, y+17, false, false, Integer.toString(stack), 0, 0xff000000);}
			if(stack>=1000){Game.mfont.render(x-5, y+17, false, false, Integer.toString(stack), 0, 0xff000000);}
			else if(stack>=100){Game.mfont.render(x+4, y+17, false, false, Integer.toString(stack), 0, 0xff000000);}
			else if(stack>=10){Game.mfont.render(x+13, y+17, false, false, Integer.toString(stack), 0, 0xff000000);}
			else if(stack>1){Game.mfont.render(x+22, y+17, false, false, Integer.toString(stack), 0, 0xff000000);}
		}
	}
	
	public void render(int x, int y, int stacksize){
		Screen.drawGUISprite(x, y, gfx, 0, false, false, col);
		if(stackMax != 1){
			if(stacksize>=2000){Game.mfont.render(x, y+17, false, false, Integer.toString(stacksize), 0, 0xff000000);}
			if(stacksize>=1000){Game.mfont.render(x-5, y+17, false, false, Integer.toString(stacksize), 0, 0xff000000);}
			else if(stacksize>=100){Game.mfont.render(x+4, y+17, false, false, Integer.toString(stacksize), 0, 0xff000000);}
			else if(stacksize>=10){Game.mfont.render(x+13, y+17, false, false, Integer.toString(stacksize), 0, 0xff000000);}
			else if(stacksize>1){Game.mfont.render(x+22, y+17, false, false, Integer.toString(stacksize), 0, 0xff000000);}
		}
	}
	
	public void render(int x, int y){
		Screen.drawGUISprite(x, y, gfx, 0, false, false, col);
	}

	public void renderOnMap(int x, int y, boolean mirrorX, boolean mirrorY){
		Screen.drawGUISprite(x, y, gfx, 0, mirrorX, mirrorY, col);
	}

	public int getAnim(){
		return anim;
	}

	public abstract void useItem(Player plr, Map map);

	public void setMouse() {
		Mouse.mouseType=MouseType.DEFAULT;
	}
	
	public abstract void save(ArrayList<Byte> file);

	public abstract void load(ArrayList<Byte> file);

}
