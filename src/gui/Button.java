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
	private SpriteSheet bg_on = null;
	private SpriteSheet bg_off = null;
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
	
	public void gfxData(String gfxPath, boolean background){
		this.gfx = new SpriteSheet(gfxPath, this.width, this.height);
		this.background = background;
		if(background)constructBackground();
	}
	
	public void gfxData(String gfxPath, int mirrorXY, boolean background){
		this.gfx = new SpriteSheet(gfxPath, this.width, this.height);
		this.mirrorXY = mirrorXY;
		this.background = background;
		if(background)constructBackground();
	}

	private void constructBackground() {
		this.bg_on = new SpriteSheet();
		this.bg_off = new SpriteSheet();
		int[] pixels = new int[width * height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				pixels[y*width+x] = ((x < 2 || y < 2 || x >= width-2 || y >= height-2)? 0xffC0C0C0 : 0xff606060);
			}
		}
		this.bg_on.setPixels(pixels, width, height, width, height);
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				pixels[y*width+x] = ((x < 2 || y < 2 || x >= width-2 || y >= height-2)? 0xffC0C0C0 : 0xffA0A0A0);
			}
		}
		this.bg_off.setPixels(pixels, width, height, width, height);
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
			Game.screen.drawTileOGL(x, y, 0, mouseover ? bg_on : bg_off);
//			for(int i = 0; i < height; i++){
//				for(int j = 0; j < width; j++){
//					if(mouseover){
//						if(i==0 || i==height-1 || j==0 || j==width-1)Game.screen.drawGUIPixel(x+j, y+i, 0xffC0C0C0);
//						else Game.screen.drawGUIPixel(x+j, y+i, 0xff606060);
//					}
//					else{
//						if(i==0 || i==height-1 || j==0 || j==width-1)Game.screen.drawGUIPixel(x+j, y+i, 0xffC0C0C0);
//						else Game.screen.drawGUIPixel(x+j, y+i, 0xffA0A0A0);
//					}
//				}
//			}
		}
		if(gfx != null){
//			gfx.tileWidth=width;
//			gfx.tileHeight=height;
//debug			System.out.println(gfx.tileWidth);
			if(mouseover){
				Game.screen.drawTileOGL(x, y, 1, gfx);
//				Game.screen.drawGUITile(x, y, 1, mirrorXY, gfx, 0);
			}else{
				Game.screen.drawTileOGL(x, y, 0, gfx);
//				Game.screen.drawGUITile(x, y, 0, mirrorXY, gfx, 0);
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
