package item.itemList;

import gfx.SpriteSheet;
import item.Pickaxe;

public class StonePickaxe extends Pickaxe{
	
	public StonePickaxe(){
		super();
		ID = 301;
		name = "StonePickaxe";
		displayName = "Stone Pickaxe";
		col = 0xff606060;
		strength = 0.5;
		size = 3;
		range = 20;
		miningTier = 1;
		gfx = new SpriteSheet("/Items/Pickaxe_Stone.png");
	}
}
