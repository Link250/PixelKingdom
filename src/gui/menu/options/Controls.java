package gui.menu.options;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import gfx.SpriteSheet;
import gui.Button;
import gui.menu.OptionScreen;
import main.Game;
import main.Keys;

public class Controls {
	private OptionScreen mainMenu;
	
	private Button  back;
	private Map<Button, Integer> buttonValues;
	private ArrayList<KeyConfig> keyConfigs;
	
	public Controls(OptionScreen mainMenu) {
		this.mainMenu = mainMenu;
		this.back = new Button(10, 10, 20, 20);
		this.back.gfxData(new SpriteSheet("/Buttons/back.png"), true);
		
		this.buttonValues = new HashMap<>();
		this.keyConfigs = new ArrayList<>();
		this.resetKeyButtons();
	}
	
	private void resetKeyButtons() {
		this.buttonValues.clear();
		this.keyConfigs.clear();
		for (Keys key : Keys.values()) {
			addKeyButton(key.getName(), key);
		}
		int i = 0;
		for (KeyConfig keyConfig : keyConfigs) {
			keyConfig.button.SetPos(Game.screen.width/Game.SCALE/2, Game.screen.height/Game.SCALE/keyConfigs.size()*i);
			keyConfig.height = Game.screen.height/Game.SCALE/keyConfigs.size()*i;
			i++;
		}
	}
	
	private void addKeyButton(String name, Keys key) {
		Button button = new Button(100, 100, 100, 20);
		for (Entry<Integer, Keys> entry : Game.configs.keyConfig.entrySet()) {
			if(key.equals(entry.getValue())) {
				buttonValues.put(button, entry.getKey());
				button.TextData(KeyEvent.getKeyText(entry.getKey()), false, 5, 0);
				break;
			}
		}
		keyConfigs.add(new KeyConfig(button, name));
	}
	
	private void setKey(KeyConfig keyConfig){
		int keyCode = 0;
		int keyCodeOld = this.buttonValues.get(keyConfig.button);
		Keys key = Game.configs.keyConfig.get(keyCodeOld);
		while((keyCode=Game.input.lastKeyCode)==0) {try{Thread.sleep(10);} catch (InterruptedException e) {}}
		//check if this key is unused
		for (Integer code : Game.configs.keyConfig.keySet()) {
			if(keyCode==code)return;
		}
		//setting new Codes
		Game.configs.keyConfig.remove(keyCodeOld);
		Game.configs.keyConfig.put(keyCode, key);
		this.resetKeyButtons();
		
		System.out.println(keyConfig.text);
	}
	
	public void tick(){
		back.tick();
		if(back.isclicked || Keys.MENU.click()){
			this.mainMenu.resetMenu();
		}
		for (KeyConfig keyConfig : keyConfigs) {
			if(keyConfig.tick())break;
		}
	}
	
	public void render(){
		back.render();
		Game.font.render(Game.screen.width/Game.SCALE/2-30, 10, "Controls", 0, 0xff000000, Game.screen);
//		Game.font.render(Game.screen.width/Game.SCALE/2-50, Game.screen.height/Game.SCALE/2, "COMING-SOON", 0, 0xff000000, Game.screen);
		for (KeyConfig keyConfig : keyConfigs) {
			keyConfig.render();
		}
	}
	
	private class KeyConfig{
		protected Button button;
		protected int height = 0;
		private String text;
		
		public KeyConfig(Button button, String text) {
			this.button = button;
			this.text = text;
		}
		
		public boolean tick(){
			button.tick();
			if(button.isclicked){
				setKey(this);
				return true;
			}
			return false;
		}
		
		public void render(){
			Game.font.render(Game.screen.height/Game.SCALE/4, height, text, 0, 0xff000000, Game.screen);
			button.render();
		}
	}
}
