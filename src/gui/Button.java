package gui;

import gfx.Screen;
import gfx.SpriteSheet;
import main.Game;
import main.MouseInput;

public class Button {
	private int x;
	private int y;
	private int width;
	private int height;
	private String Text;
	private boolean uselimit;
	private int ToffX;
	private int ToffY;
	private SpriteSheet gfx;
	private boolean mirrorX=false, mirrorY = false;
	private SpriteSheet bg_on;
	private SpriteSheet bg_off;
	private boolean background = false;
	public boolean mouseover = false;
	public boolean isclicked = false;
	
	public Button(int x, int y, int width, int height){
		this.height = height;
		this.width = width;
		this.x = x-this.width/2;
		this.y = y-this.height/2;
	}

	public Button(int x, int y, int width, int height, boolean centeredX, boolean centeredY){
		this.height = height;
		this.width = width;
		this.x = x - (centeredX ? this.width/2 : 0);
		this.y = y - (centeredY ? this.height/2 : 0);
	}

	public void TextData(String Text, boolean limit, int ToffX, int ToffY, boolean background){
		this.Text = Text;
		this.uselimit = limit;
		this.ToffX = ToffX;
		this.ToffY = ToffY;
		this.background = background;
		if(background)constructBackground();
	}
	
	public void TextData(String Text, boolean limit, boolean background){
		this.Text = Text;
		this.uselimit = limit;
		this.ToffX = this.width/2;
		this.ToffY = this.height/2;
		this.background = background;
		if(background)constructBackground();
	}
	
	public void gfxData(String gfxPath, boolean background){
		this.gfx = new SpriteSheet(gfxPath, this.width, this.height);
		this.background = background;
		if(background)constructBackground();
	}
	
	public void gfxData(String gfxPath, boolean mirrorX, boolean mirrorY, boolean background){
		this.gfx = new SpriteSheet(gfxPath, this.width, this.height);
		this.mirrorX = mirrorX;
		this.mirrorY = mirrorY;
		this.background = background;
		if(background)constructBackground();
	}

	private void constructBackground() {
		int[] pixels = new int[width * height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				pixels[y*width+x] = ((x < 2 || y < 2 || x >= width-2 || y >= height-2)? 0xffC0C0C0 : 0xff606060);
			}
		}
		this.bg_on = new SpriteSheet(pixels, width, height, width, height);
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				pixels[y*width+x] = ((x < 2 || y < 2 || x >= width-2 || y >= height-2)? 0xffC0C0C0 : 0xffA0A0A0);
			}
		}
		this.bg_off = new SpriteSheet(pixels, width, height, width, height);
	}
	
	public void setPos(int x, int y){
		setPos(x, y, true, true);
	}
	
	public void setPos(int x, int y, boolean centeredX, boolean centeredY){
		this.x = x - (centeredX ? this.width/2 : 0);
		this.y = y - (centeredY ? this.height/2 : 0);
	}
	
	public void tick(){
		if(MouseInput.mouse.x >= x && MouseInput.mouse.x <= x+width-1 && MouseInput.mouse.y >= y && MouseInput.mouse.y <= y+height-1) mouseover = true;
		else  mouseover = false;
		if(mouseover){
			isclicked = MouseInput.mousel.click();
		}else{
			isclicked = false;
		}
	}

	public void render(){
		if(background){
			Screen.drawGUISprite(x, y, mouseover ? bg_on : bg_off);
		}
		if(gfx != null){
			if(mouseover){
				Screen.drawGUISprite(x, y, gfx, 1, mirrorX, mirrorY);
			}else{
				Screen.drawGUISprite(x, y, gfx, 0, mirrorX, mirrorY);
			}
		}
		if(uselimit){
			Game.font.render(x+ToffX, y+ToffY, Text, width-20, 0xff000000);
		}else{
			Game.font.render(x+ToffX, y+ToffY, Text, 0, 0xff000000);
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
