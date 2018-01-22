package main;

import gfx.PxlFont;
import gfx.RessourceManager;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;
import gfx.Mouse.MouseType;
import gui.Window;
import gui.menu.GameMenu;
import gui.menu.MainMenu;
import item.ItemList;
import map.BiomeList;
import multiplayer.client.Client;
import multiplayer.server.Server;
import pixel.PixelList;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import org.lwjgl.glfw.GLFWVidMode;

public class Game implements Runnable{

	public static enum GameMode{
		Menu,SinglePlayer,MultiPlayer,Server
	}

	public static final String NAME = "Pixel Kingdom - the number of Commits is over 100 !";
	public static final String GAME_PATH = System.getenv("APPDATA") + "\\PixelKingdom\\";
	public static final int PORT = 7777;
	public static final double nsPerTick = 1000000000D/60D;
	public static int ticks = 0;
	public static int frames = 0;
	public static int fps = 0;
	
	private static Window window;
	
	private static boolean running = false;
	public static boolean reset = false;
	public static int tickCount = 0;
	public static boolean devmode;
	public static boolean pauseTick;
	public static GameMode gamemode;
	public static GameMenu menu;
	
	public static PixelList pixellist = new PixelList();
	public static ItemList itemlist = new ItemList();
	public static BiomeList biomelist = new BiomeList();
	
	public static PxlFont font;
	public static PxlFont sfont;
	public static PxlFont mfont;
	/**Coders Crux Font, Size: 10*18*/
	public static PxlFont ccFont;
	
	public static SinglePlayer singlePlayer;
	public static Client client;
	public static Server server;
	
	public static SpriteSheet back;
	
	public void init(){
		int width, height;
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
			width = vid.width();
			height = vid.height();
		}else {
			width = MainConfig.resX;
			height = MainConfig.resY;
		}
		window = new Window(width, height, MainConfig.fullscreen, "Pixel Kingdom", "/WindowIcon");
		
		window.init();
		
		new KeyInput(window.getWindow());
		new MouseInput(window.getWindow());
		glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		glfwSetWindowSizeLimits(window.getWindow(), 960, 720, GLFW_DONT_CARE, GLFW_DONT_CARE);

		Screen.MAP_ZOOM = MainConfig.mapZoom;
		Screen.initialize(width, height);
		
		menu = new MainMenu();
		mfont = new PxlFont(new SpriteSheet("/StackFont.png", 12, 15), "1234567890", 12, 15, -2);
		sfont = new PxlFont(new SpriteSheet("/8x8Font.png", 24, 24), " !\"# %&'()* ,-./0123456789:;|= ? ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{ }~",24,24, 1);
		font = new PxlFont(new SpriteSheet("/Font.png", 45, 60), "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 1234567890!\",;%&/()=?|+-.",45,60, 2);
		ccFont = new PxlFont(new SpriteSheet("/Font/coders_crux.png", 12, 20), "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890^´+#<,.-°!\"§$%&/()=?`*'>;:_²³{[]}\\~| ",12, 20, 2);
		
		back = new SpriteSheet("/Menu/NormalBack.png");
		
		gamemode = GameMode.Menu;
		//TODO TESTING AREA
	}
	
	public static void resizeWindow(int width, int height) {
		Screen.initialize(width, height);
		if(menu!=null)menu.refreshGUI();
		MainConfig.resX = width;
		MainConfig.resY = height;
	}
	
	public void reset(){
		gamemode = GameMode.Menu;
		Screen.xOffset=0;
		Screen.yOffset=0;
		Mouse.item = null;
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

	public static synchronized void stop() {
		running = false;
	}
	
	private long temp, messure, renderTime = 0, tickTime = 0, sleepTime = 0, sum;
	
	public void run() {
		long lastTime = System.nanoTime();
				
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		init();
		
		glClearColor(0f, 0f, 0f, 0f);
		while(running && !window.shouldClose()){
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			
			messure = System.currentTimeMillis();
			while(delta >= 1){
				ticks ++;
				if(!pauseTick)tick();
				window.update();
				delta -= 1;
			}
			temp = System.currentTimeMillis();
			tickTime += messure-temp;
			
			messure = System.currentTimeMillis();
			if(true){
				frames ++;
				render();
			}
			temp = System.currentTimeMillis();
			renderTime += messure-temp;
			
			if(System.currentTimeMillis() - lastTimer >= 1000){
				lastTimer += 1000;
//				System.out.println("Frames: " + frames + " ; Ticks: " + ticks);
				fps = frames;
				frames = 0;
				ticks = 0;
				
				if(SinglePlayer.debuginfo) {
					sum = renderTime + tickTime + sleepTime;
					Game.logInfo("sleep: " + ((int)(100*sleepTime/sum)) + "% tick: " + ((int)(100*tickTime/sum)) + "% render: " + ((int)(100*renderTime/sum)) + " %");
					renderTime = 0; tickTime = 0; sleepTime = 0;
				}
			}
		}
		if(gamemode == GameMode.SinglePlayer)singlePlayer.map.save();
		MainConfig.save();
		KeyConfig.save();
		Game.logInfo("Game shut down by User "+System.getProperty("user.name")+". ~Thanks 4 playing");
		System.exit(0);
	}
	
	public void tick(){
		tickCount++;
		if(reset)reset();
		Mouse.mouseType = Mouse.MouseType.DEFAULT;
		switch (gamemode){
		case Menu : 
			menu.tick();
			break;
		case SinglePlayer :
			singlePlayer.tick(tickCount);
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
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		switch (gamemode){
		case Menu :
			for (int x = 0; x < Screen.width; x+= back.getWidth()) {
				for (int y = 0; y < Screen.height; y+= back.getHeight()) {
					Screen.drawGUISprite(x, y, back);
				}
			}
			menu.render();
			break;
		case SinglePlayer :
			singlePlayer.render();
			break;
		case MultiPlayer :
			client.render();
			break;
		default : break;
		}
		
		Mouse.render();
		
		window.swapBuffers();
		
		RessourceManager.freeRessources();
	}
	
	public static void logError(Object text) {
		log(text, "Error", true);
	}
	public static void logWarning(Object text) {
		log(text, "Warning", false);
	}
	public static void logInfo(Object text) {
		log(text, "Info", false);
	}
	
	public static void log(Object text, String tag, boolean isError) {
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
		header+="["+tag+"]";
		if(isError){
			System.err.println(header+": "+text);
		}else{
			System.out.println(header+": "+text);
		}
	}
	
	private static void redirectStream() {
		System.setOut(new PrintStream(new OutputStream() {
			PrintStream old = System.out;
			public void write(int arg0) throws IOException {
				Server.log += (char) (arg0);
				Server.text.setText(Server.log);
				old.write(arg0);
			}
		}, false));
	}
	
	public static void main(String[] args){
		for(String s : args){
			if(s.contains("-devmode")){Game.devmode = true;Game.logInfo("Developer Mode activated !");}
			if(s.contains("-server")) {Game.gamemode = GameMode.Server;redirectStream();}
			Game.logInfo(s);
		}
		Game.logInfo("Current Java Version running : "+System.getProperty("java.version"));
		Game.logInfo("Current Operating System : "+System.getProperty("os.name"));
		if(Game.gamemode == GameMode.Server) {
			File dir = new File(Game.GAME_PATH+"maps"+File.separator+"Server"+File.separator);
			if(!dir.isDirectory()) {dir.mkdirs();}
			Game.server=new Server(Game.GAME_PATH+"maps"+File.separator+"Server");
			Thread t = new Thread(Game.server);
			t.setName("Server");
			t.start();
		}else {
			new Game().start();
		}
	}
}
