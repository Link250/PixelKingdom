package gui.menu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import gfx.Screen;
import gui.Button;
import gui.TextField;
import main.Game;
import main.Keys;

public class NewServerScreen implements GameMenu {
	private final String PATH = Game.GAME_PATH+"Server.list";

	private Button back, create;
	private TextField serverName, serverIP;
	
	public NewServerScreen() {
		back = new Button(50, 50, 60, 60);
		back.gfxData("/Buttons/back.png", true);
		create = new Button(Screen.width/2, Screen.height/2+100, 300, 60);
		create.TextData("Create", false, true);
		serverName = new TextField(1, 1, 500, 60, true, true, Game.font);
		serverName.setPos(Screen.width/2, Screen.height/2-180);
		serverIP = new TextField(1, 1, 500, 60, true, true, Game.font);
		serverIP.setPos(Screen.width/2, Screen.height/2);
	}
	
	public void tick() {
		back.tick();
		if(back.isclicked || Keys.MENU.click()){
			Game.menu = new ServerList();
		}
		create.tick();
		if(create.isclicked) {
			addServer(serverName.getText(), serverIP.getText());
			Game.menu = new ServerList();
		}
		serverName.tick();
		serverIP.tick();
	}

	public void render() {
		back.render();
		Game.font.render(Screen.width/2, Screen.height/2-260, "Server Name", 0, 0xff000000);
		serverName.render();
		Game.font.render(Screen.width/2, Screen.height/2-80, "Server Adresse", 0, 0xff000000);
		serverIP.render();
		create.render();
	}
	
	public void addServer(String name, String adress) {
		String DATA_PATH = Game.GAME_PATH+"serverData"+File.separator;
        File dir = new File(DATA_PATH);
        if(!dir.isDirectory()) {
        	dir.mkdirs();
        }
        new File(DATA_PATH+name).mkdir();

		try {
			List<String> serverList;
			File file = new File(PATH);
			if(!file.exists())file.createNewFile();
			
			serverList = Files.readAllLines(Paths.get(PATH));
		
			serverList.add(name);
			serverList.add(adress);
		
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter out = new BufferedWriter(fw);
			for(int i = 0; i < serverList.size(); i++) {
				if(i!=0)out.newLine();
				out.write(serverList.get(i));out.flush();
			}out.close();
		} catch (IOException e) {e.printStackTrace();}
	}
}
