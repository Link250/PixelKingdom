package main;

import java.awt.Point;
import java.util.EnumMap;
import java.util.Map;

import main.ConfigFile.NoConfigFoundException;

public class MainConfig {
	
	public static enum GameFields{
		Field_Equipment,Field_Crafting,
		Field_ToolBag1,
		Field_MatBag1,Field_MatBag2,
		Field_BeltBag1,
		Field_ItemBag1,Field_ItemBag2;
	}
	
	public static Map<GameFields, Point> fieldPos = new EnumMap<>(MainConfig.GameFields.class);
	public static int PlrCol;
	public static int resX,resY;
	public static int mapZoom;
	
	private static ConfigFile configFile = new ConfigFile(Game.GAME_PATH+"Options.optn");

	private static <T>T loadConfig(String config, T defaultValue) {
		if(configFile.hasConfig(config))
			try {
				return configFile.getConfig(config);
			} catch (NoConfigFoundException | ClassCastException e) {
				Game.logError(config + " config was corrupted ! setting Default Value");
			}
		return (T) defaultValue;
	}
	
	public static void load(){
		configFile.load();
		for(GameFields gameField : MainConfig.GameFields.values()) {
			int x = loadConfig(gameField.toString()+"_PosX",10);
			int y = loadConfig(gameField.toString()+"_PosY",10);
			fieldPos.put(gameField, new Point(x,y));
		}
		PlrCol = loadConfig("PlayerColor",0xff02e707);
		resX = loadConfig("ResolutionX",320*3);
		resY = loadConfig("ResolutionY",320*3/12*9);
		mapZoom = loadConfig("MapZoom",2);
	}
	
	public static void save(){
		fieldPos.forEach((gf, p) -> {
			configFile.setConfig(gf.toString()+"_PosX", p.x);
			configFile.setConfig(gf.toString()+"_PosY", p.y);
		});
		configFile.setConfig("PlayerColor", PlrCol);
		configFile.setConfig("ResolutionX", resX);
		configFile.setConfig("ResolutionY", resY);
		configFile.setConfig("MapZoom", mapZoom);
		configFile.save();
	}
}