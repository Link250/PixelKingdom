package item.itemList;

import entities.Player;
import gfx.SpriteSheet;
import item.*;
import map.Map;

public class SmallBeltBag extends BeltBag {

	public SmallBeltBag(){
		super(8);
		ID = 476;
		name = "SmallBeltBag";
		displayName = "Small Belt Bag";
		gfx = new SpriteSheet("/Items/NormalBeltBag.png");
	}

	public void holdItem(Player plr, Map map) {
	}
}
