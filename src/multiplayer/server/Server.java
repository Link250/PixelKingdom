package multiplayer.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import main.Game;
import main.conversion.ConvertData;
import main.conversion.ConverterInStream;
import main.conversion.IOConverter;
import map.Map;
import multiplayer.Request;

public class Server implements Runnable{
	boolean running = true;
	public static ArrayList<ClientManager> clients = new ArrayList<ClientManager>();
	Game game;
	Map map;
	
	public Server(Game g, String files) {
		game = g;
		map = new Map(files, g.screen);
		map.setGametype(Map.GT_SERVER);
	}
	
	public void run() {
		Thread t = new Thread(new ClientAccepter(this));
		t.setName("ClientAccepter");t.start();
		Game.logInfo("Client accepter started !");
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
		map.sendMapUpdates(ticks);
		if(Game.devmode&&game.input.X.click()) {
			int X = game.input.mouse.x/Game.SCALE+game.screen.xOffset, Y = game.input.mouse.y/Game.SCALE+game.screen.yOffset;
			Game.logInfo(X+" "+Y+" id{"+map.getID(X, Y, 1)		+","+map.getID(X, Y, 2)		+","+map.getID(X, Y, 3)		+"}");
			Game.logInfo(X+" "+Y+" up{"+map.isUpdating(X, Y, 0)	+","+map.isUpdating(X, Y, 0)+","+map.isUpdating(X, Y, 0)+"}");
			map.addPixelUpdate(X, Y, 1);
		}
	}
	
	public void save() {
		map.save();
	}
	
	public void sendChunk(ClientManager c, InputStream cIn) throws IOException {
		int x = IOConverter.receiveInt(cIn);
		int y = IOConverter.receiveInt(cIn);
		while(!map.loadChunk(x, y)) {try {Thread.sleep(1);} catch (InterruptedException e) {}}
		Game.logInfo("sending chunk");
		byte[] mapd = map.compressedChunk(x, y);
		c.send2Client(new byte[][]{new byte[]{Request.MAP_CHUNK_DATA},ConvertData.I2B(mapd.length),mapd});
	}
	
	public void receivePlayerColor(ClientManager c, InputStream cIn) throws IOException {
		byte[] data = new byte[5];
		ArrayList<Byte> temp = new ArrayList<Byte>();
		try {for(int i = 0; i < 4; i ++)temp.add((byte) cIn.read());} catch (IOException e) {}
		for(int i = 0; i < temp.size(); i++)data[i+1]=temp.get(i);
		c.player.setColor(ConvertData.B2I(temp));
		data[0]=(byte) c.id;
		
		for(ClientManager cm : clients) {
			if(cm!=c) {
				cm.sendRequest2Client(data,Request.PLAYER_DATA,Request.PLAYER.COLOR);
			}
		}
	}
	
	public void receivePlayerData(ClientManager c, InputStream cIn) throws IOException {
		byte[] data = new byte[11];
		ArrayList<Byte> temp = new ArrayList<Byte>();
		try {for(int i = 0; i < 10; i ++)temp.add((byte) cIn.read());} catch (IOException e) {}
		for(int i = 0; i < temp.size(); i++)data[i+1]=temp.get(i);
		c.player.x = ConvertData.B2I(temp);
		c.player.y = ConvertData.B2I(temp);
		c.player.anim = temp.remove(0);
		c.player.setDir(temp.remove(0));
		data[0]=(byte) c.id;
		
		for(ClientManager cm : clients) {
			if(cm!=c) {
				cm.sendRequest2Client(data,Request.PLAYER_DATA,Request.PLAYER.REFRESH);
			}
		}
	}
	
	public void receiveMapData(ClientManager c, ConverterInStream cIn) throws IOException {
		map.receiveMapUpdates(cIn);
	}
	
	public static void sendMapData(byte[][] b) throws IOException {
		for(ClientManager cm : clients) {
			cm.send2Client(b);
		}
	}

	/**
	 * @deprecated
	 * */
	public static void sendMapData(int x, int y, int l, int ID) throws IOException {
		byte[] data = new byte[11];
		ArrayList<Byte> temp = new ArrayList<Byte>();
		ConvertData.I2B(temp, x);
		ConvertData.I2B(temp, y);
		temp.add((byte) l);
		ConvertData.S2B(temp, (short) ID);
		for(int i = 0; i < temp.size(); i++)data[i]=temp.get(i);
		for(ClientManager cm : clients) {
			cm.sendRequest2Client(data, Request.MAP_DATA);
		}
	}
	
	public void newConnection(int c, Socket s) throws IOException {
		sendClients(Request.PLAYER_DATA);
		sendClients(Request.PLAYER.NEW);
		sendClients(c);
		Game.logInfo("new connection");
		clients.add(new ClientManager(s, this, c));
		for(ClientManager cm : clients) {
			if(cm.id==c) {
				for(ClientManager cmt : clients) {
					if(cmt.id!=c) {
						cm.send2Client(new byte[]{Request.PLAYER_DATA,Request.PLAYER.NEW,(byte) cmt.id});
						byte[] temp = new byte[7];
						temp[0]=Request.PLAYER_DATA;
						temp[1]=Request.PLAYER.COLOR;
						temp[2]=(byte) cmt.id;
						ConvertData.I2B(temp, 3, cmt.player.color);
						cm.send2Client(temp);
					}
				}
			}
		}
	}
	
	public void closeConnection(int id){
		ClientManager c = getClient(id);
		try {
			c.send2Client(new byte[]{Request.CLOSE_CONNECTION});
			c.closeConnection();
			clients.remove(c);
			sendClients(Request.PLAYER_DATA);
			sendClients(Request.PLAYER.DELETE);
			sendClients(id);
			Game.logInfo("Client "+id+" disconnected");
		} catch (IOException e) {
			Game.logError("Client "+id+" disconnected by error");
		}
		map.save();
	}
	
	@Deprecated
	public void sendClients(int i) throws IOException{
		for(ClientManager c : clients){
			c.send2Client(i);
		}
	}
	public void sendClients(byte[] b) throws IOException{
		for(ClientManager c : clients){
			c.send2Client(b);
		}
	}
	public void sendClients(byte[][] b) throws IOException{
		for(ClientManager c : clients){
			c.send2Client(b);
		}
	}
	
	public ClientManager getClient(int id) {
		for(int i = 0; i < clients.size(); i++)if(clients.get(i).id==id)return clients.get(i);
		return null;
	}
}
