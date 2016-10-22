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
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0x80ff0000;
		}
	}
	
	public boolean contains(int X, int Y){
		if(X >= x && X < x+width && Y >= y && Y < y+height) return true;
		else return false;
	}
	
	public void showArea(){
		Screen.drawTileOGL(x, y, 0, back);
	}
}
