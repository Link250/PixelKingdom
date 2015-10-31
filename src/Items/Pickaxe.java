package Items;

import java.util.ArrayList;

import ItemList.MatStack;
import Main.ConvertData;
import Main.Game;
import Maps.Map;
import Main.InputHandler;
import Pixels.Material;
import Pixels.PixelList;
import entities.Player;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;

public abstract class Pickaxe extends Tool{
	
	private double npxs = 0;
	Item item = new MatStack();

	public Pickaxe(){
		type = 1;
		name = "Pickaxe";
		stack  = 1;
		stackMax  = 1;
		gfx = new SpriteSheet("/Items/Pickaxe.png");
		gfxs = new SpriteSheet("/Items/Pickaxeh.png");
	}
	
	public void useItem(InputHandler input, Player plr, Map map, Screen screen) {
	anim = 0;
	
	if((input.mousel.isPressed() | input.mouser.isPressed()) && !plr.iscrouching && !plr.isinair){
		if(npxs <= 0)npxs += ((double)size*strength)/4;
		double r = 0;
		while(npxs > 0){
			r = size;
			int pX = 0, pY = 0;
			Material m = null;
			int l = Map.LAYER_FRONT;
			if(input.mousel.isPressed()){
				for(int x = -size; x <= size; x++){
					for(int y = -size; y <= size; y++){
						int X = input.mouse.x/Game.SCALE+screen.xOffset+x, Y = input.mouse.y/Game.SCALE+screen.yOffset+y;
						if(Math.sqrt(x*x+y*y) < r & Math.sqrt((plr.x-X)*(plr.x-X)+(plr.y-Y)*(plr.y-Y)) <= range){
							m = PixelList.GetMat(map.getID(X,Y,Map.LAYER_FRONT));
							if(m.ID!=0 && m.usePickaxe() <= strength && m.usePickaxe()>0){
								r = Math.sqrt(x*x+y*y);
								pX = X;pY = Y;
							}
						}
					}
				}
			}else{
				for(int x = -size; x <= size; x++){
					for(int y = -size; y <= size; y++){
						int X = input.mouse.x/Game.SCALE+screen.xOffset+x, Y = input.mouse.y/Game.SCALE+screen.yOffset+y;
						if(Math.sqrt(x*x+y*y) < r & Math.sqrt((plr.x-X)*(plr.x-X)+(plr.y-Y)*(plr.y-Y)) <= range){
							m = PixelList.GetMat(map.getID(X,Y,Map.LAYER_BACK));
							if(m.ID!=0 && m.usePickaxe() <= strength && m.usePickaxe()>0
									&& (map.getID(X-1,Y,Map.LAYER_BACK)==0 | map.getID(X+1,Y,Map.LAYER_BACK)==0
									  | map.getID(X,Y-1,Map.LAYER_BACK)==0 | map.getID(X,Y+1,Map.LAYER_BACK)==0)){
								r = Math.sqrt(x*x+y*y);
								pX = X;pY = Y;
							}
						}
					}
				}
				l=Map.LAYER_BACK;
			}
			m = PixelList.GetMat(map.getID(pX,pY,l));
			if(m.ID!=0){
//				System.out.println(map.getID(pX, pY, l));
				if(plr.PickUp(item, map.getID(pX, pY, l)) || Game.devmode){
					npxs -= PixelList.GetMat((byte)map.getID(pX, pY, l)).usePickaxe;
					map.setID(pX, pY, l, 0);
				}else{npxs = 0;}
			}else{npxs = 0;}
		}
		
		if((System.nanoTime()/100000000)%10 < 5)anim = 11;
		else anim = 10;
		}
	}
	
	public void setMouse(){
		Mouse.mousetype=1;
		Mouse.mousesize=(byte)size;
	}

	public void save(ArrayList<Byte> file) {
		ConvertData.I2B(file, ID);
		ConvertData.I2B(file, size);
	}

	public void load(ArrayList<Byte> file) {
		size = ConvertData.B2I(file);
	}
	
	public void upgrade(int up){
		size += up;
	}
}
