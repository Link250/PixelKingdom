package gui.menu.options;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import dataUtils.PArea;

import static main.MainConfig.PlrCol;

import gfx.Screen;
import gfx.SpriteSheet;
import gui.Button;
import gui.menu.OptionScreen;
import main.Game;
import main.Keys;
import main.MainConfig;
import main.MouseInput;

public class Visuals {
	private OptionScreen mainMenu;
	
	private Button  back;
	private ArrayList<GFXOption> options = new ArrayList<>();
//	private ItemField a,b,c,d;
	
	public Visuals(OptionScreen mainMenu) {
		this.mainMenu = mainMenu;
		this.back = new Button(50, 50, 60, 60);
		this.back.gfxData("/Buttons/back.png", true);
		options.add(new PlayerColorOption());
		options.add(new MapZoomOption());
		options.add(new ResolutionOption());
		options.add(new FullscreenOption());
//		a = new ItemField(100,300); a.setItem(new MatStack());
//		b = new ItemField(100,350); b.setItem(new MatStack());
//		c = new ItemField(100,400); c.setItem(new MatStack());
//		d = new ItemField(100,450); d.setItem(new MatStack());
	}
	
	public void tick(){
		back.tick();
		if(back.isclicked || Keys.MENU.click()){
			this.mainMenu.resetMenu();
		}
		int height = Screen.height/3;
		for (GFXOption gfxOption : options) {
			gfxOption.setHeight(height);
			height += Screen.height/5;
			gfxOption.tick();
		}
//		a.getItem().addStack(1); if(a.getItem().getStack()>=10)a.getItem().setStack(1);
//		b.getItem().addStack(10); if(b.getItem().getStack()>=100)b.getItem().setStack(10);
//		c.getItem().addStack(100); if(c.getItem().getStack()>=1000)c.getItem().setStack(100);
//		d.getItem().addStack(1000); if(d.getItem().getStack()>=10000)d.getItem().setStack(1000);
	}
	
	public void render(){
		back.render();
		Game.font.render(Screen.width/2, 50, "Visuals", 0, 0xff000000, Game.screen);
		
		for (GFXOption gfxOption : options) {
			gfxOption.render();
		}
//		a.render();
//		b.render();
//		c.render();
//		d.render();
	}
	
	private abstract class GFXOption{
		protected int height = 0;
		
		public abstract void tick();
		
		public abstract void render();
		
		public void setHeight(int height) {
			this.height = height;
		}
	}
	
	private class PlayerColorOption extends GFXOption{
		private SpriteSheet sliders, color, marker;

		public void tick() {
			if(MouseInput.mousel.isPressed()){
				int x = MouseInput.mouse.x;
				int y = MouseInput.mouse.y;
				if(new PArea(Screen.width/2,height-19,256,10).contains(x, y)){
					PlrCol = ((PlrCol&0xff00ffff) + ((x-Screen.width/2)<<16)) | 0xff000000;
				}
				if(new PArea(Screen.width/2,height-3,256,10).contains(x, y)){
					PlrCol = ((PlrCol&0xffff00ff) + ((x-Screen.width/2)<<8)) | 0xff000000;
				}
				if(new PArea(Screen.width/2,height+13,256,10).contains(x, y)){
					PlrCol = ((PlrCol&0xffffff00) + ((x-Screen.width/2))) | 0xff000000;
				}
			}
		}

		public void render() {
			Game.font.render(Screen.width/4, height, "Player Color", 0, 0xff000000, Game.screen);
			if(sliders!=null) {
				Screen.drawTileOGL(xPos, yPos, 0, sliders);
			}else {
				int[] pixels = new int[2];
				Game.screen.drawGUIPixelBorder(Screen.width/2-2, height-21, 260, 14, 2, 0xff404040);
				Game.screen.drawGUIPixelBorder(Screen.width/2-2, height-5, 260, 14, 2, 0xff404040);
				Game.screen.drawGUIPixelBorder(Screen.width/2-2, height+11, 260, 14, 2, 0xff404040);
				for(int c = 0; c < 256; c++){
					for(int i = 0; i < 10; i++){
						Game.screen.drawGUIPixel(Screen.width/2+c, height-19+i, 0xff000000|(c<<16));
						Game.screen.drawGUIPixel(Screen.width/2+c, height-3+i, 0xff000000|(c<<8));
						Game.screen.drawGUIPixel(Screen.width/2+c, height+13+i, 0xff000000|c);
					}
				}
			}
			if(marker!=null) {
				
			}else {
				int c = (PlrCol&0x00ff0000)>>16;
						Game.screen.drawGUIPixelArea(Screen.width/2+c-1, height-20, 3, 12, 0xff000000 + ((255-c)<<16) + ((255-c)<<8) + (255-c));
						c = (PlrCol&0x0000ff00)>>8;
						Game.screen.drawGUIPixelArea(Screen.width/2+c-1, height-4, 3, 12, 0xff000000 + ((255-c)<<16) + ((255-c)<<8) + (255-c));
						c = PlrCol&0x000000ff;
						Game.screen.drawGUIPixelArea(Screen.width/2+c-1, height+12, 3, 12, 0xff000000 + ((255-c)<<16) + ((255-c)<<8) + (255-c));
			}
			if(color!=null) {
				
			}else {
				Game.screen.drawGUIPixelBorder(Screen.width/2+270, height-15, 34, 34, 2, 0xff404040);
				Game.screen.drawGUIPixelArea(Screen.width/2+272, height-13, 30, 30, PlrCol);
			}
		}
	}
	
