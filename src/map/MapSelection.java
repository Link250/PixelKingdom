package map;

import gfx.SpriteSheet;
import gui.Button;
import main.Game;
import main.SinglePlayer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class MapSelection {
	 
	private static final String FILE_DIR = Game.GAME_PATH+"maps";
	
	ArrayList<Button> ButtonList;
	private Game game;
	private String[] list;
	private String[] files;
	private Button  back,genmap,del,start,scrollUP,scrollDOWN;
	private int selected = 0;
	
	public MapSelection(Game game) {
		this.game = game;
		back = new Button(10, 10, 20, 20, game.screen, game.input);
		back.gfxData(new SpriteSheet("/Buttons/back.png"), true);
		genmap = new Button(game.screen.width/Game.SCALE-30, 10, 20, 20, game.screen, game.input);
		genmap.gfxData(new SpriteSheet("/Buttons/new.png"), true);
		del = new Button(game.screen.width/Game.SCALE/2-80, 100, 20, 20, game.screen, game.input);
		del.gfxData(new SpriteSheet("/Buttons/delete.png"), true);
		start = new Button(game.screen.width/Game.SCALE/2+60, 100, 20, 20, game.screen, game.input);
		start.gfxData(new SpriteSheet("/Buttons/play.png"), true);
		scrollUP = new Button(game.screen.width/Game.SCALE/2-10, 40, 20, 20, game.screen, game.input);
		scrollUP.gfxData(new SpriteSheet("/Buttons/ArrowDown.png"), 0x01, false);
		scrollDOWN = new Button(game.screen.width/Game.SCALE/2-10, 160, 20, 20, game.screen, game.input);
		scrollDOWN.gfxData(new SpriteSheet("/Buttons/ArrowDown.png"), false);
		LoadFiles();
	}
	
	public void LoadFiles(){
		
		ButtonList = new ArrayList<Button>();
		
		File dir = new File(FILE_DIR);
 
		if(dir.isDirectory()==false){
			dir.mkdirs();
		}
 
		list = dir.list();
		files = new String[list.length];
		
		if (list.length == 0) {
			return;
		}
		
		for (int i = 0; i < list.length; i++) {
			if(new File(FILE_DIR + File.separator + list[i]).isDirectory()){
				files[i] = FILE_DIR + File.separator + list[i];
				ButtonList.add(new Button(Game.WIDTH/Game.SCALE/2-50, 1, 100, 20, game.screen, game.input));
				ButtonList.get(i).TextData( list[i], true, 0, 0);
			}
		}
	}
	
	public class GenericExtFilter implements FilenameFilter {
 
		private String ext;
 
		public GenericExtFilter(String ext) {
			this.ext = ext;
		}
 
		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}
	
	public void tick(){
		for(Button button : ButtonList){
			button.tick();
		}
		if(start.isclicked && ButtonList.size()!=0){
			game.SinglePlayer = new SinglePlayer(game, files[selected]);
			Game.gamemode = 1;
		}
		if(back.isclicked){
			Game.menu=0;
		}
		if(genmap.isclicked){
	        String name = "Map";
	        Map.newMap(FILE_DIR,name);
			LoadFiles();
		}
		if(del.isclicked && ButtonList.size()!=0){
			File dir = new File(files[selected] + File.separator);
			String[] del = dir.list();
			try{
				for(String Fdel : del){
					new File(files[selected] + File.separator + Fdel).delete();
				}
				new File(files[selected]).delete();
			}catch(ArrayIndexOutOfBoundsException e){}
			LoadFiles();
		}
		back.tick();
		genmap.tick();
		del.tick();
		if(ButtonList.size()!=0)start.tick();
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
		genmap.render();
		if(ButtonList.size()!=0)del.render();
		if(ButtonList.size()!=0)start.render();
		if(selected > 0)scrollUP.render();
		if(selected < ButtonList.size()-1)scrollDOWN.render();
		Game.font.render(game.screen.width/Game.SCALE/2-50, 10, "Map Selection", 0, 0xff000000, game.screen);
	}
}
