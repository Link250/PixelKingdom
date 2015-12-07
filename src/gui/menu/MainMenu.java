package gui.menu;

import gfx.SpriteSheet;
import gui.Button;
import main.Game;

public class MainMenu implements GameMenu{
	
	public static enum Menu{
		MainMenu,MapSelection,ServerList,ServerConnection,Options
	}
	
	public Menu menu = Menu.MainMenu;
	private Button SP, MP, OP, QT;
	public ServerList ServerList;
	public MapSelection MapSelect;
	public OptionScreen OptionScreen;
	private Game game;
	
	public MainMenu(Game game) {
		this.game = game;
		SP = new Button(Game.WIDTH/Game.SCALE/2-50,110,100,20);
		SP.gfxData(new SpriteSheet("/Buttons/SP.png"), true);
		MP = new Button(Game.WIDTH/Game.SCALE/2-50,140,100,20);
		MP.gfxData(new SpriteSheet("/Buttons/MP.png"), true);
		OP = new Button(Game.WIDTH/Game.SCALE/2-50,170,100,20);
		OP.gfxData(new SpriteSheet("/Buttons/OP.png"), true);
		QT = new Button(Game.WIDTH/Game.SCALE/2-50,200,100,20);
		QT.gfxData(new SpriteSheet("/Buttons/QT.png"), true);
	}
	
	public void render() {
		switch(menu) {
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
		switch (menu){
		case MainMenu :
			SP.tick();
			if(SP.isclicked){
				MapSelect = new MapSelection(game);
				menu = Menu.MapSelection;
			}
			MP.tick();
			if(MP.isclicked){
				ServerList = new ServerList(game);
				menu = Menu.ServerList;
			}
			OP.tick();
			if(OP.isclicked){
				OptionScreen = new OptionScreen(game);
				menu = Menu.Options;
			}
			QT.tick();
			if(QT.isclicked){
				game.stop();
			}
			break;
		case MapSelection :
			MapSelect.tick();
			break;
		case ServerList :
			ServerList.tick();
			break;
		case Options :
			OptionScreen.tick();
			break;
		default:break;
		}
	}
}
