package gui.menu;

import gfx.Screen;
import gfx.SpriteSheet;
import gui.Button;
import main.Game;
import main.Keys;

public class MainMenu implements GameMenu{
	
	private Button SP, MP, OP, QT;
	private SpriteSheet logo;
	
	public MainMenu() {
		int bWidth = 300, bHeight = 60, bYOff = 250, bNr = 0;
		SP = new Button((Game.WIDTH)/2,bYOff + (int)(bHeight*bNr++*1.5),bWidth,bHeight);
		SP.gfxData("/Buttons/SP.png", true);
		MP = new Button((Game.WIDTH)/2,bYOff + (int)(bHeight*bNr++*1.5),bWidth,bHeight);
		MP.gfxData("/Buttons/MP.png", true);
		OP = new Button((Game.WIDTH)/2,bYOff + (int)(bHeight*bNr++*1.5),bWidth,bHeight);
		OP.gfxData("/Buttons/OP.png", true);
		QT = new Button((Game.WIDTH)/2,bYOff + (int)(bHeight*bNr++*1.5),bWidth,bHeight);
		QT.gfxData("/Buttons/QT.png", true);
		logo = new SpriteSheet("/Logo.png");
	}
	
	public void render() {
		Screen.drawGUISprite((Game.WIDTH-logo.getWidth())/2, 50, logo);
		SP.render();
		MP.render();
		OP.render();
		QT.render();
	}

	public void tick() {
		SP.tick();
		if(SP.isclicked){
			Game.menu = new MapSelection();
		}
		MP.tick();
		if(MP.isclicked){
			Game.menu = new ServerList();
		}
		OP.tick();
		if(OP.isclicked){
			Game.menu = new OptionScreen();
		}
		QT.tick();
		if(QT.isclicked || Keys.MENU.click()){
			Game.stop();
		}
	}
}
