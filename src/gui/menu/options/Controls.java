package gui.menu.options;

import static main.KeyConfig.keyMapping;
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
	
	private Button  back,scrollUP,scrollDOWN;
	private Map<Button, Integer> buttonValues;
	private ArrayList<KeyConfig> keyConfigs;
	
	private int lOffset;
	private int lVisibleEntries;
	private int lFieldTop, lFieldBottom, lFieldSize;
	
	public Controls(OptionScreen mainMenu) {
		this.mainMenu = mainMenu;
		this.back = new Button(10, 10, 20, 20);
		this.back.gfxData(new SpriteSheet("/Buttons/back.png"), true);
		this.lOffset = 0;
		this.lVisibleEntries = 5;
		this.lFieldTop = Game.screen.height/Game.SCALE/10*3;
		this.lFieldBottom = Game.screen.height/Game.SCALE-30;
		this.lFieldSize = this.lFieldBottom-this.lFieldTop;
		
		this.buttonValues = new HashMap<>();
		this.keyConfigs = new ArrayList<>();
		this.resetKeyButtons();
		
		this.scrollUP = new Button(Game.screen.width/Game.SCALE/2-10, this.lFieldTop-20, 20, 20);
		this.scrollUP.gfxData(new SpriteSheet("/Buttons/ArrowDown.png"), 0x01, false);
		this.scrollDOWN = new Button(Game.screen.width/Game.SCALE/2-10, this.lFieldBottom, 20, 20);
		this.scrollDOWN.gfxData(new SpriteSheet("/Buttons/ArrowDown.png"), false);
	}
	
	private void resetKeyButtons() {
		this.buttonValues.clear();
		this.keyConfigs.clear();
		for (Keys key : Keys.values()) {
			addKeyButton(key.getName(), key);
		}
		this.setButtonPositions();
	}
	
	private void addKeyButton(String name, Keys key) {
		Button button = new Button(100, 100, 100, 20);
		for (Entry<Integer, Keys> entry : keyMapping.entrySet()) {
			if(key.equals(entry.getValue())) {
				buttonValues.put(button, entry.getKey());
				button.TextData(KeyEvent.getKeyText(entry.getKey()), false, 5, 0);
				break;
			}
		}
		keyConfigs.add(new KeyConfig(button, name));
	}
	
	private void setButtonPositions() {
		int buttonHeight = keyConfigs.get(0).button.getHeight();
		int gapHeight = (this.lFieldSize-(buttonHeight*this.lVisibleEntries))/(this.lVisibleEntries+1);
		for(int i = 0; i < this.lVisibleEntries; i++) {
			keyConfigs.get(i+this.lOffset).button.SetPos(Game.screen.width/Game.SCALE/2, this.lFieldTop+(i+1)*gapHeight+i*buttonHeight);
			keyConfigs.get(i+this.lOffset).height = this.lFieldTop+(i+1)*gapHeight+i*buttonHeight;
		}
	}
	
	private void setKey(KeyConfig keyConfig){
		int keyCode = 0;
		int keyCodeOld = this.buttonValues.get(keyConfig.button);
		Keys key = keyMapping.get(keyCodeOld);
		while((keyCode=Game.input.lastKeyCode)==0) {try{Thread.sleep(10);} catch (InterruptedException e) {}}
		//check if this key is unused
		for (Integer code : keyMapping.keySet()) {
			if(keyCode==code)return;
		}
		//setting new Codes
		keyMapping.remove(keyCodeOld);
		keyMapping.put(keyCode, key);
		this.resetKeyButtons();
		
		System.out.println(keyConfig.text);
	}
	
	public void tick(){
		back.tick();
		if(back.isclicked || Keys.MENU.click()){
			this.mainMenu.resetMenu();
		}
		int scroll = Game.input.mouse.getScroll();
		if(this.lOffset > 0)scrollUP.tick();
		if((scrollUP.isclicked || scroll<0) && this.lOffset > 0) {this.lOffset--;this.setButtonPositions();}
		if(this.lOffset < keyConfigs.size()-this.lVisibleEntries)scrollDOWN.tick();
		if((scrollDOWN.isclicked || scroll>0) && this.lOffset < keyConfigs.size()-this.lVisibleEntries) {this.lOffset++;this.setButtonPositions();}
		
		for(int i = this.lOffset; i < this.lOffset+this.lVisibleEntries; i++) {
			if(keyConfigs.get(i).tick())break;
		}
	}
	
	public void render(){
		back.render();
		if(this.lOffset > 0)scrollUP.render();
		if(this.lOffset < keyConfigs.size()-this.lVisibleEntries)scrollDOWN.render();
//		Game.font.render(Game.screen.width/Game.SCALE/2-30, 10, "Controls", 0, 0xff000000, Game.screen);
//		Game.font.render(Game.screen.width/Game.SCALE/2-50, Game.screen.height/Game.SCALE/2, "COMING-SOON", 0, 0xff000000, Game.screen);
		for(int i = this.lOffset; i < this.lOffset+this.lVisibleEntries; i++) {
			keyConfigs.get(i).render();
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
