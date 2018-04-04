package item.itemList;

import entities.Player;
import gfx.SpriteSheet;
import item.*;
import map.Map;

public class SmallToolBag extends ToolBag {

	public SmallToolBag(){
		super(6);
		ID = 426;
		name = "SmallToolBag";
		displayName = "Small Tool Bag";
		gfx = new SpriteSheet("/Items/NormalToolBag.png");
	}
	
	public void holdItem(Player plr, Map map) {
	}
}
