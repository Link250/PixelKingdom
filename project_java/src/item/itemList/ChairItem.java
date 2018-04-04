package item.itemList;

import entities.Player;
import gfx.Mouse;
import gfx.SpriteSheet;
import item.Item;
import main.MouseInput;
import map.Map;
import pixel.MultiPixel;
import pixel.PixelList;

public class ChairItem extends Item {
	MultiPixel<?> place;
	
	public ChairItem() {
		ID = 500;
		name = "Chair";
		displayName = "Chair";
		stack  = 1;
		stackMax  = 1;
		place = (MultiPixel<?>) PixelList.GetMat(65);
		gfx = new SpriteSheet("/Items/Chair.png");
	}
	
	public void holdItem(Player plr, Map map) {
		if(MouseInput.mousel.isPressed()) {
			if(map.placeMultiPixel(MouseInput.mouse.getMapX(), MouseInput.mouse.getMapY(), Map.LAYER_FRONT, place)) {
				stack--;
			}
			if(stack==0){
				plr.delItem(this);
			}
		}
		Mouse.mouseType = Mouse.MouseType.MULTIPIXEL;
		Mouse.multiPixel = place;
	}
}
