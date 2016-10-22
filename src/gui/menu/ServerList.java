package gui.menu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import gfx.Screen;
import gui.Button;
import gui.NewServerWindow;
import main.Game;
import main.Keys;
import main.MouseInput;
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
	/** should always be uneven or else the delete and play buttons dont lign up */
	private int maxButtonsOnScreen = 1;

	public ServerList(Game game) {
		this.game = game;
		int buttonSize = 60;
		maxButtonsOnScreen = (int) ((Screen.height-buttonSize*3)/(buttonSize*1.5));
		//makes it uneven
		maxButtonsOnScreen -= maxButtonsOnScreen%2==0 ? 1 : 0;
		back = new Button(50, 50, buttonSize, buttonSize);
		back.gfxData("/Buttons/back.png", true);
		add = new Button(Screen.width-50, 50, buttonSize, buttonSize);
		add.gfxData("/Buttons/add_server.png", true);
		start = new Button(Screen.width-50, Screen.height-50, buttonSize, buttonSize);
		start.gfxData("/Buttons/new_server.png", true);
		del = new Button(Screen.width/2-240, Screen.height/2, buttonSize, buttonSize);
		del.gfxData("/Buttons/delete.png", true);
		join = new Button(Screen.width/2+240, Screen.height/2, buttonSize, buttonSize);
		join.gfxData("/Buttons/play.png", true);
		scrollUP = new Button(Screen.width/2, Screen.height/2-(maxButtonsOnScreen/2+1)*buttonSize*3/2, buttonSize, buttonSize);
		scrollUP.gfxData("/Buttons/ArrowDown.png", false, true, false);
		scrollDOWN = new Button(Screen.width/2, Screen.height/2+(maxButtonsOnScreen/2+1)*buttonSize*3/2, buttonSize, buttonSize);
		scrollDOWN.gfxData("/Buttons/ArrowDown.png", false);
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
					ButtonList.add(new Button(0, 0, 300, 60));
					ButtonList.get(ButtonList.size()-1).TextData( s, true);
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
		if(back.isclicked || Keys.MENU.click()){
			if(this.newServerWindow != null)this.newServerWindow.dispose();
			Game.menu.subMenu=Menu.MainMenu;
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
		int scroll = MouseInput.mouse.getScroll();
		if(game.server==null)start.tick();
		if(ButtonList.size()!=0)join.tick();
		if(selected > 0)scrollUP.tick();
		if((scrollUP.isclicked || scroll<0) && selected > 0)selected--;
		if(selected < ButtonList.size()-1)scrollDOWN.tick();
		if((scrollDOWN.isclicked || scroll>0) && selected < ButtonList.size()-1)selected++;
		if(selected > ButtonList.size()-1 && ButtonList.size()!=0)selected = ButtonList.size()-1;
	}
	
	public void render(){
		for(int i = -maxButtonsOnScreen/2; i <= maxButtonsOnScreen/2; i++){
			try{
				ButtonList.get(selected+i).SetPos(Game.WIDTH/2, Screen.height/2+(i*90));
				ButtonList.get(selected+i).render();
			}catch(ArrayIndexOutOfBoundsException e){}catch(IndexOutOfBoundsException e){}
		}
		back.render();
		add.render();
		if(game.server==null)start.render();
		else Game.font.render(Screen.width/2-65, Screen.height-20, "ServerRunning", 0, 0xff000000, Game.screen);
		if(ButtonList.size()!=0)del.render();
		if(ButtonList.size()!=0)join.render();
		if(selected > 0)scrollUP.render();
		if(selected < ButtonList.size()-1)scrollDOWN.render();
		Game.font.render(Screen.width/2, 50, "Multiplayer", 0, 0xff000000, Game.screen);
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
