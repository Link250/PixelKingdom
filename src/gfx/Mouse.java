package gfx;

import item.*;
import main.Game;

public class Mouse {
	public static byte mousetype = 0;
	public static byte mousesize = 0;
	public static Item Item;
	public static SpriteSheet mouseN = new SpriteSheet("/Mouse.png");
	
	public static void render(Game game){
		if(Item!=null)mousetype = 3;
		switch(mousetype){
		case 1:
			for(int y = -mousesize; y <= mousesize; y++){
				for(int x = -mousesize; x <= mousesize; x++){
					if(Math.sqrt(x*x+y*y) < mousesize){
						game.screen.drawGUIScaled(game.input.mouse.x/Game.SCALE+game.screen.xOffset+x, game.input.mouse.y/Game.SCALE+game.screen.yOffset+y, 0xa0ff0000);
					}
				}
			}
			break;
		case 2:
			for(int y = -mousesize; y <= mousesize; y++){
				for(int x = -mousesize; x <= mousesize; x++){
					if(x == mousesize || y == mousesize || x == -mousesize || y == -mousesize){
						game.screen.drawGUIScaled(game.input.mouse.x/Game.SCALE+game.screen.xOffset+x, game.input.mouse.y/Game.SCALE+game.screen.yOffset+y, 0xa0ff0000);
					}
				}
			}
			for(int y = (int)(-mousesize*1.5); y <= (int)(mousesize*1.5); y++){
				if(Math.abs(y) > mousesize)game.screen.drawGUIScaled(game.input.mouse.x/Game.SCALE+game.screen.xOffset, game.input.mouse.y/Game.SCALE+game.screen.yOffset+y, 0xa0ff0000);
			}
			for(int x = (int)(-mousesize*1.5); x <= (int)(mousesize*1.5); x++){
				if(Math.abs(x) > mousesize)game.screen.drawGUIScaled(game.input.mouse.x/Game.SCALE+game.screen.xOffset+x, game.input.mouse.y/Game.SCALE+game.screen.yOffset, 0xa0ff0000);
			}
			break;
		case 3:
			if(Item==null)mousetype = 0;
			Item.render(game.screen, game.input.mouse.x/Game.SCALE+game.screen.xOffset-5, game.input.mouse.y/Game.SCALE+game.screen.yOffset-5);
			Game.sfont.render(game.input.mouse.x/Game.SCALE+game.screen.xOffset+5, game.input.mouse.y/Game.SCALE+game.screen.yOffset, Item.getName(), 0, 0xff000000, game.screen);
			break;
		default:
			game.screen.drawGUITile(game.input.mouse.x/Game.SCALE+game.screen.xOffset, game.input.mouse.y/Game.SCALE+game.screen.yOffset, 0, 0x00, mouseN, 0);
		}
	}
}
