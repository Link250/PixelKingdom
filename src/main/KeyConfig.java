package main;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import main.ConfigFile.NoConfigFoundException;

public class KeyConfig {

	public static Map<Integer, Keys> keyMapping = new HashMap<>();
	private static ConfigFile configFile = new ConfigFile(Game.GAME_PATH+"Keys.optn");
	
	public static void setDefaultValues() {
		keyMapping.put(KeyEvent.VK_W,Keys.UP);
		keyMapping.put(KeyEvent.VK_S,Keys.DOWN);
		keyMapping.put(KeyEvent.VK_A,Keys.LEFT);
		keyMapping.put(KeyEvent.VK_D,Keys.RIGHT);
		keyMapping.put(KeyEvent.VK_SPACE,Keys.JUMP);
		keyMapping.put(KeyEvent.VK_SHIFT,Keys.CROUCH);
		keyMapping.put(KeyEvent.VK_ESCAPE,Keys.MENU);
		keyMapping.put(KeyEvent.VK_H,Keys.HOTBAR);
		keyMapping.put(KeyEvent.VK_I,Keys.INV);
		keyMapping.put(KeyEvent.VK_C,Keys.CRAFT);
		keyMapping.put(KeyEvent.VK_E,Keys.EQUIP);
		keyMapping.put(KeyEvent.VK_F3,Keys.DEBUGINFO);
		keyMapping.put(KeyEvent.VK_F5,Keys.DEBUGMODE);
		keyMapping.put(KeyEvent.VK_X,Keys.DEBUGPXL);
		//TODO don't override already existing Configs (override only the broken ones)
		save();
	}
	
	public static void load(){
		configFile.load();
		try{
			for (Keys key : Keys.values()) {
				keyMapping.put(configFile.getConfig(key.toString()), key);
			}
		}catch(ClassCastException | NoConfigFoundException e){
			Game.logInfo("New Key Configs created ");
			setDefaultValues();
		}
	}
	
	public static void save(){
		keyMapping.forEach((i, key) -> configFile.setConfig(key.toString(), i));
		configFile.save();
	}
}
