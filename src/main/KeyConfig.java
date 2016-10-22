package main;

import static org.lwjgl.glfw.GLFW.*;
import java.util.HashMap;
import java.util.Map;

import main.ConfigFile.NoConfigFoundException;

public class KeyConfig {

	public static Map<Integer, Keys> keyMapping = new HashMap<>();
	private static ConfigFile configFile = new ConfigFile(Game.GAME_PATH+"Keys.optn");
	
	public static void setDefaultValues() {
		keyMapping.put(GLFW_KEY_W,Keys.UP);
		keyMapping.put(GLFW_KEY_S,Keys.DOWN);
		keyMapping.put(GLFW_KEY_A,Keys.LEFT);
		keyMapping.put(GLFW_KEY_D,Keys.RIGHT);
		keyMapping.put(GLFW_KEY_SPACE,Keys.JUMP);
		keyMapping.put(GLFW_KEY_LEFT_SHIFT,Keys.CROUCH);
		keyMapping.put(GLFW_KEY_ESCAPE,Keys.MENU);
		keyMapping.put(GLFW_KEY_H,Keys.HOTBAR);
		keyMapping.put(GLFW_KEY_I,Keys.INV);
		keyMapping.put(GLFW_KEY_C,Keys.CRAFT);
		keyMapping.put(GLFW_KEY_E,Keys.EQUIP);
		keyMapping.put(GLFW_KEY_F3,Keys.DEBUGINFO);
		keyMapping.put(GLFW_KEY_F5,Keys.DEBUGMODE);
		keyMapping.put(GLFW_KEY_X,Keys.DEBUGPXL);
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
