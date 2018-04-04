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
		int bWidth = 300, bHeight = 60;
		SP = new Button(1, 1, bWidth, bHeight);
		SP.gfxData("/Buttons/SP.png", true);
		MP = new Button(1, 1, bWidth, bHeight);
		MP.gfxData("/Buttons/MP.png", true);
		OP = new Button(1, 1, bWidth, bHeight);
		OP.gfxData("/Buttons/OP.png", true);
		QT = new Button(1, 1, bWidth, bHeight);
		QT.gfxData("/Buttons/QT.png", true);
		refreshGUI();
		logo = new SpriteSheet("/Menu/Logo.png");
	}
	
	public void render() {
		Screen.drawGUISprite((Screen.width-logo.getWidth())/2, 50, logo);
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

	public void refreshGUI() {
		int bYOff = 250, bNr = 0, bHeight = 60;
		SP.setPos((Screen.width)/2,bYOff + (int)(bHeight*bNr++*1.5));
		MP.setPos((Screen.width)/2,bYOff + (int)(bHeight*bNr++*1.5));
		OP.setPos((Screen.width)/2,bYOff + (int)(bHeight*bNr++*1.5));
		QT.setPos((Screen.width)/2,bYOff + (int)(bHeight*bNr++*1.5));
	}
}