	private class MapZoomOption extends GFXOption{
		private Button value;
		
		public MapZoomOption() {
			value = new Button(0,0, 80, 60);
			value.TextData("x"+MainConfig.mapZoom, false);
		}
		
		public void tick() {
			value.tick();
			if(value.isclicked) {
				MainConfig.mapZoom++;
				if(MainConfig.mapZoom > 5) MainConfig.mapZoom = 1;
				value.TextData("x"+MainConfig.mapZoom, false);
			}
		}

		public void render() {
			Game.font.render(Screen.width/4, height, "Map Zoom", 0, 0xff000000, Game.screen);
			Game.sfont.render(Screen.width/4*3, height, "(needs a restart)", 0, 0xff000000, Game.screen);
			value.render();
		}

		public void setHeight(int height) {
			super.setHeight(height);
			value.SetPos(Screen.width/2, height);
		}
	}
	
	private class ResolutionOption extends GFXOption{
		private List<Point> resolutions = new ArrayList<>();
		private int currentRes = -1;
		private Button arrowLeft, arrowRight;
		
		public ResolutionOption() {
			arrowLeft = new Button(0, 0, 60, 60);
			arrowLeft.gfxData("/Buttons/ArrowRight.png", 0x10, false);
			arrowRight = new Button(0, 0, 60, 60);
			arrowRight.gfxData("/Buttons/ArrowRight.png", 0x00, false);
			resolutions.add(new Point(960,720));
			resolutions.add(new Point(1024,768));
			resolutions.add(new Point(1200,900));
			resolutions.add(new Point(1280,720));
			resolutions.add(new Point(1280,1024));
			resolutions.add(new Point(1368,768));
			resolutions.add(new Point(1440,900));
			resolutions.add(new Point(1600,900));
			resolutions.add(new Point(1600,1200));
			resolutions.add(new Point(1680,1050));
			resolutions.add(new Point(1920,1080));
			resolutions.add(new Point(1920,1200));
			resolutions.add(new Point(2560,1440));
			resolutions.add(new Point(2560,1600));
			for(int i = 0; i < resolutions.size(); i++) {
				if(MainConfig.resX == resolutions.get(i).getX() && MainConfig.resY == resolutions.get(i).getY())currentRes = i;
			}if(currentRes == -1)currentRes = 1;
		}
		
		public void tick() {
			arrowLeft.tick();
			if(arrowLeft.isclicked) {
				if(currentRes>0) {
					currentRes--;
					setResolution();
				}
			}
			arrowRight.tick();
			if(arrowRight.isclicked) {
				if(currentRes<resolutions.size()-1) {
					currentRes++;
					setResolution();
				}
			}
		}
		
		private void setResolution() {
			MainConfig.resX = resolutions.get(currentRes).x;
			MainConfig.resY = resolutions.get(currentRes).y;
		}

		public void render() {
			Game.font.render(Screen.width/2-300, height, "Resolution", 0, 0xff000000, Game.screen);
			Game.sfont.render(Screen.width/2+180, height, MainConfig.resX+"x"+MainConfig.resY, 0, 0xff000000, Game.screen);
			arrowLeft.render();
			arrowRight.render();
		}

		public void setHeight(int height) {
			super.setHeight(height);
			arrowLeft.SetPos(Screen.width/2+30, height);
			arrowRight.SetPos(Screen.width/2+330, height);
		}
	}
	
	private class FullscreenOption extends GFXOption{
		private Button fullscreen;
		
		public FullscreenOption() {
			fullscreen = new Button(0, 0, 500, 60);
			fullscreen.TextData("Fullscreen: " + (MainConfig.fullscreen ? "ON" : "OFF"), false);
		}
		
		public void tick() {
			fullscreen.tick();
			if(fullscreen.isclicked) {
				MainConfig.fullscreen = !MainConfig.fullscreen;
				fullscreen.TextData("Fullscreen: " + (MainConfig.fullscreen ? "ON" : "OFF"), false);
			}
		}

		public void render() {
			fullscreen.render();
		}

		public void setHeight(int height) {
			super.setHeight(height);
			fullscreen.SetPos(Screen.width/2, height);
		}
	}
}
