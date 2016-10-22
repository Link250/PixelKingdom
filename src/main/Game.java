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

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	
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
	private JFrame frame;
	private Image windowIcon;
	
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
	private SpriteSheet background;
	public SinglePlayer SinglePlayer;
	public Client client;
	public Server server;
	
	public Game(){
		Game.WIDTH = MainConfig.fullscreen ? frame.getWidth() : MainConfig.resX;
		Game.HEIGHT = MainConfig.fullscreen ? frame.getHeight() : MainConfig.resY;
		
		frame = new JFrame(NAME);
		if(MainConfig.fullscreen) {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			frame.setUndecorated(true);
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new java.awt.event.WindowAdapter(){
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent){
				if(gamemode == GameMode.SinglePlayer)SinglePlayer.map.save();
				if(server!=null)server.save();
			}
		});
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor cursor = toolkit.createCustomCursor(toolkit.getImage(""), new Point(0,0), ""); 
		setCursor(cursor);
		
		System.setProperty("sun.java2d.opengl","True");
		
	}
	
	public void init(){
		Window.setCallbacks();
		
		if(!glfwInit()) {
			System.err.println("GLFW Failed to initialize!");
			System.exit(1);
		}
		window = new Window(WIDTH, HEIGHT, false, "Pixel Kingdom");
		
		window.init();
		new KeyInput(window.getWindow());
		new MouseInput(window.getWindow());
		
		background = new SpriteSheet("/Back.png");
		
		try {windowIcon = ImageIO.read(SpriteSheet.class.getResourceAsStream("/WindowIcon.png"));} catch (IOException e) {e.printStackTrace();}
		frame.setIconImage(windowIcon);
		mfont = new PxlFont(new SpriteSheet("/StackFont.png", 12, 15), "1234567890", 12, 15, -2);
		sfont = new PxlFont(new SpriteSheet("/8x8Font.png", 24, 24), " !\"# %&'()* ,-./0123456789:; = ? ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{ }~",24,24, 1);
		font = new PxlFont(new SpriteSheet("/Font.png", 45, 60), "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 1234567890!\",;%&/()=?'+-.",45,60, 2);
		Screen.initialize(WIDTH, HEIGHT, csheetf, csheetm, csheetb);
		menu = new MainMenu(this);
		
		pixellist = new PixelList();
		itemlist = new ItemList();
		biomelist = new BiomeList();
		
		/*		TESTING AREA		*/
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

//		BufferStrategy bs = getBufferStrategy();
//		if(bs == null){
//			createBufferStrategy(2);
//			return;
//		}
//		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

//		screen.resetPixelAll();
		
		switch (gamemode){
		case Menu :
//			g.drawImage(back, 0, 0, WIDTH, HEIGHT, null);
			Screen.drawTileOGL(0, 0, 0, background);
//			  7,292E-1  0,000E+0 -0,000E+0 -2,708E-1
//			  0,000E+0  9,722E-1 -0,000E+0  2,778E-2
//			  0,000E+0  0,000E+0  7,000E+2  0,000E+0
//			  0,000E+0  0,000E+0  0,000E+0  1,000E+0
//			?  0,000E+0 -0,000E+0?
//					?  9,736E-1 -0,000E+0?
//					?  0,000E+0  7,010E+2?
//					  0,000E+0  0,000E+0  0,000E+0  1,000E+0
//			g.drawImage(back, AffineTransform.getRotateInstance(Math.PI, back.getWidth()/2, back.getHeight()/2), null);
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
		
//		for(int xy = 0; xy < screen.length; xy++){
//			pxsGUI[xy] = screen.GUI[xy];
//		}
//		for(int xy = 0; xy < screen.lengthMap; xy++){
//			pxsMain[xy] = screen.pixels[xy];
//		}
//		for(int xy = 0; xy < screen.lengthShadow; xy++){
//			pxsShadow[xy] = screen.shadow[xy];
//		}
//		
//		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
//		g.drawImage(shadow, 0, 0, WIDTH, HEIGHT, null);
//		g.drawImage(GUI, 0, 0, WIDTH, HEIGHT, null);
//		g.dispose();
//		bs.show();
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
		for(String s : args){if(s.contains("-devmode")){Game.devmode = true;Game.logInfo("Developer Mode activated !");}}
		
		File gameDir = new File(Game.GAME_PATH);
		if(!gameDir.isDirectory()) {gameDir.mkdirs();Game.logInfo("Created PixelKingdom Path");}
		MainConfig.load();
		KeyConfig.load();
		Screen.MAP_ZOOM = MainConfig.mapZoom;
		for(String s : args){
			Game.logInfo(s);
		}
		new Game().start();
	}
}
