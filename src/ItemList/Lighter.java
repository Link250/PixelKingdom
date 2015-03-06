package ItemList;

import java.util.ArrayList;

import Items.Tool;
import Main.ConvertData;
import Maps.Map;
import Main.InputHandler;
import Pixels.Fire;
import Pixels.PixelList;
import entities.Player;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;

public class Lighter extends Tool{
	
	public Lighter(){
		ID = 400;
		name = "Lighter";
		stack  = 1;
		stackMax  = 100;
		gfx = new SpriteSheet("/Items/Lighter.png");
		strength = 1;
		size = 1;
		type = 0;
		range = 10;
	}

	public void useItem(InputHandler input, Player plr, Map map, Screen screen) {
		if((input.mousel.isPressed()|input.mouser.isPressed())){
			int X = input.mouse.xMap, Y = input.mouse.yMap, L = 1;
			if(!input.mousel.isPressed()){L=3;}
			byte burntime = PixelList.GetMat(X, Y, map, L).burnable;
			if(burntime>0 && Math.sqrt(Math.pow(input.mouse.xMap-plr.x, 2)+Math.pow(input.mouse.yMap-plr.y, 2))<20){
				map.setID(X, Y, 32, L);
				((Fire)PixelList.GetMat(X, Y, map, L)).setTime(burntime, map);
				stack--;
			}
			if(stack==0){
				plr.delItem(this);
			}
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
