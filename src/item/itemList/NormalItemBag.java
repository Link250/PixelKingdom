package item.itemList;

import entities.Player;
import gfx.SpriteSheet;
import item.*;
import map.Map;

public class NormalItemBag extends ItemBag {

	public NormalItemBag(){
		super(12);
		ID = 401;
		name = "NormalItemBag";
		displayName = "Normal Item Bag";
		gfx = new SpriteSheet("/Items/NormalItemBag.png");
	}

	public void holdItem(Player plr, Map map) {
	}
}
