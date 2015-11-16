package item.itemList;

import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import item.*;
import main.InputHandler;
import map.Map;

public class NormalItemBag extends ItemBag {

	public NormalItemBag(){
		super(12);
		ID = 401;
		name = "NormalItemBag";
		gfx = new SpriteSheet("/Items/NormalItemBag.png");
	}

	public void useItem(InputHandler input, Player plr, Map map, Screen screen) {
	}
}
