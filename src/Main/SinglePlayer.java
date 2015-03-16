package Main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Maps.Map;
import Main.InputHandler;
import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;

public class SinglePlayer {
	private Screen screen;
	private InputHandler input;
	private Map map;
	private Player player;
	public static boolean debuginfo = false;
	private String files;
	public File plr;
	private BufferedImage back = null;
	
	public SinglePlayer(Game game, String files){
		try {back = ImageIO.read(SpriteSheet.class.getResourceAsStream("/NormalBack.png"));} catch (IOException e) {e.printStackTrace();}
		this.screen = game.screen;
		this.input = game.input;
		this.files = files;
		map = new Map(files, screen);
		player = new Player(map, game);
		plr = new File(files + File.separator + "plr.pdat");
		if(plr.exists()){
			try {load();}catch(ClassCastException e){create();save();}
		}else{
			create();
			save();
		}
	}
	
	public void tick(int tickCount){
		
		player.tick(tickCount);
		if(tickCount%4==0) player.Gravity();

		map.tick(tickCount);
		
		if(input.F3.click()){
			if(debuginfo)debuginfo = false;
			else debuginfo = true;
		}
		if(input.F5.click()){
			if(Game.devmode)Game.devmode = false;
			else Game.devmode = true;
		}
		if(input.Esc.isPressed()){
			map.save();
			this.save();
			Game.reset = true;
		}
		screen.xOffset= player.x-Game.WIDTH/3/2;
		screen.yOffset= player.y-Game.HEIGHT/3/2;

		if(debuginfo){
			if(tickCount%60==0){
				System.out.println("FPS:"+Game.fps+" BlockUpdates:"+map.updatecount);
				map.updatecount=0;
			}
		}
	}
	
	public void render(Graphics g){
		
		g.drawImage(back, 0, 0, Game.WIDTH, Game.HEIGHT, null);

		map.render();
		
		player.render();
		
		if(debuginfo){
			Game.sfont.render(10+screen.xOffset, 10+screen.yOffset, "FPS:" + Integer.toString(Game.fps), 0, 0xff000000, screen);
			Game.sfont.render(10+screen.xOffset, 20+screen.yOffset, "X:" + Integer.toString(player.x), 0, 0xff000000, screen);
			Game.sfont.render(10+screen.xOffset, 30+screen.yOffset, "Y:" + Integer.toString(player.y), 0, 0xff000000, screen);
			Game.sfont.render(10+screen.xOffset, 40+screen.yOffset, "sX:" + Integer.toString(player.getspeedX()), 0, 0xff000000, screen);
			Game.sfont.render(10+screen.xOffset, 50+screen.yOffset, "sY:" + Integer.toString(player.getspeedY()), 0, 0xff000000, screen);
		}
	}
	
	public void create(){
		try {
			plr.createNewFile();
		} catch (IOException e) {e.printStackTrace();}
		player.create();
		System.out.println("Player Created");
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
		System.out.println("Player Loaded");
	}
}
