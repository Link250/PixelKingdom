package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Mob;
import entities.Player;
import entities.entityList.Slime;
import gfx.Screen;
import gfx.SpriteSheet;
import map.Map;

public class SinglePlayer {
	private Screen screen;
	protected Map map;
	private Player player;
	public static boolean debuginfo = false;
	private String files;
	public File plr;
	private BufferedImage back = null;
	private ArrayList<Mob> mobList = new ArrayList<>();
	
	public SinglePlayer(String files){
		try {back = ImageIO.read(SpriteSheet.class.getResourceAsStream("/NormalBack.png"));} catch (IOException e) {e.printStackTrace();}
		this.screen = Game.screen;
		this.files = files;
		map = new Map(files, screen);
		player = new Player(map);
		plr = new File(files + File.separator + "plr.pdat");
		if(plr.exists()){
			try {load();}catch(ClassCastException e){create();save();}
		}else{
			create();
			save();
		}
		screen.xOffset= player.x-Game.WIDTH/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;
		screen.yOffset= player.y-Game.HEIGHT/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;
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
		screen.xOffset= player.x-Game.WIDTH/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;
		screen.yOffset= player.y-Game.HEIGHT/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;

		if(debuginfo){
			if(Game.devmode&&Keys.DEBUGPXL.click()) {
				int X = Game.input.mouse.getMapX(), Y = Game.input.mouse.getMapY();
				Game.logInfo(X+" "+Y+" id{"
						+map.getID(X, Y, Map.LAYER_BACK)+","
						+map.getID(X, Y, Map.LAYER_LIQUID)+","
						+map.getID(X, Y, Map.LAYER_FRONT)+"}");
				Game.logInfo(X+" "+Y+" up{"
						+map.isUpdating(X, Y, Map.LAYER_BACK)+","
						+map.isUpdating(X, Y, Map.LAYER_LIQUID)+","
						+map.isUpdating(X, Y, Map.LAYER_FRONT)+","
						+map.isUpdating(X, Y, Map.LAYER_LIGHT)+"}");
				map.addPixelUpdate(X, Y, 1);
				this.mobList.add(new Slime(this.map, player.x, player.y));
			}
			if(tickCount%60==0){
				Game.logInfo("FPS:"+Game.fps+" PixelUpdates:"+map.updateCountPixel+" LightUpdates:"+map.updateCountLight);
				map.updateCountPixel=0;
				map.updateCountLight=0;
			}
		}
	}
	
	public void render(Graphics g){
		
		g.drawImage(back, 0, 0, Game.WIDTH, Game.HEIGHT, null);

		map.render();
		for (Mob mob : mobList) {mob.render();}
		player.render();
		
		if(debuginfo){
			Game.sfont.render(10, 10, "FPS:" + Integer.toString(Game.fps), 0, 0xff000000, screen);
			Game.sfont.render(10, 20, "X:" + Integer.toString(player.x), 0, 0xff000000, screen);
			Game.sfont.render(10, 30, "Y:" + Integer.toString(player.y), 0, 0xff000000, screen);
			Game.sfont.render(10, 40, "sX:" + Integer.toString(player.getspeedX()), 0, 0xff000000, screen);
			Game.sfont.render(10, 50, "sY:" + Integer.toString(player.getspeedY()), 0, 0xff000000, screen);
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
