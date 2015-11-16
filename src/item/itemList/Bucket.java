package item.itemList;

import java.util.ArrayList;

import entities.Player;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;
import item.Item;
import main.ConvertData;
import main.InputHandler;
import map.Map;

public class Bucket extends Item {

	private byte buildsize = 2;

	public Bucket() {
		ID = 332;
		name = "Bucket";
		stack  = 1;
		stackMax  = 0;
		gfx = new SpriteSheet("/Items/Bucket.png");
	}
	
	public void useItem(InputHandler input, Player plr, Map map, Screen screen) {
		input.mouse.refresh();
		if((input.mousel.isPressed()|input.mouser.isPressed())){
			int X = input.mouse.xMap, Y = input.mouse.yMap;
			for(int y = -buildsize+1; y < buildsize; y++){
				for(int x = -buildsize+1; x < buildsize; x++){
					if((input.mousel.isPressed())){
						if(map.getID(X+x, Y+y, Map.LAYER_LIQUID)!=1)map.setID(X+x, Y+y, Map.LAYER_LIQUID, 1);
					}else {
						if(map.getID(X+x, Y+y, Map.LAYER_LIQUID)!=0)map.setID(X+x, Y+y, Map.LAYER_LIQUID, 0);
					}
				}
			}
		}
		if(input.mouse.scrolled!=0){
			buildsize -= input.mouse.getScroll();
			if(buildsize < 1)buildsize = 1;
			if(buildsize > 10)buildsize = 10;
			Mouse.mousesize=buildsize;
		}
	}
	
	public void setMouse() {
		Mouse.mousetype=0;
	}
	
	public void save(ArrayList<Byte> file) {
		ConvertData.I2B(file, ID);
		ConvertData.I2B(file, stack);
	}

	public void load(ArrayList<Byte> file) {
		stack = ConvertData.B2I(file);
	}

}
