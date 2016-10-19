package item.itemList;

import java.util.ArrayList;

import dataUtils.conversion.ConvertData;
import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import item.Tool;
import main.InputHandler_OLD;
import map.Map;
import pixel.PixelList;
import pixel.pixelList.Fire;

public class Lighter extends Tool{
	
	public Lighter(){
		ID = 400;
		name = "Lighter";
		displayName = "Lighter";
		stack  = 1;
		stackMax  = 100;
		gfx = new SpriteSheet("/Items/Lighter.png");
		strength = 1;
		size = 1;
		type = 0;
		range = 10;
	}

	public void useItem(InputHandler_OLD input, Player plr, Map map, Screen screen) {
		if((input.mousel.isPressed()|input.mouser.isPressed())){
			int X = input.mouse.getMapX(), Y = input.mouse.getMapY(), L = Map.LAYER_FRONT;
			if(!input.mousel.isPressed()){L=Map.LAYER_BACK;}
			byte burntime = PixelList.GetMat(X, Y, map, L).burnable;
			if(burntime>0 && Math.sqrt(Math.pow(input.mouse.getMapX()-plr.x, 2)+Math.pow(input.mouse.getMapY()-plr.y, 2))<20){
				map.setID(X, Y, L, 32);
				((Fire)PixelList.GetMat(X, Y, map, L)).setTime(X, Y, L, burntime, map);
				stack--;
			}
			if(stack==0){
				plr.delItem(this);
			}
		}
	}
	
	public void save(ArrayList<Byte> file) {
		ConvertData.I2B(file, ID);
		ConvertData.I2B(file, stack);
	}

	public void load(ArrayList<Byte> file) {
		stack = ConvertData.B2I(file);
	}
}
