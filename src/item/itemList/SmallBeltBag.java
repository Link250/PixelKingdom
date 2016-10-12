package item.itemList;

import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import item.*;
import main.InputHandler;
import map.Map;

public class SmallBeltBag extends BeltBag {

	public SmallBeltBag(){
		super(8);
		ID = 476;
		name = "SmallBeltBag";
		displayName = "Small Belt Bag";
		gfx = new SpriteSheet("/Items/NormalBeltBag.png");
	}

	public void useItem(InputHandler input, Player plr, Map map, Screen screen) {
	}
}
