package main;

import gfx.ColorSheet;
import gfx.PxlFont;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;
import gfx.Mouse.MouseType;
import gui.Window;
import gui.menu.MainMenu;
import gui.menu.Menu;
import item.ItemList;
import map.BiomeList;
import multiplayer.client.Client;
import multiplayer.server.Server;
import pixel.PixelList;
import sun.font.TrueTypeFont;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.lwjgl.glfw.GLFWVidMode;

public class Game implements Runnable{

	public static enum GameMode{
		Menu,SinglePlayer,MultiPlayer
	}

	public static int WIDTH;
	public static int HEIGHT;
	public static final String NAME = "Pixel Kingdom - the number of Commits is over 100 !";
	public static final String GAME_PATH = System.getenv("APPDATA") + "\\PixelKingdom\\";
	public static final int PORT = 7777;
	public static final double nsPerTick = 1000000000D/60D;
	public static int ticks = 0;
	public static int frames = 0;
	public static int fps = 0;
	
	private Window window;
	
	private boolean running = false;
	public static boolean reset = false;
	public int tickCount = 0;
	public static boolean devmode;
	public static GameMode gamemode = GameMode.Menu;
	public static MainMenu menu;
	
	public static ItemList itemlist;
	public static PixelList pixellist;
	public static BiomeList biomelist;
	public static ColorSheet csheetf = new ColorSheet("/Mat_Front.png");
	public static ColorSheet csheetm = new ColorSheet("/Mat_Mid.png");
	public static ColorSheet csheetb = new ColorSheet("/Mat_Back.png");
	public static Screen screen;
	public static PxlFont font;
	public static PxlFont sfont;
	public static PxlFont mfont;
	/**Coders Crux Font, Size: 10*18*/
	public static PxlFont ccFont;
	public SinglePlayer SinglePlayer;
	public Client client;
	public Server server;
	
	public SpriteSheet back;
	
	public TrueTypeFont font2;
	
	public void init(){
		File gameDir = new File(Game.GAME_PATH);
		if(!gameDir.isDirectory()) {gameDir.mkdirs();Game.logInfo("Created PixelKingdom Path");}
		MainConfig.load();
		KeyConfig.load();
		
		System.setProperty("sun.java2d.opengl","True");
		Window.setCallbacks();
		
		if(!glfwInit()) {
			System.err.println("GLFW Failed to initialize!");
			System.exit(1);
		}
		
		if(MainConfig.fullscreen) {
			GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
			Game.WIDTH = vid.width();
			Game.HEIGHT = vid.height();
		}else {
			Game.WIDTH = MainConfig.resX;
			Game.HEIGHT = MainConfig.resY;
		}
		
		window = new Window(WIDTH, HEIGHT, MainConfig.fullscreen, "Pixel Kingdom");
		
		window.init();
		
		new KeyInput(window.getWindow());
		new MouseInput(window.getWindow());
		glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		glfwSetWindowSizeLimits(window.getWindow(), WIDTH, HEIGHT, WIDTH, HEIGHT);

		Screen.MAP_ZOOM = MainConfig.mapZoom;
		Screen.initialize(WIDTH, HEIGHT, csheetf, csheetm, csheetb);
		
		menu = new MainMenu(this);
		mfont = new PxlFont(new SpriteSheet("/StackFont.png", 12, 15), "1234567890", 12, 15, -2);
		sfont = new PxlFont(new SpriteSheet("/8x8Font.png", 24, 24), " !\"# %&'()* ,-./0123456789:; = ? ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{ }~",24,24, 1);
		font = new PxlFont(new SpriteSheet("/Font.png", 45, 60), "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 1234567890!\",;%&/()=?'+-.",45,60, 2);
		ccFont = new PxlFont(new SpriteSheet("/Font/coders_crux.png", 12, 20), "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890^´+#<,.-°!\"§$%&/()=?`*'>;:_²³{[]}\\~| ",12, 20, 2);
		
		pixellist = new PixelList();
		itemlist = new ItemList();
		biomelist = new BiomeList();
		
		back = new SpriteSheet("/NormalBack.png");
		//TODO		TESTING AREA
	}
	
