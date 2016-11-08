package item.itemList;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import dataUtils.conversion.ConvertData;
import entities.Player;
import gfx.Mouse;
import gfx.SpriteSheet;
import item.ItemList;
import item.Tool;
import main.MouseInput;
import map.Map;
import pixel.PixelList;

public class MagnifyingLens extends Tool {
	int layer = Map.LAYER_FRONT;
	
	public MagnifyingLens() {
		ID = 331;
		name = "MagnifyingLens";
		displayName = "Magnifying Lens";
		stack  = 1;
		stackMax  = 1;
		gfx = new SpriteSheet("/Items/Lighter.png");
		size = 1;
		type = 0;
		range = 10;
		tooltip.add("Hold over a Pixel to see its Name");
	}
	
	public void holdItem(Player plr, Map map) {
		MouseInput.mouse.refresh();
		List<String> text = new ArrayList<>();
		Point plrPos = new Point(plr.x, plr.y);
		if (plrPos.distance(MouseInput.mouse.getMapX(), MouseInput.mouse.getMapY()) <= 20){
			text.add(PixelList.GetPixel(map.getID(MouseInput.mouse.getMapX(), MouseInput.mouse.getMapY(), layer), layer).getDisplayName());
			Mouse.setText(text);
			Mouse.mouseType = Mouse.MouseType.TEXT;
			if(MouseInput.mousel.click()) {
				if(layer == Map.LAYER_FRONT) layer = Map.LAYER_BACK;
				else if(layer == Map.LAYER_BACK) layer = Map.LAYER_LIQUID;
				else if(layer == Map.LAYER_LIQUID) layer = Map.LAYER_FRONT;
			}
			if(MouseInput.mouser.click()) {
				map.spawnItemEntity(ItemList.NewItem("Lighter"), MouseInput.mouse.getMapX(), MouseInput.mouse.getMapY());
			}
		}
	}

	public void save(ArrayList<Byte> file) {
		ConvertData.I2B(file, ID);
	}

	public void load(ArrayList<Byte> file) {

	}
}
