package main;

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
import main.Game;
import multiplayer.client.Client;
import multiplayer.server.Server;

public class ServerList {
	
	private final String PATH = Game.GAME_PATH+"Server.list";
	
	private ArrayList<Button> ButtonList;
	private ArrayList<String> adress;
	private ArrayList<String> name;
	private Game game;
	private Button  back,add,del,join,scrollUP,scrollDOWN,start;
	private int selected = 0;
	
	public ServerList(Game game) {
		this.game = game;
		back = new Button(10, 10, 20, 20, game.screen, game.input);
		back.gfxData(new SpriteSheet("/Buttons/back.png"), true);
		add = new Button(game.screen.width/Game.SCALE-30, 10, 20, 20, game.screen, game.input);
		add.gfxData(new SpriteSheet("/Buttons/new.png"), true);
		start = new Button(game.screen.width/Game.SCALE-30, game.screen.height/Game.SCALE-30, 20, 20, game.screen, game.input);
		start.gfxData(new SpriteSheet("/Buttons/new.png"), true);
		del = new Button(game.screen.width/Game.SCALE/2-80, 100, 20, 20, game.screen, game.input);
		del.gfxData(new SpriteSheet("/Buttons/delete.png"), true);
		join = new Button(game.screen.width/Game.SCALE/2+60, 100, 20, 20, game.screen, game.input);
		join.gfxData(new SpriteSheet("/Buttons/play.png"), true);
		scrollUP = new Button(game.screen.width/Game.SCALE/2-10, 40, 20, 20, game.screen, game.input);
		scrollUP.gfxData(new SpriteSheet("/Buttons/ArrowDown.png"), 0x01, false);
		scrollDOWN = new Button(game.screen.width/Game.SCALE/2-10, 160, 20, 20, game.screen, game.input);
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
					ButtonList.add(new Button(Game.WIDTH/Game.SCALE/2-50, 1, 100, 20, game.screen, game.input));
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
		if(join.isclicked&ButtonList.size()!=0){
			//Game.gamemode = 1;
		}
		if(back.isclicked){
			Game.menu=0;
		}
		if(add.isclicked){
	        name.add("Server"+name.size());
	        adress.add("46.5.116.45");
			LoadServers(true);
		}
		if(del.isclicked&ButtonList.size()!=0){
			name.remove(selected);
			adress.remove(selected);
			LoadServers(true);
		}
		if(start.isclicked && game.server==null){
			game.server=new Server(game,Game.GAME_PATH+"maps\\Server");
			Thread t = new Thread(game.server);
			t.setName("Server");
			t.start();
		}
		if(join.isclicked){
			game.client = new Client(game, adress.get(selected), Game.GAME_PATH+"serverPlayer"+ File.separator + name.get(selected));
			Game.gamemode = 2;
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
		else Game.font.render(game.screen.width/Game.SCALE/2-65, game.screen.height/Game.SCALE-20, "ServerRunning", 0, 0xff000000, game.screen);
		if(ButtonList.size()!=0)del.render();
		if(ButtonList.size()!=0)join.render();
		if(selected > 0)scrollUP.render();
		if(selected < ButtonList.size()-1)scrollDOWN.render();
		Game.font.render(game.screen.width/Game.SCALE/2-50, 10, "Multiplayer", 0, 0xff000000, game.screen);
	}
}