	public void reset(){
		gamemode = GameMode.Menu;
		menu.subMenu = Menu.MainMenu;
		Screen.xOffset=0;
		Screen.yOffset=0;
		Mouse.Item = null;
		Mouse.mouseType=MouseType.DEFAULT;
		if(client!=null)client.reset();
		reset = false;
	}
	
	public synchronized void start() {
		running = true;
		Thread t = new Thread(this);
		t.setName("Main Game");
		t.start();
	}

	public synchronized void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
				
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		init();
//		try {
//			WindowIcon.setIcon(window.getHeight(), "WindowIcon.png");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		glClearColor(0.6f, 0.6f, 0.9f, 1.0f);		
		while(running && !window.shouldClose()){
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			
			while(delta >= 1){
				ticks ++;
				tick();
				window.update();
				delta -= 1;
			}
			
			if(true){
				frames ++;
				render();
			}
			
			if(System.currentTimeMillis() - lastTimer >= 1000){
				lastTimer += 1000;
//				System.out.println("Frames: " + frames + " ; Ticks: " + ticks);
				fps = frames;
				frames = 0;
				ticks = 0;
			}
		}
		if(gamemode == GameMode.SinglePlayer)SinglePlayer.map.save();
		if(server!=null)server.save();
		MainConfig.save();
		KeyConfig.save();
		Game.logInfo("Game shut down by User "+System.getProperty("user.name")+". ~Thanks 4 playing");
		System.exit(0);
	}
	
	public void tick(){
		tickCount++;
		if(reset)reset();
		switch (gamemode){
		case Menu : 
			menu.tick();
			break;
		case SinglePlayer :
			SinglePlayer.tick(tickCount);
			break;
		case MultiPlayer :
			try {
				client.tick(tickCount);
			} catch (IOException e) {Game.reset=true;}
			break;
		default : break;
		}
	}
	
	public void render(){
		glClear(GL_COLOR_BUFFER_BIT);

		for (int x = 0; x < Screen.width; x+= back.getWidth()) {
			for (int y = 0; y < Screen.height; y+= back.getHeight()) {
				Screen.drawGUISprite(x, y, back);
			}
		}
		switch (gamemode){
		case Menu :
			menu.render();
			break;
		case SinglePlayer :
			SinglePlayer.render();
			break;
		case MultiPlayer :
			client.render();
			break;
		default : break;
		}
		
		Mouse.render();
		
		window.swapBuffers();
	}
	
	public static void logError(Object text) {
		log(text,1);
	}
	public static void logWarning(Object text) {
		log(text,2);
	}
	public static void logInfo(Object text) {
		log(text,3);
	}
	
	private static void log(Object text, int type) {
		Calendar time = Calendar.getInstance();
		int h=time.get(Calendar.HOUR_OF_DAY),
			m=time.get(Calendar.MINUTE),
			s=time.get(Calendar.SECOND);
		String header = "[";
		if(h<10)header+="0";
		header+=h+":";
		if(m<10)header+="0";
		header+=m+":";
		if(s<10)header+="0";
		header+=s+"]"+"["+Thread.currentThread().getName()+"]";
		switch(type) {
		case 1:header+="[ERROR]";break;
		case 2:header+="[WARNING]";break;
		case 3:header+="[INFO]";break;
		}
		if(type==1)
			System.err.println(header+": "+text);
		else
			System.out.println(header+": "+text);
	}
	
	public static void main(String[] args){
		Game.logInfo("Current Java Version running : "+System.getProperty("java.version"));
		Game.logInfo("Current Operating System : "+System.getProperty("os.name"));
		for(String s : args){
			if(s.contains("-devmode")){Game.devmode = true;Game.logInfo("Developer Mode activated !");}
			Game.logInfo(s);
		}
		
		new Game().start();
	}
}
