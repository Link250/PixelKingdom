package gfx;

import Main.Game;

public class PxlFont {
	private String chars;
	private int[] size;
	private int[] sizel;
	private SpriteSheet sheet;
	
	public PxlFont(SpriteSheet sheet, String chars, int sizex, int sizey){
		this.sheet = sheet;
		this.chars = chars;
		sheet.tileWidth = sizex;
		sheet.tileHeight = sizey;
		size = new int[chars.length()];
		sizel = new int[chars.length()];
		for(int i = 0; i < this.chars.length(); i++){
			int xs,xe;
			int pixelfound = 0;
			for(xs = 0; xs < sheet.tileWidth && (pixelfound == 0); xs++){
				pixelfound = 0;
				for(int y = 0; y < sheet.tileHeight; y++){
					if((sheet.pixels[((i%(sheet.width/sheet.tileWidth))*sheet.tileWidth+xs) + ((i/(sheet.width/sheet.tileWidth))*sheet.tileHeight+y)*sheet.width] & 0xffffff)!= 0x000000){
						pixelfound++;
					}
				}
			}
			for(xe = xs; xe < sheet.tileWidth && (pixelfound > 0); xe++){
				pixelfound = 0;
				for(int y = 0; y < sheet.tileHeight; y++){
					if((sheet.pixels[((i%(sheet.width/sheet.tileWidth))*sheet.tileWidth+xe) + ((i/(sheet.width/sheet.tileWidth))*sheet.tileHeight+y)*sheet.width] & 0xffffff)!= 0x000000){
						pixelfound++;
					}
				}
			}
			size[i] = (xe-xs)/Game.SCALE;
			sizel[i] = (xs)/Game.SCALE;
		}
	}
	
	public void render(int x, int y, String msg, int limit, int color, Screen screen){
		if(msg == null) return;
		if(limit!=0)limit+=x;
		for(int i = 0; i < msg.length(); i++){
			int charIndex = chars.indexOf(msg.charAt(i));
			if(charIndex >= 0){
				if(x < limit || limit == 0){
					screen.renderGUITile(x-sizel[charIndex], y, charIndex, 0x00, sheet, color);
					x += size[charIndex];
				}else{if(x < limit+10){
					screen.renderGUITile(x, y, chars.length()-1, 0x00, sheet, color);
				}}
			}
		}
	}
}
