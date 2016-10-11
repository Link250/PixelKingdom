package gui.menu.options;

import java.util.ArrayList;

import static main.MainConfig.PlrCol;
import gfx.SpriteSheet;
import gui.Button;
import gui.menu.OptionScreen;
import main.Game;
import main.Keys;
import main.PArea;

public class Visuals {
	private OptionScreen mainMenu;
	
	private Button  back;
	private ArrayList<GFXOption> options = new ArrayList<>();
//	private ItemField a,b,c,d;
	
	public Visuals(OptionScreen mainMenu) {
		this.mainMenu = mainMenu;
		this.back = new Button(50, 50, 60, 60);
		this.back.gfxData(new SpriteSheet("/Buttons/back.png"), true);
		options.add(new PlayerColorOption());
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
		int height = Game.screen.height/3;
		for (GFXOption gfxOption : options) {
			gfxOption.setHeight(height);
			height += Game.screen.height/5;
			gfxOption.tick();
		}
//		a.getItem().addStack(1); if(a.getItem().getStack()>=10)a.getItem().setStack(1);
//		b.getItem().addStack(10); if(b.getItem().getStack()>=100)b.getItem().setStack(10);
//		c.getItem().addStack(100); if(c.getItem().getStack()>=1000)c.getItem().setStack(100);
//		d.getItem().addStack(1000); if(d.getItem().getStack()>=10000)d.getItem().setStack(1000);
	}
	
	public void render(){
		back.render();
		Game.font.render(Game.screen.width/2, 50, "Visuals", 0, 0xff000000, Game.screen);
		
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

		public void tick() {
			if(Game.input.mousel.isPressed()){
				int x = Game.input.mouse.x;
				int y = Game.input.mouse.y;
				if(new PArea(Game.screen.width/2,height-19,256,10).contains(x, y)){
					PlrCol = ((PlrCol&0xff00ffff) + ((x-Game.screen.width/2)<<16)) | 0xff000000;
				}
				if(new PArea(Game.screen.width/2,height-3,256,10).contains(x, y)){
					PlrCol = ((PlrCol&0xffff00ff) + ((x-Game.screen.width/2)<<8)) | 0xff000000;
				}
				if(new PArea(Game.screen.width/2,height+13,256,10).contains(x, y)){
					PlrCol = ((PlrCol&0xffffff00) + ((x-Game.screen.width/2))) | 0xff000000;
				}
			}
		}

		public void render() {
			Game.font.render(Game.screen.width/4, height, "Player Color", 0, 0xff000000, Game.screen);
			Game.screen.drawGUIPixelBorder(Game.screen.width/2-2, height-21, 260, 14, 2, 0xff404040);
			Game.screen.drawGUIPixelBorder(Game.screen.width/2-2, height-5, 260, 14, 2, 0xff404040);
			Game.screen.drawGUIPixelBorder(Game.screen.width/2-2, height+11, 260, 14, 2, 0xff404040);
			for(int c = 0; c < 256; c++){
				for(int i = 0; i < 10; i++){
					Game.screen.drawGUIPixel(Game.screen.width/2+c, height-19+i, 0xff000000|(c<<16));
					Game.screen.drawGUIPixel(Game.screen.width/2+c, height-3+i, 0xff000000|(c<<8));
					Game.screen.drawGUIPixel(Game.screen.width/2+c, height+13+i, 0xff000000|c);
				}
			}
			int c = (PlrCol&0x00ff0000)>>16;
			Game.screen.drawGUIPixelArea(Game.screen.width/2+c-1, height-20, 3, 12, 0xff000000 + ((255-c)<<16) + ((255-c)<<8) + (255-c));
			c = (PlrCol&0x0000ff00)>>8;
			Game.screen.drawGUIPixelArea(Game.screen.width/2+c-1, height-4, 3, 12, 0xff000000 + ((255-c)<<16) + ((255-c)<<8) + (255-c));
			c = PlrCol&0x000000ff;
			Game.screen.drawGUIPixelArea(Game.screen.width/2+c-1, height+12, 3, 12, 0xff000000 + ((255-c)<<16) + ((255-c)<<8) + (255-c));
			
			Game.screen.drawGUIPixelBorder(Game.screen.width/2+270, height-15, 34, 34, 2, 0xff404040);
			Game.screen.drawGUIPixelArea(Game.screen.width/2+272, height-13, 30, 30, PlrCol);
		}

		public void setHeight(int height) {
			super.setHeight(height);
		}
		
	}
}
