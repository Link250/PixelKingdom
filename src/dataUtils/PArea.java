package dataUtils;

import gfx.Screen;
import gfx.SpriteSheet;

public class PArea {
	
	public int x,y,width,height;
	private SpriteSheet back;
	
	public PArea(int x, int y, int w, int h){
		setBounds(x, y, w, h);
	}
	
	public void setBounds(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		setSize(w, h);
	}
	
	public void setSize(int w, int h){
		this.width = w;
		this.height = h;
		int[] pixels = new int[w*h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				pixels[x+y*w] = x <= 1 || x>=w-2 || y <= 1 || y>=h-2 ? 0xff404040 : 0xff808080;
			}
		}
		back = new SpriteSheet(pixels, w, h, w, h);
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean contains(int X, int Y){
		if(X >= x && X < x+width && Y >= y && Y < y+height) return true;
		else return false;
	}
	
	public void showArea(){
		Screen.drawGUISprite(x, y, back);
	}
}
