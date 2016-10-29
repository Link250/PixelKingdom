package gui.menu;

import gfx.Screen;
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
		
		int bWidth = 300, bHeight = 60, bYOff = 250, bNr = 0;
		back = new Button(50, 50, 60, 60);
		back.gfxData("/Buttons/back.png", true);
		visualsButton = new Button(Game.WIDTH/2,bYOff + (int)(bHeight*bNr++*1.5),bWidth,bHeight);
		visualsButton.TextData("Visuals", false, true);
		controlsButton = new Button(Game.WIDTH/2,bYOff + (int)(bHeight*bNr++*1.5),bWidth,bHeight);
		controlsButton.TextData("Controls", false, true);
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
			Game.font.render(Screen.width/2, 50, "Options", 0, 0xff000000);
			back.render();
			this.visualsButton.render();
			this.controlsButton.render();
			break;
		}
	}
}
