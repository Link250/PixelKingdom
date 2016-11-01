package gui.menu.options;

import java.util.ArrayList;
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
		options.add(new FullscreenOption());
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
	}
	
	public void render(){
		back.render();
		Game.font.render(Screen.width/2, 50, "Visuals", 0, 0xff000000);
		
		for (GFXOption gfxOption : options) {
			gfxOption.render();
		}
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
