package gui.menu;

import gfx.Screen;
import gui.Button;
import gui.TextField;
import main.Game;
import main.Keys;
import map.Map;

public class NewMapScreen implements GameMenu {
	
	private Button back, create;
	private TextField mapName;
	
	public NewMapScreen() {
		back = new Button(50, 50, 60, 60);
		back.gfxData("/Buttons/back.png", true);
		create = new Button(Screen.width/2, Screen.height/2+100, 300, 60);
		create.TextData("Create", false, true);
		mapName = new TextField(1, 1, 500, 60, true, true, Game.font);
		mapName.setPos(Screen.width/2, Screen.height/2);
		mapName.setFocus(true);
	}
	
	public void tick() {
		back.tick();
		create.tick();
		if(back.isclicked || Keys.MENU.click() || create.isclicked){
			if(create.isclicked) {
				Map.newMap(MapSelection.FILE_DIR,mapName.getText());
			}
			Game.menu = new MapSelection();
			mapName.setFocus(false);
		}
		mapName.tick();
	}

	public void render() {
		back.render();
		Game.font.render(Screen.width/2, Screen.height/2-80, "Map Name", 0, 0xff000000);
		mapName.render();
		create.render();
	}

	public void refreshGUI() {
		create.setPos(Screen.width/2, Screen.height/2+100);
		mapName.setPos(Screen.width/2, Screen.height/2);
	}
	
}
