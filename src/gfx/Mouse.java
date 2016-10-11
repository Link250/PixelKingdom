package gfx;

import item.*;
import main.Game;

public class Mouse {
	public static byte mousetype = 0;
	public static byte mousesize = 0;
	public static Item Item;
	public static SpriteSheet mouseN = new SpriteSheet("/Mouse.png");
	
	public static void render(){
		if(Item!=null)mousetype = 3;
		switch(mousetype){
		case 1:
			for(int y = -mousesize; y <= mousesize; y++){
				for(int x = -mousesize; x <= mousesize; x++){
					if(Math.sqrt(x*x+y*y) < mousesize){
						Game.screen.drawGUIPixelScaled(Game.input.mouse.getMapX()+x, Game.input.mouse.getMapY()+y, 0xa0ff0000);
					}
				}
			}
			break;
		case 2:
			for(int y = -mousesize; y <= mousesize; y++){
				for(int x = -mousesize; x <= mousesize; x++){
					if(x == mousesize || y == mousesize || x == -mousesize || y == -mousesize){
						Game.screen.drawGUIPixelScaled(Game.input.mouse.getMapX()+x, Game.input.mouse.getMapY()+y, 0xa0ff0000);
					}
				}
			}
			for(int y = (int)(-mousesize*1.5); y <= (int)(mousesize*1.5); y++){
				if(Math.abs(y) > mousesize)Game.screen.drawGUIPixelScaled(Game.input.mouse.getMapX(), Game.input.mouse.getMapY()+y, 0xa0ff0000);
			}
			for(int x = (int)(-mousesize*1.5); x <= (int)(mousesize*1.5); x++){
				if(Math.abs(x) > mousesize)Game.screen.drawGUIPixelScaled(Game.input.mouse.getMapX()+x, Game.input.mouse.getMapY(), 0xa0ff0000);
			}
			break;
		case 3:
			if(Item==null)mousetype = 0;
			Item.render(Game.screen, Game.input.mouse.x-5, Game.input.mouse.y-5);
			Game.sfont.render(Game.input.mouse.x+5, Game.input.mouse.y, Item.getName(), 0, 0xff000000, Game.screen);
			break;
		default:
			Game.screen.drawGUITile(Game.input.mouse.x, Game.input.mouse.y, 0, 0x00, mouseN, 0);
		}
	}
}
