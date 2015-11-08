package Main;

import gfx.ColorSheet;
import gfx.PxlFont;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import GUI.Button;
import Items.ItemList;
import Maps.BiomeList;
import Maps.MapSelection;
import Main.InputHandler;
import Multiplayer.Client;
import Multiplayer.Server;
import Pixels.PixelList;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;

	public static int WIDTH;
	public static int HEIGHT;
	public static final int SCALE = 3;
	public static final String NAME = "Pixel Kingdom";
	public static final String GAME_PATH = System.getenv("APPDATA") + "\\PixelKingdom\\";
	public static final int PORT = 7777;
	public static final double nsPerTick = 1000000000D/60D;
	static int ticks = 0;
	static int frames = 0;
	public static int fps = 0;
	
	private JFrame frame;
	
	public boolean running = false;
	public static boolean reset = false;
	public int tickCount = 0;
	public static boolean devmode;
	public static int gamemode = 0;
	public static int menu = 0;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private int[] pxsMain = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	private BufferedImage shadow = new BufferedImage(WIDTH/3, HEIGHT/3, BufferedImage.TYPE_INT_ARGB);
	private int[] pxsShadow = ((DataBufferInt)shadow.getRaster().getDataBuffer()).getData();
	private BufferedImage GUI = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private int[] pxsGUI = ((DataBufferInt)GUI.getRaster().getDataBuffer()).getData();
	
	public static Configs configs;
	public static ItemList itemlist;
	public static PixelList pixellist;
	public static BiomeList biomelist;
	public static ColorSheet csheetf = new ColorSheet("/Mat_Front.png");
	public static ColorSheet csheetm = new ColorSheet("/Mat_Mid.png");
	public static ColorSheet csheetb = new ColorSheet("/Mat_Back.png");
	public Screen screen;
	private BufferedImage back = null;
	public InputHandler input;
	public static PxlFont font;
	public static PxlFont sfont;
	public static PxlFont mfont;
	private Button SP, MP, OP, QT;
	public SinglePlayer SinglePlayer;
	public Client client;
	public Server server;
	public ServerList ServerList;
	public MapSelection MapSelect;
	public OptionScreen OptionScreen;
	
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
				if(gamemode == 1)SinglePlayer.map.save();
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
		input = new InputHandler(this);
		mfont = new PxlFont(new SpriteSheet("/StackFont.png"), "1234567890", 4*Game.SCALE, 5*Game.SCALE);
		sfont = new PxlFont(new SpriteSheet("/8x8Font.png"), " !\"# %&´()* ,-./0123456789:; = ? ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{ }~",8*Game.SCALE,8*Game.SCALE);
		font = new PxlFont(new SpriteSheet("/Font.png"), "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 1234567890!\",;%&/()=?ß+-.",15*Game.SCALE,20*Game.SCALE);
		screen = new Screen(WIDTH, HEIGHT, csheetf, csheetm, csheetb);
		SP = new Button(WIDTH/Game.SCALE/2-50,110,100,20,screen,input);
		SP.gfxData(new SpriteSheet("/Buttons/SP.png"), true);
		MP = new Button(WIDTH/Game.SCALE/2-50,140,100,20,screen,input);
		MP.gfxData(new SpriteSheet("/Buttons/MP.png"), true);
		OP = new Button(WIDTH/Game.SCALE/2-50,170,100,20,screen,input);
		OP.gfxData(new SpriteSheet("/Buttons/OP.png"), true);
		QT = new Button(WIDTH/Game.SCALE/2-50,200,100,20,screen,input);
		QT.gfxData(new SpriteSheet("/Buttons/QT.png"), true);
		
		pixellist = new PixelList();
		itemlist = new ItemList();
		biomelist = new BiomeList();
		
		/*		TESTING AREA		*/
	}
	
	public void reset(){
		gamemode = 0;
		menu = 0;
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
		configs.save();
		Game.logInfo("Game shut down by User "+System.getProperty("user.name")+". ~Thanks 4 playing");
		System.exit(0);
	}
	
	public void tick(){
		tickCount++;
		if(reset)reset();
		switch (gamemode){
		case 0 : 
			switch (menu){
			case 0 :
				SP.tick();
				if(SP.isclicked){
					MapSelect = new MapSelection(this);
					menu = 1;
				}
				MP.tick();
				if(MP.isclicked){
					ServerList = new ServerList(this);
					menu = 2;
				}
				OP.tick();
				if(OP.isclicked){
					OptionScreen = new OptionScreen(this);
					menu = 3;
				}
				QT.tick();
				if(QT.isclicked){
					running = false;
				}
				break;
			case 1 :
				MapSelect.tick();
				break;
			case 2 :
				ServerList.tick();
				break;
			case 3 :
				OptionScreen.tick();
				break;
			}
			break;
		case 1 :
			SinglePlayer.tick(tickCount);
			break;
		case 2 :
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
		Graphics g = bs.getDrawGraphics();
		g.drawRect(0, 0, getWidth(), getHeight());

		for(int y = 0; y < screen.height; y++){
			for(int x = 0; x < screen.width; x++){
				screen.resetPixel(x, y);
			}
		}
		switch (gamemode){
		case 0 :
			g.drawImage(back, 0, 0, WIDTH, HEIGHT, null);
			switch (menu){
			case 0 :
				SP.render();
				MP.render();
				OP.render();
				QT.render();
				break;
			case 1 :
				MapSelect.render();
				break;
			case 2 :
				ServerList.render();
				break;
			case 3 :
				OptionScreen.render();
				break;
			}
			break;
		case 1 :
			SinglePlayer.render(g);
			break;
		case 2 :
			client.render(g);
			break;
		default : break;
		}		
		
		Mouse.render(this);
		
		for(int y = 0; y < screen.height; y++){
			for(int x = 0; x < screen.width; x++){
				pxsMain[x + y * WIDTH] = screen.pixels[x + y * screen.width];
				if(x < screen.width/3 & y < screen.height/3)pxsShadow[x + y * WIDTH/3] = screen.shadow[x + y * screen.width/3];
				pxsGUI[x + y * WIDTH] = screen.GUI[x + y * screen.width];
			}
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
		Game.configs = new Configs();
		Game.WIDTH = 320*3;//configs.resX;
		Game.HEIGHT = WIDTH/12*9;//configs.resY;
		for(String s : args){
			Game.logInfo(s);
		}
		new Game().start();
	}
}
