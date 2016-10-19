package item.itemList;

import java.util.ArrayList;

import dataUtils.conversion.ConvertData;
import entities.Player;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;
import gfx.Mouse.MouseType;
import item.Item;
import main.Game;
import main.InputHandler_OLD;
import map.Map;
import pixel.PixelList;

public class MatStack extends Item{
	private byte buildsize = 2;
	
	public MatStack(){
		ID = 1;
		name = PixelList.GetMat((byte)ID).getName();
		displayName = PixelList.GetMat((byte)ID).getDisplayName();
		stack  = 1;
		stackMax  = 1024;
		gfx = new SpriteSheet("/Items/Material.png");
		gfxs = new SpriteSheet("/Items/Materialh.png");
		col = Game.csheetf.pixels[ID];
	}

	public void useItem(InputHandler_OLD input, Player plr, Map map, Screen screen) {
		input.mouse.refresh();
		anim = 12;
		int l = Map.LAYER_FRONT;if(!input.mousel.isPressed())l = Map.LAYER_BACK;
		if((input.mousel.isPressed() | input.mouser.isPressed()) && !plr.iscrouching
		&& Math.sqrt(Math.pow(plr.x-(input.mouse.getMapX()),2)+Math.pow(plr.y-(input.mouse.getMapY()), 2)) <= 25){
			for(int y = -buildsize+1; y < buildsize; y++){
				for(int x = -buildsize+1; x < buildsize; x++){
					if(stack>0 && map.getID(input.mouse.getMapX()+x, input.mouse.getMapY()+y,l)==0){
						map.setID(input.mouse.getMapX()+x,input.mouse.getMapY()+y,l,ID);
						stack--;
					}
					if(stack==0){
						plr.delItem(this);
						Mouse.mouseType=MouseType.DEFAULT;
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
	
	public void setMat(int mat){
		ID = mat;
		name = PixelList.GetMat((byte)ID).getName();
		displayName = PixelList.GetMat((byte)ID).getDisplayName();
		col = Game.csheetf.pixels[ID];
	}

	public void setMouse() {
		Mouse.mouseType=MouseType.BUILDING;
		Mouse.mousesize=buildsize;
	}

	public void save(ArrayList<Byte> file) {
		ConvertData.I2B(file, ID);
		ConvertData.I2B(file, stack);
	}

	public void load(ArrayList<Byte> file) {
		stack = ConvertData.B2I(file);
	}
}
