package Main;

import gfx.Screen;

public class PArea {
	
	public int x,y,width,height;
	
	public PArea(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public void setBounds(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public boolean contains(int X, int Y){
		if(X >= x & X < x+width & Y >= y & Y < y+height) return true;
		else return false;
	}
	
	public void showArea(Screen s){
		for(int X = 0; X < width; X++){
			for(int Y = 0; Y < height; Y++){
				s.renderPixelScaled(X+x+s.xOffset, Y+y+s.yOffset, 0x80ff0000);
			}
		}
	}
}
