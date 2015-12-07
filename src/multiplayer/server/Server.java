package multiplayer.server;

import java.io.IOException;
import java.net.ServerSocket;
import main.Game;
import map.Map;
import multiplayer.MapManager;
import multiplayer.MapUpdater;

public class Server implements Runnable{
	boolean running = true;
	
	private ServerSocket serverSocket;
	private ClientsManager clientsManager;
	private Map map;
	private MapManager mapManager;
	
	public Server(String files) {
		try {
			serverSocket = new ServerSocket(Game.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientsManager = new ClientsManager(this,serverSocket);
		
		map = new Map(files, Game.screen);
		mapManager = new MapManager(new MapUpdater(map, Map.GT_SERVER), clientsManager);
		clientsManager.addInputReceiver(mapManager);
		clientsManager.addInputReceiver(new PlayerManager(clientsManager));
		clientsManager.addInputReceiver(new ChunkManagerS(clientsManager, map));
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
		map.save();
	}
	
	public void tick(int ticks){
		map.tick(ticks);
		
		try {
			mapManager.sendMapUpdates();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(Game.devmode&&Game.input.X.click()) {
			int X = Game.input.mouse.x/Game.SCALE+Game.screen.xOffset, Y = Game.input.mouse.y/Game.SCALE+Game.screen.yOffset;
			Game.logInfo(X+" "+Y+" id{"+map.getID(X, Y, 1)		+","+map.getID(X, Y, 2)		+","+map.getID(X, Y, 3)		+"}");
			Game.logInfo(X+" "+Y+" up{"+map.isUpdating(X, Y, 0)	+","+map.isUpdating(X, Y, 0)+","+map.isUpdating(X, Y, 0)+"}");
			map.addPixelUpdate(X, Y, 1);
		}
	}
	
	public void save() {
		map.save();
	}
}
