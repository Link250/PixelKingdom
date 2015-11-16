package item.itemList;

import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import item.*;
import main.InputHandler;
import map.Map;

public class GiantItemBag extends ItemBag {

	public GiantItemBag(){
		super(30);
		ID = 403;
		name = "GiantItemBag";
		gfx = new SpriteSheet("/Items/NormalItemBag.png");
	}

	public void useItem(InputHandler input, Player plr, Map map, Screen screen) {
	}
}
