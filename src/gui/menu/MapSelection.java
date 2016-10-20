package gui.menu;

import gfx.SpriteSheet;
import gui.Button;
import gui.NewMapWindow;
import main.Game;
import main.Keys;
import main.SinglePlayer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class MapSelection implements GameMenu{
	 
	public static final String FILE_DIR = Game.GAME_PATH+"maps";
	
	ArrayList<Button> ButtonList;
	private String[] list;
	private String[] files;
	private Button  back,genmap,del,start,scrollUP,scrollDOWN;
	private int selected = 0;
	private Game game;
	/** should always be uneven or else the delete and play buttons dont lign up */
	private int maxButtonsOnScreen = 1;
	
	private NewMapWindow newMapWindow;
	
	public MapSelection(Game game) {
		this.game = game;
		int buttonSize = 60;
		maxButtonsOnScreen = (int) ((Game.screen.height-buttonSize*3)/(buttonSize*1.5));
		//makes it uneven
		maxButtonsOnScreen -= maxButtonsOnScreen%2==0 ? 1 : 0;
		back = new Button(50, 50, buttonSize, buttonSize);
		back.gfxData("/Buttons/back.png", true);
		genmap = new Button(Game.screen.width-50, 50, buttonSize, buttonSize);
		genmap.gfxData("/Buttons/new.png", true);
		del = new Button(Game.screen.width/2-240, Game.screen.height/2, buttonSize, buttonSize);
		del.gfxData("/Buttons/delete.png", true);
		start = new Button(Game.screen.width/2+240, Game.screen.height/2, buttonSize, buttonSize);
		start.gfxData("/Buttons/play.png", true);
		scrollUP = new Button(Game.screen.width/2, Game.screen.height/2-(maxButtonsOnScreen/2+1)*buttonSize*3/2, buttonSize, buttonSize);
		scrollUP.gfxData("/Buttons/ArrowDown.png", 0x01, false);
		scrollDOWN = new Button(Game.screen.width/2, Game.screen.height/2+(maxButtonsOnScreen/2+1)*buttonSize*3/2, buttonSize, buttonSize);
		scrollDOWN.gfxData("/Buttons/ArrowDown.png", false);
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
				ButtonList.add(new Button(0, 0, 300, 60));
				ButtonList.get(i).TextData( list[i], true);
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
			game.SinglePlayer = new SinglePlayer(files[selected]);
			Game.gamemode = Game.GameMode.SinglePlayer;
		}
		if(back.isclicked || Keys.MENU.click()){
			if(this.newMapWindow != null)this.newMapWindow.dispose();
			Game.menu.subMenu=Menu.MainMenu;
		}
		if(genmap.isclicked){
			if(this.newMapWindow != null && this.newMapWindow.isDisplayable()) {
				this.newMapWindow.requestFocus();
			}else{
				this.newMapWindow = new NewMapWindow(game, this);
			}
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
		int scroll = Game.input.mouse.getScroll();
		if(ButtonList.size()!=0)start.tick();
		if(selected > 0)scrollUP.tick();
		if((scrollUP.isclicked || scroll<0) && selected > 0)selected--;
		if(selected < ButtonList.size()-1)scrollDOWN.tick();
		if((scrollDOWN.isclicked || scroll>0) && selected < ButtonList.size()-1)selected++;
		if(selected > ButtonList.size()-1 && ButtonList.size()!=0)selected = ButtonList.size()-1;
	}
	
	public void render(){
		for(int i = -maxButtonsOnScreen/2; i <= maxButtonsOnScreen/2; i++){
			try{
				ButtonList.get(selected+i).SetPos(Game.WIDTH/2, Game.screen.height/2+(i*90));
				ButtonList.get(selected+i).render();
			}catch(ArrayIndexOutOfBoundsException e){}catch(IndexOutOfBoundsException e){}
		}
		back.render();
		genmap.render();
		if(ButtonList.size()!=0)del.render();
		if(ButtonList.size()!=0)start.render();
		if(selected > 0)scrollUP.render();
		if(selected < ButtonList.size()-1)scrollDOWN.render();
		Game.font.render(Game.screen.width/2, 50, "Map Selection", 0, 0xff000000, Game.screen);
	}
}
