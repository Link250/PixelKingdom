package gui.menu;

import gfx.Screen;
import gui.Button;
import main.Game;
import main.Keys;
import main.MouseInput;
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
	/** should always be uneven or else the delete and play buttons dont lign up */
	private int maxButtonsOnScreen = 1;
	
	public MapSelection() {
		int buttonSize = 60;
		maxButtonsOnScreen = (int) ((Screen.height-buttonSize*3)/(buttonSize*1.5));
		//makes it uneven
		maxButtonsOnScreen -= maxButtonsOnScreen%2==0 ? 1 : 0;
		back = new Button(50, 50, buttonSize, buttonSize);
		back.gfxData("/Buttons/back.png", true);
		genmap = new Button(Screen.width-50, 50, buttonSize, buttonSize);
		genmap.gfxData("/Buttons/new.png", true);
		del = new Button(Screen.width/2-240, Screen.height/2, buttonSize, buttonSize);
		del.gfxData("/Buttons/delete.png", true);
		start = new Button(Screen.width/2+240, Screen.height/2, buttonSize, buttonSize);
		start.gfxData("/Buttons/play.png", true);
		scrollUP = new Button(Screen.width/2, Screen.height/2-(maxButtonsOnScreen/2+1)*buttonSize*3/2, buttonSize, buttonSize);
		scrollUP.gfxData("/Buttons/ArrowDown.png", false, true, false);
		scrollDOWN = new Button(Screen.width/2, Screen.height/2+(maxButtonsOnScreen/2+1)*buttonSize*3/2, buttonSize, buttonSize);
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
				ButtonList.get(i).TextData( list[i], true, true);
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
			Game.singlePlayer = new SinglePlayer(files[selected]);
			Game.gamemode = Game.GameMode.SinglePlayer;
		}
		if(back.isclicked || Keys.MENU.click()){
			Game.menu = new MainMenu();
		}
		if(genmap.isclicked){
			Game.menu = new NewMapScreen();
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
		int scroll = MouseInput.mouse.getScroll();
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
				ButtonList.get(selected+i).setPos(Game.WIDTH/2, Screen.height/2+(i*90));
				ButtonList.get(selected+i).render();
			}catch(ArrayIndexOutOfBoundsException e){}catch(IndexOutOfBoundsException e){}
		}
		back.render();
		genmap.render();
		if(ButtonList.size()!=0)del.render();
		if(ButtonList.size()!=0)start.render();
		if(selected > 0)scrollUP.render();
		if(selected < ButtonList.size()-1)scrollDOWN.render();
		Game.font.render(Screen.width/2, 50, "Map Selection", 0, 0xff000000);
	}
}
