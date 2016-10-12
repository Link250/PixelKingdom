package gfx;

import java.util.ArrayList;
import java.util.List;

import item.*;
import main.Game;
import main.PArea;

public class Mouse {
	public static enum MouseType{
		DEFAULT,ITEM,MINING,BUILDING,TEXT;
	}
	public static MouseType mouseType = MouseType.DEFAULT;
	public static byte mousesize = 0;
	public static Item Item;
	private static List<String> textList = new ArrayList<>();
	private static boolean textActive;
	private static PArea textField = new PArea(0,0,0,0);
	public static SpriteSheet mouseN = new SpriteSheet("/Mouse.png");
	
	public static void setText(List<String> list) {
		if(!list.equals(textField)) {
			textList = list;
			textField = new PArea(0,0,0,0);
			textList.forEach((s) -> {
				if(Game.sfont.renderLength(s, 0) > textField.width) textField.width = Game.sfont.renderLength(s, 0);
			});
			textField.height = 26*textList.size() + 8;
			textField.width += 6;
		}
		textActive = true;
	}
	
	public static void resetText() {
		textList = new ArrayList<>();
		textField = new PArea(0,0,0,0);
	}
	
	public static void render(){
		if(textActive)mouseType = MouseType.TEXT;
		if(Item!=null)mouseType = MouseType.ITEM;
		switch(mouseType){
		case MINING:
			for(int y = -mousesize; y <= mousesize; y++){
				for(int x = -mousesize; x <= mousesize; x++){
					if(Math.sqrt(x*x+y*y) < mousesize){
						Game.screen.drawGUIPixelScaled(Game.input.mouse.getMapX()+x, Game.input.mouse.getMapY()+y, 0xa0ff0000);
					}
				}
			}
			break;
		case BUILDING:
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
		case ITEM:
			if(Item==null)mouseType = MouseType.DEFAULT;
			Item.render(Game.screen, Game.input.mouse.x-5, Game.input.mouse.y-5);
			break;
		case TEXT:
			int x = Game.input.mouse.x+mouseN.width+2;
			int y = Game.input.mouse.y;
			if(x+textField.width>Game.screen.width) x -= textField.width+mouseN.width*2;
			if(y+textField.height>Game.screen.height) y -= textField.height-mouseN.height;
			Game.screen.drawGUIPixelBorder(x, y, textField.width, textField.height, 2, 0xff000000);
			Game.screen.drawGUIPixelArea(x+2, y+2, textField.width-4, textField.height-4, 0xffa0a0a0);
			for (String string : textList) {
				Game.sfont.render(x+4, y+4, false, false, string, 0, 0xff000000, Game.screen);
				y+=26;
			}
			textActive = false;
		case DEFAULT:
		default:
			Game.screen.drawGUITile(Game.input.mouse.x, Game.input.mouse.y, 0, 0x00, mouseN, 0);
			break;
		}
	}
}
