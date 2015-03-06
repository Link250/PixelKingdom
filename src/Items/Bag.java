package Items;

import Main.InputHandler;
import Maps.Map;
import entities.Player;
import gfx.Mouse;
import gfx.Screen;

public abstract class Bag extends Item {
	
	public Item[] inventory;
	
	public Bag(int size){
		stack=1;
		stackMax=1;
		anim = 0;
	}

	public abstract void useItem(InputHandler input, Player plr, Map map, Screen screen);

	public void setMouse() {
		Mouse.mousetype=0;
	}
	
}
