package ItemList;

import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import Items.*;
import Main.InputHandler;
import Maps.Map;

public class SmallBeltBag extends BeltBag {

	public SmallBeltBag(){
		super(8);
		ID = 476;
		name = "SmallBeltBag";
		gfx = new SpriteSheet("/Items/NormalBeltBag.png");
	}

	public void useItem(InputHandler input, Player plr, Map map, Screen screen) {
	}
}
