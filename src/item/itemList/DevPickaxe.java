package item.itemList;

import gfx.SpriteSheet;
import item.Pickaxe;

public class DevPickaxe extends Pickaxe{
	
	public DevPickaxe(){
		super();
		ID = 333;
		name = "DevPickaxe";
		displayName = "Developer Pickaxe";
		col = 0xffff00ff;
		strength = 333;
		size = 8;
		range = 100;
		gfx = new SpriteSheet("/Items/Pickaxe_Dev.png");
	}
}
