package multiplayer;

import entities.MPlayer;
import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import main.ConvertData;
import main.Game;
import main.InputHandler;
import map.Chunk;
import map.Map;
import multiplayer.conversion.ConverterInStream;
import multiplayer.conversion.ConverterOutStream;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Client {

	private String IP = "91.89.152.87";
	private static ConverterOutStream out;
	private ConverterInStream in;
	private Socket serverConnection = null;
	public static ServerManager server;
	
	private Screen screen;
	private InputHandler input;
	protected Map map;
	private Player player;
	private File plr;
	private String files;
	private ArrayList<MPlayer> players;
	private BufferedImage back = null;
	public PlayerManager playerManager;
	
	public Client(Game game, String ip, String files) {
		try {back = ImageIO.read(SpriteSheet.class.getResourceAsStream("/NormalBack.png"));} catch (IOException e) {e.printStackTrace();}
		this.screen = game.screen;
		this.input = game.input;
		this.files = files;
		IP=ip;
		map = new Map(null, screen);
		try {
			serverConnection = new Socket(IP, Game.PORT);
			Game.logInfo("connected so Server");
			in = new ConverterInStream(serverConnection.getInputStream());
			out = new ConverterOutStream(serverConnection.getOutputStream());
		} catch (IOException e) {e.printStackTrace();}
		server = new ServerManager(this,in);
		try {
			ServerManager.request(Request.PLAYER_COLOR,ConvertData.I2B(Game.configs.PlrCol));
		} catch (IOException e) {e.printStackTrace();}
		
		Thread t = new Thread(server);
		t.setName("ServerConnection");
		t.start();
		player = new Player(map, game);
		
		plr = new File(files + File.separator + "plr.pdat");
		if(plr.exists()){
			try {load();}catch(ClassCastException e){create();save();}
		}else{
			create();
			save();
		}
		
		screen.xOffset= player.x-Game.WIDTH/3/2;
		screen.yOffset= player.y-Game.HEIGHT/3/2;
		players = new ArrayList<MPlayer>();
		playerManager = new PlayerManager(game, players);
		map.setGametype(Map.GT_CLIENT);
	}
	
	public void tick(int tickCount) throws IOException{
		player.tick(tickCount);
		map.tick(tickCount);
		map.sendMapUpdates(tickCount);
		for(MPlayer p : players)p.tick(tickCount);
		if(tickCount%4==0) player.Gravity();
		
		if(tickCount%3==0) {
			byte[] data = new byte[10];
			ConvertData.I2B(data, 0, player.x);
			ConvertData.I2B(data, 4, player.y);
			data[8]=player.getAnim();
			data[9]=player.getDir();
			ServerManager.request(Request.PLAYER_DATA, data);
		}
		
		if(input.Esc.isPressed()){
			send2Server(Request.CLOSE_CONNECTION);
			Game.reset = true;
		}
		screen.xOffset= player.x-Game.WIDTH/3/2;
		screen.yOffset= player.y-Game.HEIGHT/3/2;
	}
	
	public void render(Graphics g) {
		g.drawImage(back, 0, 0, Game.WIDTH, Game.HEIGHT, null);
		
		map.render();
		
		player.render();
		
		for(MPlayer p : players)p.render();
		
		Game.sfont.render(10+screen.xOffset, 10+screen.yOffset, "FPS:" + Integer.toString(Game.fps), 0, 0xff000000, screen);
		Game.sfont.render(10+screen.xOffset, 20+screen.yOffset, "cX:" + Integer.toString(player.x/Chunk.width), 0, 0xff000000, screen);
		Game.sfont.render(10+screen.xOffset, 30+screen.yOffset, "cY:" + Integer.toString(player.y/Chunk.height), 0, 0xff000000, screen);
		Game.sfont.render(10+screen.xOffset, 40+screen.yOffset, "rX:" + Integer.toString(player.x%Chunk.width), 0, 0xff000000, screen);
		Game.sfont.render(10+screen.xOffset, 50+screen.yOffset, "rY:" + Integer.toString(player.y%Chunk.height), 0, 0xff000000, screen);
	}
	
	public static void send2Server(int i) throws IOException{
		byte[] b = {(byte) (i>>24),(byte) (i>>16),(byte) (i>>8),(byte) i};
		out.write(b);
		out.flush();
	}
	
	public static void send2Server(short i) throws IOException{
		byte[] b = {(byte) (i>>8),(byte) i};
		out.write(b);
		out.flush();
	}
	
	public static void send2Server(byte i) throws IOException{
		byte[] b = {i};
		out.write(b);
		out.flush();
	}
	
	public static void send2Server(byte[] b) throws IOException{
		out.write(b);
		out.flush();
	}
	
	public static void send2Server(byte[][] b, int l) throws IOException{
		byte[] d = new byte[l]; l=0;
		for (int x = 0; x < b.length; x++) {
			for (int y = 0; y < b[x].length; y++) {
				d[l]=b[x][y];l++;
			}
		}
		out.write(d);
		out.flush();
	}
	
	public static void send2Server(byte[][] b) throws IOException{
		int n = 0;
		for (int i = 0; i < b.length; i++)
			n += b[i].length;
		byte[] d = new byte[n]; n=0;
		for (int x = 0; x < b.length; x++) {
			for (int y = 0; y < b[x].length; y++) {
				d[n]=b[x][y];n++;
			}
		}
		out.write(d);
		out.flush();
	}
	
	public void close() {
		try {
			out.close();
			in.close();
			serverConnection.close();
		} catch (IOException e) {e.printStackTrace();
		} finally {
			save();
		}
	}
	
	public void reset() {
		map.cancelChunkLoading();
	}
	
	public void create(){
		try {
			(new File(files)).mkdirs();
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