package dataUtils;

import gfx.Screen;
import gfx.SpriteSheet;
import main.MouseInput;

public class PArea {
	
	public int x,y,width,height;
	private SpriteSheet back;
	private boolean resized = false;
	
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
		this.resized = true;
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean isClicked(MouseInput.Mouse mouse){
		return contains(mouse.x, mouse.y) && mouse.click();
	}
	
	public boolean containsMouse(MouseInput.Mouse mouse){
		return contains(mouse.x, mouse.y);
	}
	
	public boolean contains(int X, int Y){
		if(X >= x && X < x+width && Y >= y && Y < y+height) return true;
		else return false;
	}
	
	private void constructBack() {
		int[] pixels = new int[width*height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixels[x+y*width] = x <= 1 || x>=width-2 || y <= 1 || y>=height-2 ? 0xff404040 : 0xff808080;
			}
		}
		back = new SpriteSheet(pixels, width, height, width, height);
	}
	
	public void showArea(){
		if(back==null || resized)constructBack();
		Screen.drawGUISprite(x, y, back);
	}
}
