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
		Game.font.render(Screen.width/2, 50, "Visuals", 0, 0xff000000);
		
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
		private SpriteSheet 
		sliders = new SpriteSheet("/Menu/Options/Visuals/PlayerColor_Sliders.png"),
		preview = new SpriteSheet("/Menu/Options/Visuals/PlayerColor_Preview.png"),
		marker = new SpriteSheet("/Menu/Options/Visuals/PlayerColor_Marker.png");

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
			Game.font.render(Screen.width/2-200, height, "Player Color", 0, 0xff000000);
			Screen.drawGUISprite(Screen.width/2-2, height-21, sliders);
			int c = (PlrCol&0x00ff0000)>>16;
			Screen.drawGUISprite(Screen.width/2+c-1, height-20, marker);
			c = (PlrCol&0x0000ff00)>>8;
			Screen.drawGUISprite(Screen.width/2+c-1, height-4, marker);
			c = PlrCol&0x000000ff;
			Screen.drawGUISprite(Screen.width/2+c-1, height+12, marker);
			Screen.drawGUISprite(Screen.width/2+270, height-15, preview, 0, false, false, PlrCol);
		}
	}
	
	private class MapZoomOption extends GFXOption{
		private Button value;
		
		public MapZoomOption() {
			value = new Button(0,0, 80, 60);
			value.TextData("x"+MainConfig.mapZoom, false, true);
		}
		
		public void tick() {
			value.tick();
			if(value.isclicked) {
				MainConfig.mapZoom++;
				if(MainConfig.mapZoom > 5) MainConfig.mapZoom = 1;
				value.TextData("x"+MainConfig.mapZoom, false, true);
			}
		}

		public void render() {
			Game.font.render(Screen.width/2-200, height, "Map Zoom", 0, 0xff000000);
			value.render();
		}

		public void setHeight(int height) {
			super.setHeight(height);
			value.setPos(Screen.width/2+180, height);
		}
	}
	
	private class ResolutionOption extends GFXOption{
		private List<Point> resolutions = new ArrayList<>();
		private int currentRes = -1;
		private Button arrowLeft, arrowRight;
		
		public ResolutionOption() {
			arrowLeft = new Button(0, 0, 60, 60);
			arrowLeft.gfxData("/Buttons/ArrowRight.png", true, false, false);
			arrowRight = new Button(0, 0, 60, 60);
			arrowRight.gfxData("/Buttons/ArrowRight.png", false);
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
			Game.font.render(Screen.width/2-200, height, "Resolution", 0, 0xff000000);
			Game.sfont.render(Screen.width/2+180, height, MainConfig.resX+"x"+MainConfig.resY, 0, 0xff000000);
			arrowLeft.render();
			arrowRight.render();
		}

		public void setHeight(int height) {
			super.setHeight(height);
			arrowLeft.setPos(Screen.width/2+30, height);
			arrowRight.setPos(Screen.width/2+330, height);
		}
	}
	
	private class FullscreenOption extends GFXOption{
		private Button fullscreen;
		
		public FullscreenOption() {
			fullscreen = new Button(0, 0, 500, 60);
			fullscreen.TextData("Fullscreen: " + (MainConfig.fullscreen ? "ON" : "OFF"), false, true);
		}
		
		public void tick() {
			fullscreen.tick();
			if(fullscreen.isclicked) {
				MainConfig.fullscreen = !MainConfig.fullscreen;
				fullscreen.TextData("Fullscreen: " + (MainConfig.fullscreen ? "ON" : "OFF"), false, true);
			}
		}

		public void render() {
			fullscreen.render();
		}

		public void setHeight(int height) {
			super.setHeight(height);
			fullscreen.setPos(Screen.width/2, height);
		}
	}
}
