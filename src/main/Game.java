package main;

import gfx.ColorSheet;
import gfx.PxlFont;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;
import gui.menu.MainMenu;
import gui.menu.Menu;
import item.ItemList;
import main.InputHandler;
import map.BiomeList;
import multiplayer.client.Client;
import multiplayer.server.Server;
import pixel.PixelList;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
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
	public static final int SCALE = 3;
	public static final String NAME = "Pixel Kingdom - the number of Commits is over 100 !";
	public static final String GAME_PATH = System.getenv("APPDATA") + "\\PixelKingdom\\";
	public static final int PORT = 7777;
	public static final double nsPerTick = 1000000000D/60D;
	public static int ticks = 0;
	public static int frames = 0;
	public static int fps = 0;
	
	private JFrame frame;
	private Image windowIcon;
	
	private boolean running = false;
	public static boolean reset = false;
	public int tickCount = 0;
	public static boolean devmode;
	public static GameMode gamemode = GameMode.Menu;
	public static MainMenu menu;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private int[] pxsMain = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	private BufferedImage shadow = new BufferedImage(WIDTH/3, HEIGHT/3, BufferedImage.TYPE_INT_ARGB);
	private int[] pxsShadow = ((DataBufferInt)shadow.getRaster().getDataBuffer()).getData();
	private BufferedImage GUI = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private int[] pxsGUI = ((DataBufferInt)GUI.getRaster().getDataBuffer()).getData();
	
	public static ItemList itemlist;
	public static PixelList pixellist;
	public static BiomeList biomelist;
	public static ColorSheet csheetf = new ColorSheet("/Mat_Front.png");
	public static ColorSheet csheetm = new ColorSheet("/Mat_Mid.png");
	public static ColorSheet csheetb = new ColorSheet("/Mat_Back.png");
	public static Screen screen;
	public static InputHandler input;
	public static PxlFont font;
	public static PxlFont sfont;
	public static PxlFont mfont;
	private BufferedImage back = null;
	public SinglePlayer SinglePlayer;
	public Client client;
	public Server server;
	
	public Game(){
		setMinimumSize(new Dimension(WIDTH-12, HEIGHT-12));
		setMaximumSize(new Dimension(WIDTH-12, HEIGHT-12));
		setPreferredSize(new Dimension(WIDTH-12, HEIGHT-12));
		
		frame = new JFrame(NAME);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
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
		try {back = ImageIO.read(SpriteSheet.class.getResourceAsStream("/Back.png"));} catch (IOException e) {e.printStackTrace();}
		try {windowIcon = ImageIO.read(SpriteSheet.class.getResourceAsStream("/WindowIcon.png"));} catch (IOException e) {e.printStackTrace();}
		frame.setIconImage(windowIcon);
		input = new InputHandler(this);
		mfont = new PxlFont(new SpriteSheet("/StackFont.png"), "1234567890", 4*Game.SCALE, 5*Game.SCALE);
		sfont = new PxlFont(new SpriteSheet("/8x8Font.png"), " !\"# %&´()* ,-./0123456789:; = ? ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{ }~",8*Game.SCALE,8*Game.SCALE);
		font = new PxlFont(new SpriteSheet("/Font.png"), "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 1234567890!\",;%&/()=?ß+-.",15*Game.SCALE,20*Game.SCALE);
		screen = new Screen(WIDTH, HEIGHT, csheetf, csheetm, csheetb);
		menu = new MainMenu(this);
		
		pixellist = new PixelList();
		itemlist = new ItemList();
		biomelist = new BiomeList();
		
		/*		TESTING AREA		*/
	}
	
	public void reset(){
		gamemode = GameMode.Menu;
		menu.subMenu = Menu.MainMenu;
		screen.xOffset=0;
		screen.yOffset=0;
		Mouse.Item = null;
		Mouse.mousetype=0;
		if(client!=null)client.reset();
		for(int y = 0; y < screen.height/3; y++){
			for(int x = 0; x < screen.width/3; x++){
				screen.shadow[x + y * screen.width/3] = 0;
			}
		}
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
		
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			
			while(delta >= 1){
				ticks ++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			
			if(shouldRender){
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
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(2);
			return;
		}
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		screen.resetPixelAll();
		
		switch (gamemode){
		case Menu :
			g.drawImage(back, 0, 0, WIDTH, HEIGHT, null);
//			g.drawImage(back, AffineTransform.getRotateInstance(Math.PI, back.getWidth()/2, back.getHeight()/2), null);
			menu.render();
			break;
		case SinglePlayer :
			SinglePlayer.render(g);
			break;
		case MultiPlayer :
			client.render(g);
			break;
		default : break;
		}
		
		Mouse.render();
		
		for(int xy = 0; xy < screen.length; xy++){
			pxsMain[xy] = screen.pixels[xy];
			pxsGUI[xy] = screen.GUI[xy];
		}
		for(int xy = 0; xy < screen.lengthShadow; xy++){
			pxsShadow[xy] = screen.shadow[xy];
		}
		
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		g.drawImage(shadow, 0, 0, WIDTH, HEIGHT, null);
		g.drawImage(GUI, 0, 0, WIDTH, HEIGHT, null);
		g.dispose();
		bs.show();
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
		Game.WIDTH = 320*3;//configs.resX;
		Game.HEIGHT = WIDTH/12*9;//configs.resY;
		for(String s : args){
			Game.logInfo(s);
		}
		new Game().start();
	}
}
