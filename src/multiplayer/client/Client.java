package multiplayer.client;

import entities.MPlayer;
import entities.Player;
import gfx.Screen;
import main.Game;
import main.Keys;
import map.Chunk;
import map.Map;
import multiplayer.MapManager;
import multiplayer.MapUpdater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {

	private Socket serverConnection = null;
	private ServerManager serverManager;
	
	private Map map;
	private MapManager mapManager;
	
	private ArrayList<MPlayer> players;
	private Player player;
	private File plr;
	private String files;
	
	public static boolean debuginfo = false;
	
	public Client(String IP, String files) throws UnknownHostException, IOException {
		serverConnection = new Socket(IP, Game.PORT);
		Game.logInfo("connected so Server");
		
		this.files = files;
		
		this.map = new Map(null);
		
		this.player = new Player(map);
		this.plr = new File(files + File.separator + "plr.pdat");
		if(this.plr.exists()){
			load();
		}else{
			create();
			save();
		}
		Screen.xOffset= player.x-Screen.width/3/2;
		Screen.yOffset= player.y-Screen.height/3/2;
		this.players = new ArrayList<MPlayer>();
		
		serverManager = new ServerManager(this,serverConnection);
		mapManager = new MapManager(new MapUpdater(map, Map.GT_CLIENT), serverManager);
		
		serverManager.addInputReceiver(mapManager);
		serverManager.addInputReceiver(new PlayerManager(players));
		serverManager.addInputReceiver(map.setChunkManager(serverManager));
		
		serverManager.startThread();
		
		serverManager.sendPlayerColor(player);
	}
	
	public void tick(int tickCount) throws IOException{
		player.tick(tickCount);
		map.tick(tickCount);
		
		mapManager.sendMapUpdates();
		
		for(MPlayer p : players)p.tick(tickCount);
		if(tickCount%4==0) {
			this.player.applyGravity();
		}
		
		if(tickCount%3==0) {
			this.serverManager.sendPlayerUpdate(player);
		}
		
		if(Keys.MENU.isPressed()){
			this.serverManager.disconnect();
			Game.reset = true;
		}
		if(Keys.DEBUGINFO.click()){
			debuginfo =! debuginfo;
		}
		if(tickCount%60==0){
			if(debuginfo){
				Game.logInfo("FPS:"+Game.fps+" PixelUpdates:"+map.updateCountPixel+" LightUpdates:"+map.updateCountLight);
				map.updateCountPixel=0;
				map.updateCountLight=0;
			}
		}
		Screen.xOffset= player.x-Screen.width/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;
		Screen.yOffset= player.y-Screen.height/Screen.MAP_SCALE/Screen.MAP_ZOOM/2;
	}
	
	public void render() {
//		g.drawImage(back, 0, 0, Game.WIDTH, Game.HEIGHT, null);
		
		map.render();
		
		player.render();
		
		for(MPlayer p : players)p.render();
		
		if(debuginfo){
			Game.sfont.render(10, 10, "FPS:" + Integer.toString(Game.fps), 0, 0xff000000);
			Game.sfont.render(10, 20, "cX:" + Integer.toString(player.x/Chunk.width), 0, 0xff000000);
			Game.sfont.render(10, 30, "cY:" + Integer.toString(player.y/Chunk.height), 0, 0xff000000);
			Game.sfont.render(10, 40, "rX:" + Integer.toString(player.x%Chunk.width), 0, 0xff000000);
			Game.sfont.render(10, 50, "rY:" + Integer.toString(player.y%Chunk.height), 0, 0xff000000);
		}
	}
	
	public void close() {
		try {
			serverConnection.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			save();
		}
	}
	
	public void reset() {
		map.cancelChunkLoading();
	}
	
	private void create(){
		try {
			(new File(files)).mkdirs();
			plr.createNewFile();
		} catch (IOException e) {e.printStackTrace();}
		player.create();
		Game.logInfo("Player Created");
	}
	
	private void save(){
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
	
	private void load(){
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