package item.itemList;

import gfx.SpriteSheet;
import item.Pickaxe;

public class IronPickaxe extends Pickaxe{
	
	public IronPickaxe(){
		super();
		ID = 302;
		name = "IronPickaxe";
		displayName = "Iron Pickaxe";
		col = 0xffA0A0A0;
		strength = 3;
		size = 5;
		range = 30;
		gfx = new SpriteSheet("/Items/Pickaxe_Iron.png");
	}
}
