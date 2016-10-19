package item.itemList;

import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import item.*;
import main.InputHandler_OLD;
import map.Map;

public class SmallToolBag extends ToolBag {

	public SmallToolBag(){
		super(6);
		ID = 426;
		name = "SmallToolBag";
		displayName = "Small Tool Bag";
		gfx = new SpriteSheet("/Items/NormalToolBag.png");
	}
	
	public void useItem(InputHandler_OLD input, Player plr, Map map, Screen screen) {
	}
}
