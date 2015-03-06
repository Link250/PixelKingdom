package ItemList;

import java.util.ArrayList;

import Items.Item;
import Main.ConvertData;
import Main.Game;
import Maps.Map;
import Main.InputHandler;
import Pixels.PixelList;
import entities.Player;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;

public class MatStack extends Item{
	private byte buildsize = 2;
	
	public MatStack(){
		ID = 1;
		name = PixelList.GetMat((byte)ID).getName();
		stack  = 1;
		stackMax  = 1024;
		gfx = new SpriteSheet("/Items/Material.png");
		gfxs = new SpriteSheet("/Items/Materialh.png");
		col = Game.csheetf.pixels[ID];
	}

	public void useItem(InputHandler input, Player plr, Map map, Screen screen) {
		input.mouse.refresh();
		anim = 12;
		int l = 1;if(!input.mousel.isPressed())l+=2;
		if((input.mousel.isPressed() | input.mouser.isPressed()) && !plr.iscrouching
		&& Math.sqrt(Math.pow(plr.x-(input.mouse.xMap),2)+Math.pow(plr.y-(input.mouse.yMap), 2)) <= 25){
			for(int y = -buildsize+1; y < buildsize; y++){
				for(int x = -buildsize+1; x < buildsize; x++){
					if(stack>0 && map.getID(input.mouse.xMap+x, input.mouse.yMap+y,l)==0){
						map.setID(input.mouse.xMap+x,input.mouse.yMap+y,ID,l);
						stack--;
					}
					if(stack==0){
						plr.delItem(this);
						Mouse.mousetype=0;
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
		col = Game.csheetf.pixels[ID];
	}

	public void setMouse() {
		Mouse.mousetype=2;
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
