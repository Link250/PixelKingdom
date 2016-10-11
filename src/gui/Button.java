package gui;

import gfx.SpriteSheet;
import main.Game;

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
	private int mirrorXY=0;
	private boolean background = true;
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

	public void TextData(String Text, boolean limit, int ToffX, int ToffY){
		this.Text = Text;
		this.uselimit = limit;
		this.ToffX = ToffX;
		this.ToffY = ToffY;
	}
	
	public void TextData(String Text, boolean limit){
		this.Text = Text;
		this.uselimit = limit;
		this.ToffX = this.width/2;
		this.ToffY = this.height/2;
	}
	
	public void gfxData(SpriteSheet gfx, boolean background){
		this.gfx = gfx;
		this.background = background;
	}
	
	public void gfxData(SpriteSheet gfx, int mirrorXY, boolean background){
		this.gfx = gfx;
		this.mirrorXY = mirrorXY;
		this.background = background;
	}

	public void SetPos(int x, int y){
		SetPos(x, y, true, true);
	}
	
	public void SetPos(int x, int y, boolean centeredX, boolean centeredY){
		this.x = x - (centeredX ? this.width/2 : 0);
		this.y = y - (centeredY ? this.height/2 : 0);
	}
	
	public void tick(){
//		System.out.println(input.mouse.x + " " + input.mouse.y);
		if(Game.input.mouse.x >= x && Game.input.mouse.x <= x+width-1 && Game.input.mouse.y >= y && Game.input.mouse.y <= y+height-1) mouseover = true;
		else  mouseover = false;
		if(mouseover){
			isclicked = Game.input.mousel.click();
		}else{
			isclicked = false;
		}
	}

	public void render(){
		if(background){
			for(int i = 0; i < height; i++){
				for(int j = 0; j < width; j++){
					if(mouseover){
						if(i==0 || i==height-1 || j==0 || j==width-1)Game.screen.drawGUIPixel(x+j, y+i, 0xffC0C0C0);
						else Game.screen.drawGUIPixel(x+j, y+i, 0xff606060);
					}
					else{
						if(i==0 || i==height-1 || j==0 || j==width-1)Game.screen.drawGUIPixel(x+j, y+i, 0xffC0C0C0);
						else Game.screen.drawGUIPixel(x+j, y+i, 0xffA0A0A0);
					}
				}
			}
		}
		if(gfx != null){
			gfx.tileWidth=width;
			gfx.tileHeight=height;
//debug			System.out.println(gfx.tileWidth);
			if(mouseover){
				Game.screen.drawGUITile(x, y, 1, mirrorXY, gfx, 0);
			}else{
				Game.screen.drawGUITile(x, y, 0, mirrorXY, gfx, 0);
			}
		}
		if(uselimit){
			Game.font.render(x+ToffX, y+ToffY, Text, width-20, 0xff000000, Game.screen);
		}else{
			Game.font.render(x+ToffX, y+ToffY, Text, 0, 0xff000000, Game.screen);
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
