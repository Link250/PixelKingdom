package gfx;

import java.util.ArrayList;
import java.util.List;

import dataUtils.PArea;
import item.*;
import main.Game;
import main.MouseInput;

public class Mouse {
	public static enum MouseType{
		DEFAULT,ITEM,MINING,BUILDING,TEXT;
		
		private SpriteSheet sheet;
	}
	public static MouseType mouseType = MouseType.DEFAULT;
	public static byte mousesize = 0;
	public static Item Item;
	private static List<String> textList = new ArrayList<>();
	private static boolean textActive;
	private static PArea textField;
	
	public static void setText(List<String> list) {
		if(!list.equals(textField)) {
			textField = new PArea(0, 0, 1, 1);
			textList = list;
			textList.forEach((s) -> {
				if(Game.sfont.renderLength(s, 0) > textField.width) textField.width = Game.sfont.renderLength(s, 0);
			});
			textField.setSize(textField.width+6, 26*textList.size() + 8);
		}
		textActive = true;
	}
	
	public static void resetText() {
		textList = new ArrayList<>();
		textField = new PArea(0,0,1,1);
	}
	
	public static void render(){
		if(textActive)mouseType = MouseType.TEXT;
		if(Item!=null)mouseType = MouseType.ITEM;
		switch(mouseType){
		case MINING:
			if(mouseType.sheet==null || (mouseType.sheet.getWidth()-1)/2 != mousesize)genMouseTexture(mouseType);
			Screen.drawMapSprite(MouseInput.mouse.getMapX()-mousesize, MouseInput.mouse.getMapY()-mousesize, mouseType.sheet);
			break;
		case BUILDING:
			if(mouseType.sheet==null || (mouseType.sheet.getWidth()-3)/2 != mousesize)genMouseTexture(mouseType);
			Screen.drawMapSprite(MouseInput.mouse.getMapX()-mousesize-1, MouseInput.mouse.getMapY()-mousesize-1, mouseType.sheet);
			break;
		case ITEM:
			if(Item==null)mouseType = MouseType.DEFAULT;
			Item.render(MouseInput.mouse.x-5, MouseInput.mouse.y-5);
			break;
		case TEXT:
			int x = MouseInput.mouse.x+MouseType.DEFAULT.sheet.getWidth()+2;
			int y = MouseInput.mouse.y;
			if(x+textField.width>Screen.width) x -= textField.width+MouseType.DEFAULT.sheet.getWidth()*2;
			if(y+textField.height>Screen.height) y -= textField.height-MouseType.DEFAULT.sheet.getHeight();
			textField.setPosition(x, y);
			textField.showArea();
			for (String string : textList) {
				Game.sfont.render(x+4, y+4, false, false, string, 0, 0xff000000);
				y+=26;
			}
			textActive = false;
		case DEFAULT:
		default:
			if(mouseType.sheet==null)genMouseTexture(MouseType.DEFAULT);
			Screen.drawGUISprite(MouseInput.mouse.x, MouseInput.mouse.y, MouseType.DEFAULT.sheet);
			break;
		}
	}
	
	private static void genMouseTexture(MouseType type) {
		int width;
		int[] pixels;
		int[] pixelsScaled;
		int s = Screen.MAP_SCALE;
		switch(type){
		case MINING:
			width = 2*mousesize+1;
			pixels = new int[width*width];
			for(int y = -mousesize; y <= mousesize; y++){
				for(int x = -mousesize; x <= mousesize; x++){
					if(Math.sqrt(x*x+y*y) < mousesize){
						pixels[(x+mousesize)+(y+mousesize)*width] = 0xa0ff0000;
					}
				}
			}
			pixelsScaled = new int[pixels.length*s*s];
			for (int x = 0; x < width*s; x++) {
				for (int y = 0; y < width*s; y++) {
					pixelsScaled[x+y*width*s] = pixels[(int)(x/s)+((int)(y/s))*width];
				}
			}
			type.sheet = new SpriteSheet(pixelsScaled, width*s, width*s, width*s, width*s);
			break;
		case BUILDING:
			width = 2*mousesize+3;
			pixels = new int[width*width];
			for(int y = -mousesize; y <= mousesize; y++){
				for(int x = -mousesize; x <= mousesize; x++){
					if(x == mousesize || y == mousesize || x == -mousesize || y == -mousesize){
						pixels[(x+mousesize+1)+(y+mousesize+1)*width] = 0xa0ff0000;
					}
				}
			}
			pixels[mousesize+1+0] = 0xa0ff0000;
			pixels[mousesize+1+(mousesize*2+2)*width] = 0xa0ff0000;
			pixels[0+(mousesize+1)*width] = 0xa0ff0000;
			pixels[(mousesize*2+2)+(mousesize+1)*width] = 0xa0ff0000;
			pixelsScaled = new int[pixels.length*s*s];
			for (int x = 0; x < width*s; x++) {
				for (int y = 0; y < width*s; y++) {
					pixelsScaled[x+y*width*s] = pixels[(int)(x/s)+((int)(y/s))*width];
				}
			}
			type.sheet = new SpriteSheet(pixelsScaled, width*s, width*s, width*s, width*s);
			break;
		case TEXT:break;
		case DEFAULT:
		default:
			MouseType.DEFAULT.sheet = new SpriteSheet("/Mouse.png");
			break;
		}
	}
}
