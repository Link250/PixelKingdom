package gui.menu;

import gfx.SpriteSheet;
import gui.Button;
import main.Game;
import main.Keys;

public class MainMenu implements GameMenu{
	
	public Menu subMenu = Menu.MainMenu;
	private Button SP, MP, OP, QT;
	public ServerList ServerList;
	public MapSelection MapSelect;
	public OptionScreen OptionScreen;
	private Game game;
	
	public MainMenu(Game game) {
		this.game = game;
		int bWidth = 300, bHeight = 60, bYOff = 250, bNr = 0;
		SP = new Button((Game.WIDTH)/2,bYOff + (int)(bHeight*bNr++*1.5),bWidth,bHeight);
		SP.gfxData(new SpriteSheet("/Buttons/SP.png"), true);
		MP = new Button((Game.WIDTH)/2,bYOff + (int)(bHeight*bNr++*1.5),bWidth,bHeight);
		MP.gfxData(new SpriteSheet("/Buttons/MP.png"), true);
		OP = new Button((Game.WIDTH)/2,bYOff + (int)(bHeight*bNr++*1.5),bWidth,bHeight);
		OP.gfxData(new SpriteSheet("/Buttons/OP.png"), true);
		QT = new Button((Game.WIDTH)/2,bYOff + (int)(bHeight*bNr++*1.5),bWidth,bHeight);
		QT.gfxData(new SpriteSheet("/Buttons/QT.png"), true);
	}
	
	public void render() {
		switch(subMenu) {
		case MapSelection:
			MapSelect.render();
			break;
		case ServerList:
			ServerList.render();
			break;
		case Options:
			OptionScreen.render();
			break;
		default:
		case MainMenu:
			SP.render();
			MP.render();
			OP.render();
			QT.render();
			break;
		}
	}

	public void tick() {
		switch (subMenu){
		case MapSelection :
			MapSelect.tick();
			break;
		case ServerList :
			ServerList.tick();
			break;
		case Options :
			OptionScreen.tick();
			break;
		default:
		case MainMenu :
			SP.tick();
			if(SP.isclicked){
				MapSelect = new MapSelection(game);
				subMenu = Menu.MapSelection;
			}
			MP.tick();
			if(MP.isclicked){
				ServerList = new ServerList(game);
				subMenu = Menu.ServerList;
			}
			OP.tick();
			if(OP.isclicked){
				OptionScreen = new OptionScreen(game);
				subMenu = Menu.Options;
			}
			QT.tick();
			if(QT.isclicked || Keys.MENU.click()){
				game.stop();
			}
			break;
		}
	}
}
