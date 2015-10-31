package Multiplayer;

import entities.MPlayer;
import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Main.ConvertData;
import Main.Game;
import Main.InputHandler;
import Maps.Map;

public class Client {

	private String IP = "91.89.152.87";
	private static OutputStream out;
	private InputStream in;
	private Socket serverConnection = null;
	public static ServerManager server;
	
	private Screen screen;
	private InputHandler input;
	protected Map map;
	private Player player;
	private ArrayList<MPlayer> players;
	public File plr;
	private BufferedImage back = null;
	public PlayerManager playerManager;
	
	public Client(Game game, String ip) {
		try {back = ImageIO.read(SpriteSheet.class.getResourceAsStream("/NormalBack.png"));} catch (IOException e) {e.printStackTrace();}
		this.screen = game.screen;
		this.input = game.input;
		IP=ip;
		map = new Map(null, screen);
		try {
			serverConnection = new Socket(IP, Game.PORT);
			Game.logInfo("connected so Server");
			in = serverConnection.getInputStream();
			out = serverConnection.getOutputStream();
		} catch (IOException e) {e.printStackTrace();}
		server = new ServerManager(this,in);
		try {
			ServerManager.request(Request.PLAYER_COLOR,ConvertData.I2B(Game.configs.PlrCol));
		} catch (IOException e) {e.printStackTrace();}
		Thread t = new Thread(server);
		t.setName("ServerConnection");
		t.start();
		player = new Player(map, game);player.create();
		screen.xOffset= player.x-Game.WIDTH/3/2;
		screen.yOffset= player.y-Game.HEIGHT/3/2;
		players = new ArrayList<MPlayer>();
		playerManager = new PlayerManager(game, players);
		map.setGametype(Map.GT_CLIENT);
	}
	
	public void tick(int tickCount) throws IOException{
		player.tick(tickCount);
		map.sendMapUpdates(tickCount);
		for(MPlayer p : players)p.tick(tickCount);
		if(tickCount%4==0) player.Gravity();
		
		if(tickCount%3==0) {
			byte[] data = new byte[10];
			ArrayList<Byte> temp = new ArrayList<Byte>();
			ConvertData.I2B(temp, player.x); ConvertData.I2B(temp, player.y);
			for(int i = 0; i < temp.size(); i++)data[i]=temp.get(i);
			data[8]=player.getAnim();data[9]=player.getDir();
			ServerManager.request(Request.PLAYER_DATA, data);
		}
		
		if(input.Esc.isPressed()){
			send2Server(Request.CLOSE_CONNECTION);
			Game.reset = true;
		}
		if(tickCount%60==0){
//			System.out.println("FPS:"+Game.fps);
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
		Game.sfont.render(10+screen.xOffset, 20+screen.yOffset, "X:" + Integer.toString(player.x), 0, 0xff000000, screen);
		Game.sfont.render(10+screen.xOffset, 30+screen.yOffset, "Y:" + Integer.toString(player.y), 0, 0xff000000, screen);
		Game.sfont.render(10+screen.xOffset, 40+screen.yOffset, "sX:" + Integer.toString(player.getspeedX()), 0, 0xff000000, screen);
		Game.sfont.render(10+screen.xOffset, 50+screen.yOffset, "sY:" + Integer.toString(player.getspeedY()), 0, 0xff000000, screen);
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
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void reset() {
		map.cancelChunkLoading();
	}
	
}