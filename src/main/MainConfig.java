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
	
	private static ConfigFile configFile = new ConfigFile(Game.GAME_PATH+"Options.optn");

	public static void setDefaults(){
		for(GameFields gameField : MainConfig.GameFields.values()) {
			fieldPos.put(gameField, new Point(10,10));
		}
		PlrCol = 0xff02e707;
		resX = 320*3;
		resY = resX/12*9;
		//TODO don't override already existing Configs (override only the broken ones)
		save();
	}
	
	public static void load(){
		configFile.load();
		try{
			for(GameFields gameField : MainConfig.GameFields.values()) {
				int x = configFile.getConfig(gameField.toString()+"_PosX");
				int y = configFile.getConfig(gameField.toString()+"_PosY");
				fieldPos.put(gameField, new Point(x,y));
			}
			PlrCol = configFile.getConfig("PlayerColor");
			resX = configFile.getConfig("ResolutionX");
			resY = configFile.getConfig("ResolutionY");
		}catch(ClassCastException | NoConfigFoundException e){
			Game.logInfo("New Main Configs created ");
			setDefaults();
		}
	}
	
	public static void save(){
		fieldPos.forEach((gf, p) -> {
			configFile.setConfig(gf.toString()+"_PosX", p.x);
			configFile.setConfig(gf.toString()+"_PosY", p.y);
		});
		configFile.setConfig("PlayerColor", PlrCol);
		configFile.setConfig("ResolutionX", resX);
		configFile.setConfig("ResolutionY", resY);
		configFile.save();
	}
}