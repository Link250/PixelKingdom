package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import entities.Player;
import gfx.Screen;
import map.Map;
import pixel.UDS;

public class SinglePlayer {
	protected Map map;
	private Player player;
	public static boolean debuginfo = false;
	private String files;
	public File plr;
	
	public SinglePlayer(String files){
		this.files = files;
		map = new Map(files);
		player = new Player(map);
		plr = new File(files + File.separator + "plr.pdat");
		if(plr.exists()){
			try {load();}catch(ClassCastException e){create();save();}
		}else{
			create();
			save();
		}
		map.addMobEntity(player);
		Screen.setMapPos(player.x, player.y);
	}
	
	public void tick(int tickCount){
		
		map.tick(tickCount);
		
		if(Keys.DEBUGINFO.click()){
			debuginfo = !debuginfo;
		}
		if(Keys.DEBUGMODE.click()){
			if(Game.devmode)Game.devmode = false;
			else Game.devmode = true;
		}
		if(Keys.MENU.click()){
			map.save();
			this.save();
			Game.reset = true;
		}
		Screen.setMapPos(player.x, player.y);

		if(debuginfo){
			if(Game.devmode&&Keys.DEBUGPXL.click()) {
				int X = MouseInput.mouse.getMapX(), Y = MouseInput.mouse.getMapY();
				Game.logInfo(X+" "+Y+" id{"
						+map.getID(X, Y, Map.LAYER_BACK)+","
						+map.getID(X, Y, Map.LAYER_LIQUID)+","
						+map.getID(X, Y, Map.LAYER_FRONT)+","
						+map.getlight(X, Y)+"}");
				Game.logInfo(X+" "+Y+" up{"
						+map.isUpdating(X, Y, Map.LAYER_BACK)+","
						+map.isUpdating(X, Y, Map.LAYER_LIQUID)+","
						+map.isUpdating(X, Y, Map.LAYER_FRONT)+","
						+map.isUpdating(X, Y, Map.LAYER_LIGHT)+"}");
				map.addPixelUpdate(X, Y, 1);
				for(int L : Map.LAYER_ALL) {
					Game.logInfo("UPS layer " + L);
					UDS uds = map.<UDS>getUDS(X, Y, L);
					if(uds!=null) {
						Field[] f = uds.getClass().getDeclaredFields();
						for (Field field : f) {
							try {
							if(byte.class == field.getType()){Game.logInfo(field.getName()+" "+field.getByte(uds));continue;}
							if(short.class == field.getType()){Game.logInfo(field.getName()+" "+field.getShort(uds));continue;}
							if(int.class == field.getType()){Game.logInfo(field.getName()+" "+field.getInt(uds));continue;}
							if(long.class == field.getType()){Game.logInfo(field.getName()+" "+field.getLong(uds));continue;}
							if(float.class == field.getType()){Game.logInfo(field.getName()+" "+field.getFloat(uds));continue;}
							if(double.class == field.getType()){Game.logInfo(field.getName()+" "+field.getDouble(uds));continue;}
							if(boolean.class == field.getType()){Game.logInfo(field.getName()+" "+field.getBoolean(uds));continue;}
							if(char.class == field.getType()){Game.logInfo(field.getName()+" "+field.getChar(uds));continue;}
							} catch (IllegalArgumentException | SecurityException | IllegalAccessException e) {e.printStackTrace();}
						}
					}
				}
//				this.mobList.add(new Slime(this.map, player.x, player.y));
			}
			if(tickCount%60==0){
				Game.logInfo("FPS:"+Game.fps+" PixelUpdates:"+map.updateCountPixel+" LightUpdates:"+map.updateCountLight);
				map.updateCountPixel=0;
				map.updateCountLight=0;
			}
		}
	}
	
	public void render(){
		
		map.render();
		player.renderGUI();
		
		if(debuginfo){
			Game.sfont.render(50, 30, false, false, "FPS:" + Integer.toString(Game.fps), 0, 0xff000000);
			Game.sfont.render(50, 60, false, false, "X:" + Double.toString(player.x), 0, 0xff000000);
			Game.sfont.render(50, 90, false, false, "Y:" + Double.toString(player.y), 0, 0xff000000);
			Game.sfont.render(50, 120, false, false, "sX:" + Integer.toString(player.getspeedX()), 0, 0xff000000);
			Game.sfont.render(50, 150, false, false, "sY:" + Integer.toString(player.getspeedY()), 0, 0xff000000);
		}
	}
	
	public void create(){
		try {
			plr.createNewFile();
		} catch (IOException e) {e.printStackTrace();}
		player.create();
		Game.logInfo("Player Created");
	}
	
	public void save(){
		ArrayList<Byte> savedata = new ArrayList<Byte>();
		player.save(savedata);
		
		byte[] save = new byte[savedata.size()];
		for(int i = 0; i < save.length; i++){
			save[i] = savedata.get(i);
		}
		try {
			FileOutputStream fos = new FileOutputStream(files + File.separator + "plr.pdat");
			fos.write(save);
			fos.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void load(){
		byte[] temp = new byte[(int)plr.length()];
		try {
			FileInputStream fileInputStream = new FileInputStream(plr);
			fileInputStream.read(temp);
			fileInputStream.close();
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		ArrayList<Byte> data = new ArrayList<Byte>();
		for(byte dat : temp){data.add(dat);}
		player.load(data);
		Game.logInfo("Player Loaded");
	}
}
