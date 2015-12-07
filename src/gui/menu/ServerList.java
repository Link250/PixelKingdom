package gui.menu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import gfx.SpriteSheet;
import gui.Button;
import gui.NewServerWindow;
import main.Game;
import multiplayer.client.Client;
import multiplayer.server.Server;

public class ServerList implements GameMenu{
	
	private final String PATH = Game.GAME_PATH+"Server.list";
	private final String DATA_PATH = Game.GAME_PATH+"serverData"+File.separator;
	
	private ArrayList<Button> ButtonList;
	private ArrayList<String> adress;
	private ArrayList<String> name;
	private Game game;
	private Button  back,add,del,join,scrollUP,scrollDOWN,start;
	private int selected = 0;
	private NewServerWindow newServerWindow;
	
	public ServerList(Game game) {
		this.game = game;
		back = new Button(10, 10, 20, 20);
		back.gfxData(new SpriteSheet("/Buttons/back.png"), true);
		add = new Button(Game.screen.width/Game.SCALE-30, 10, 20, 20);
		add.gfxData(new SpriteSheet("/Buttons/new.png"), true);
		start = new Button(Game.screen.width/Game.SCALE-30, Game.screen.height/Game.SCALE-30, 20, 20);
		start.gfxData(new SpriteSheet("/Buttons/new.png"), true);
		del = new Button(Game.screen.width/Game.SCALE/2-80, 100, 20, 20);
		del.gfxData(new SpriteSheet("/Buttons/delete.png"), true);
		join = new Button(Game.screen.width/Game.SCALE/2+60, 100, 20, 20);
		join.gfxData(new SpriteSheet("/Buttons/play.png"), true);
		scrollUP = new Button(Game.screen.width/Game.SCALE/2-10, 40, 20, 20);
		scrollUP.gfxData(new SpriteSheet("/Buttons/ArrowDown.png"), 0x01, false);
		scrollDOWN = new Button(Game.screen.width/Game.SCALE/2-10, 160, 20, 20);
		scrollDOWN.gfxData(new SpriteSheet("/Buttons/ArrowDown.png"), false);
		LoadServers(false);
	}
	
	public void LoadServers(boolean save){
		
		if(save)save();
		
		ButtonList = new ArrayList<Button>();
		name = new ArrayList<String>();
		adress = new ArrayList<String>();
		
		File file = new File(PATH);
		if(!file.exists()){
			try {file.createNewFile();} catch (IOException e) {e.printStackTrace();}
		}
		try {
			List<String> serverlist = Files.readAllLines(Paths.get(PATH));
			boolean turn = true;
			for(String s : serverlist){
				if(turn) {
					name.add(s);
					ButtonList.add(new Button(Game.WIDTH/Game.SCALE/2-50, 1, 100, 20));
					ButtonList.get(ButtonList.size()-1).TextData( s, true, 0, 0);
				}else{
					adress.add(s);
				}
				turn = !turn;
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void save(){
		File file = new File(PATH);
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter out = new BufferedWriter(fw);
			for(int i = 0; i < name.size(); i++) {
				if(i!=0)out.newLine();
				out.write(name.get(i));out.newLine();
				out.write(adress.get(i));out.flush();
			}out.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void tick(){
		for(Button button : ButtonList){
			button.tick();
		}
		if(back.isclicked){
			if(this.newServerWindow != null)this.newServerWindow.dispose();
			Game.mainMenu.menu=MainMenu.Menu.MainMenu;
		}
		if(add.isclicked){
			if(newServerWindow != null && newServerWindow.isDisplayable()) {
				newServerWindow.requestFocus();
			}else{
				newServerWindow = new NewServerWindow(game, this);
			}
		}
		if(del.isclicked&ButtonList.size()!=0){
			String DIR = this.DATA_PATH + name.remove(selected);
			try {
				for(String Fdel : new File(DIR + File.separator).list()){
					new File(DIR + File.separator + Fdel).delete();
				}
			}catch(NullPointerException e) {e.printStackTrace();}
			new File(DIR).delete();
			adress.remove(selected);
			LoadServers(true);
		}
		if(start.isclicked && game.server==null){
			File dir = new File(Game.GAME_PATH+"maps"+File.separator+"Server"+File.separator);
			if(!dir.isDirectory()) {dir.mkdirs();}
			game.server=new Server(Game.GAME_PATH+"maps"+File.separator+"Server");
			Thread t = new Thread(game.server);
			t.setName("Server");
			t.start();
		}
		if(join.isclicked){
			try {
				game.client = new Client(game, adress.get(selected), this.DATA_PATH + name.get(selected));
				Game.gamemode = Game.GameMode.MultiPlayer;
			} catch (IOException e) {
				Game.logWarning("Could not connect to Server.");
				game.client = null;
			}
		}
		back.tick();
		add.tick();
		del.tick();
		if(game.server==null)start.tick();
		if(ButtonList.size()!=0)join.tick();
		if(selected > 0)scrollUP.tick();
		if(scrollUP.isclicked && selected > 0)selected--;
		if(selected < ButtonList.size()-1)scrollDOWN.tick();
		if(scrollDOWN.isclicked && selected < ButtonList.size()-1)selected++;
		if(selected > ButtonList.size()-1 && ButtonList.size()!=0)selected = ButtonList.size()-1;
	}
	
	public void render(){
		for(int i = -1; i < 2; i++){
			try{
				ButtonList.get(selected+i).SetPos(Game.WIDTH/Game.SCALE/2-50, 100+(i*30));
				ButtonList.get(selected+i).render();
			}catch(ArrayIndexOutOfBoundsException e){}catch(IndexOutOfBoundsException e){}
		}
		back.render();
		add.render();
		if(game.server==null)start.render();
		else Game.font.render(Game.screen.width/Game.SCALE/2-65, Game.screen.height/Game.SCALE-20, "ServerRunning", 0, 0xff000000, Game.screen);
		if(ButtonList.size()!=0)del.render();
		if(ButtonList.size()!=0)join.render();
		if(selected > 0)scrollUP.render();
		if(selected < ButtonList.size()-1)scrollDOWN.render();
		Game.font.render(Game.screen.width/Game.SCALE/2-50, 10, "Multiplayer", 0, 0xff000000, Game.screen);
	}
	
	public void addServer(String name, String adress) {
        this.name.add(name);
        this.adress.add(adress);
        File dir = new File(this.DATA_PATH);
        if(!dir.isDirectory()) {
        	dir.mkdirs();
        }
        new File(this.DATA_PATH+name).mkdir();
		this.LoadServers(true);
	}
}
