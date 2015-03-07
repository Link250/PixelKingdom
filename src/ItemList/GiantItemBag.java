package ItemList;

import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import Items.*;
import Main.InputHandler;
import Maps.Map;

public class GiantItemBag extends ItemBag {

	public GiantItemBag(){
		super(40);
		ID = 403;
		name = "GiantItemBag";
		gfx = new SpriteSheet("/Items/NormalItemBag.png");
	}

	public void useItem(InputHandler input, Player plr, Map map, Screen screen) {
	}
}
