package ItemList;

import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import Items.*;
import Main.InputHandler;
import Maps.Map;

public class SmallToolBag extends ToolBag {

	public SmallToolBag(){
		super(6);
		ID = 426;
		name = "SmallToolBag";
		gfx = new SpriteSheet("/Items/NormalToolBag.png");
	}
	
	public void useItem(InputHandler input, Player plr, Map map, Screen screen) {
	}
}
