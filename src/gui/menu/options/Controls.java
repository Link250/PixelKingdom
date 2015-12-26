package gui.menu.options;

import gfx.SpriteSheet;
import gui.Button;
import gui.menu.OptionScreen;
import main.Game;

public class Controls {
	private OptionScreen mainMenu;
	
	private Button  back;
	
	public Controls(OptionScreen mainMenu) {
		this.mainMenu = mainMenu;
		this.back = new Button(10, 10, 20, 20);
		this.back.gfxData(new SpriteSheet("/Buttons/back.png"), true);
	}
	
	public void tick(){
		back.tick();
		if(back.isclicked || Game.input.Esc.click()){
			this.mainMenu.resetMenu();
		}
	}
	
	public void render(){
		back.render();
		Game.font.render(Game.screen.width/Game.SCALE/2-30, 10, "Controls", 0, 0xff000000, Game.screen);
		Game.font.render(Game.screen.width/Game.SCALE/2-50, Game.screen.height/Game.SCALE/2, "COMING-SOON", 0, 0xff000000, Game.screen);
	}
}
