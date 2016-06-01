package gui.menu;

import gfx.SpriteSheet;
import gui.Button;
import gui.menu.options.Controls;
import gui.menu.options.Visuals;
import main.Game;
import main.Keys;

public class OptionScreen implements GameMenu{
	
	public static enum SubMenu{
		MAIN,VISUALS,CONTROLS;
	}
	
	private Button visualsButton, controlsButton;
	private Button back;
	
//	private Game game;
	private Visuals visuals;
	private Controls controls;
	
	private SubMenu subMenu = SubMenu.MAIN;
	
	public OptionScreen(Game game) {
//		this.game = game;
		this.visuals = new Visuals(this);
		this.controls = new Controls(this);
		
		back = new Button(10, 10, 20, 20);
		back.gfxData(new SpriteSheet("/Buttons/back.png"), true);
		visualsButton = new Button(Game.WIDTH/Game.SCALE/2-50,110,100,20);
		visualsButton.TextData("Visuals", false, 0, 0);
		controlsButton = new Button(Game.WIDTH/Game.SCALE/2-50,140,100,20);
		controlsButton.TextData("Controls", false, 0, 0);
	}
	
	public void resetMenu() {
		this.subMenu = SubMenu.MAIN;
	}
	
	public void tick(){
		switch(this.subMenu) {
		case CONTROLS:
			this.controls.tick();
			break;
		case VISUALS:
			this.visuals.tick();
			break;
		case MAIN:
		default:
			this.back.tick();
			this.visualsButton.tick();
			this.controlsButton.tick();
			if(back.isclicked || Keys.MENU.click()){
				Game.menu.subMenu=Menu.MainMenu;
			}
			if(visualsButton.isclicked) {
				this.subMenu = SubMenu.VISUALS;
			}
			if(controlsButton.isclicked) {
				this.subMenu = SubMenu.CONTROLS;
			}
			break;
		}
	}
	
	public void render(){
		switch(this.subMenu) {
		case CONTROLS:
			this.controls.render();
			break;
		case VISUALS:
			this.visuals.render();
			break;
		case MAIN:
		default:
			Game.font.render(Game.screen.width/Game.SCALE/2-30, 10, "Options", 0, 0xff000000, Game.screen);
			back.render();
			this.visualsButton.render();
			this.controlsButton.render();
			break;
		}
	}
}
