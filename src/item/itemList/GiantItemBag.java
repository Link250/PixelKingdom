package item.itemList;

import entities.Player;
import gfx.SpriteSheet;
import item.*;
import map.Map;

public class GiantItemBag extends ItemBag {

	public GiantItemBag(){
		super(30);
		ID = 403;
		name = "GiantItemBag";
		displayName = "Giant Item Bag";
		gfx = new SpriteSheet("/Items/NormalItemBag.png");
	}

	public void useItem(Player plr, Map map) {
	}
}
