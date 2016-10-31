package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import entities.Mob;
import entities.Player;
import gfx.Screen;
import map.Map;

public class SinglePlayer {
	protected Map map;
	private Player player;
	public static boolean debuginfo = false;
	private String files;
	public File plr;
	private ArrayList<Mob> mobList = new ArrayList<>();
	
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
		Screen.xOffset= player.x-Game.WIDTH/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;
		Screen.yOffset= player.y-Game.HEIGHT/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;
	}
	
	public void tick(int tickCount){
		
		player.tick(tickCount);
		for (Mob mob : mobList) {mob.tick(tickCount);}
		
		if(tickCount%4==0) {
			player.applyGravity();
			for (Mob mob : mobList) {mob.applyGravity();}
		}

		map.tick(tickCount);
		
		if(Keys.DEBUGINFO.click()){
			if(debuginfo)debuginfo = false;
			else debuginfo = true;
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
		Screen.xOffset= player.x-Game.WIDTH/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;
		Screen.yOffset= player.y-Game.HEIGHT/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;

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
		for (Mob mob : mobList) {mob.render();}
		player.render();
		
		if(debuginfo){
			Game.sfont.render(50, 30, false, false, "FPS:" + Integer.toString(Game.fps), 0, 0xff000000);
			Game.sfont.render(50, 60, false, false, "X:" + Integer.toString(player.x), 0, 0xff000000);
			Game.sfont.render(50, 90, false, false, "Y:" + Integer.toString(player.y), 0, 0xff000000);
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
