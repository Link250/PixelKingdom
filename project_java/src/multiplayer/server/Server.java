package multiplayer.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import main.Game;
import main.Keys;
import main.MouseInput;
import map.Map;
import multiplayer.MapManager;
import multiplayer.MapUpdater;

public class Server implements Runnable{
	boolean running = true;
	
	private ServerSocket serverSocket;
	private ClientsManager clientsManager;
	private Map map;
	private MapManager mapManager;
	
	private JFrame frame = new JFrame();
	public static JTextPane text = new JTextPane();
	public static String log = "";
	
	public Server(String files) {
		initFrame();
		try {
			serverSocket = new ServerSocket(Game.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientsManager = new ClientsManager(this,serverSocket);
		
		map = new Map(files);
		mapManager = new MapManager(new MapUpdater(map, Map.GT_SERVER), clientsManager);
		clientsManager.addInputReceiver(mapManager);
		clientsManager.addInputReceiver(new PlayerManager(clientsManager));
		clientsManager.addInputReceiver(new ChunkManagerS(clientsManager, map));
	}
	
	private void initFrame(){
		frame.setPreferredSize(new Dimension(800, 500));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.pack();

		text.setEditable(false);
		text.setBackground(new Color(0xff000000));
		text.setForeground(new Color(0xff00ff00));
		text.setText(log);
		text.setFont(new Font("Consolas", 0, 14));
		frame.add(text);
		
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setFocusable(true);
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		int ticks = 0;
		
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / Game.nsPerTick;
			lastTime = now;
			
			while(delta >= 1){
				ticks ++;
				tick(ticks);
				delta -= 1;
			}
			
			if(System.currentTimeMillis() - lastTimer >= 1000){
				if(Game.devmode && map.updateCountPixel>0)
					Game.logInfo("Ticks:"+ticks+" BlockUpdates:"+map.updateCountPixel);
				map.updateCountPixel=0;
				lastTimer += 1000;
				ticks = 0;
			}
		}
		save();
		Game.logInfo("Server shut down by User "+System.getProperty("user.name")+". ~Thanks 4 playing");
	}
	
	public void tick(int ticks){
		map.tick(ticks);
		
		try {
			mapManager.sendMapUpdates();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(Game.devmode&&Keys.DEBUGPXL.click()) {
			int X = MouseInput.mouse.getMapX(), Y = MouseInput.mouse.getMapY();
			Game.logInfo(X+" "+Y+" id{"+map.getID(X, Y, 1)		+","+map.getID(X, Y, 2)		+","+map.getID(X, Y, 3)		+"}");
			Game.logInfo(X+" "+Y+" up{"+map.isUpdating(X, Y, 0)	+","+map.isUpdating(X, Y, 0)+","+map.isUpdating(X, Y, 0)+"}");
			map.addPixelUpdate(X, Y, 1);
		}
	}
	
	public void save() {
		map.save();
	}
}
