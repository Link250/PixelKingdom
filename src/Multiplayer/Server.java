package Multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

import Main.ConvertData;
import Main.Game;
import Main.IOConverter;
import Maps.Map;

public class Server implements Runnable{
	boolean running = true;
	public static ArrayList<ClientManager> clients = new ArrayList<ClientManager>();
	Game game;
	Map map;
	
	public Server(Game g, String files) {
		game = g;
		map = new Map(files, g.screen);
		map.setGametype(Map.GT_SERVER);
		map.loadChunk(512*1024, 512*1024);
	}
	
	public void run() {
		Thread t = new Thread(new ClientAccepter(this));
		t.setName("ClientAccepter");t.start();
		System.out.println("Client accepter started !");
		while(running){try{
			Thread.sleep(10);
		}catch(InterruptedException e){}}
	}
	
	public void tick(int tickCount){
		map.tick(tickCount);
		map.sendMapUpdates(tickCount);
	}
	
	public void sendChunk(ClientManager c, InputStream cIn) throws IOException {
		int x = IOConverter.receiveInt(cIn);
		int y = IOConverter.receiveInt(cIn);
		while(!map.loadChunk(x*1024, y*1024)) {try {Thread.sleep(1);} catch (InterruptedException e) {}}
		System.out.println("sending chunk");
		byte[] mapd = map.compressedChunk(x, y);
		c.send2Client(new byte[]{Request.CHUNK_DATA});
		c.send2Client(ConvertData.I2B(mapd.length));
		c.send2Client(mapd);
	}
	
	public void receivePlayerData(ClientManager c, InputStream cIn) throws IOException {
		byte[] data = new byte[9];
		ArrayList<Byte> temp = new ArrayList<Byte>();
		try {for(int i = 0; i < 8; i ++)temp.add((byte) cIn.read());} catch (IOException e) {}
		for(int i = 0; i < temp.size(); i++)data[i+1]=temp.get(i);
		c.player.x = ConvertData.B2I(temp);
		c.player.y = ConvertData.B2I(temp);
		data[0]=(byte) c.id;
		
		for(ClientManager cm : clients) {
			if(cm!=c) {
				cm.sendRequest2Client(data,Request.PLAYER_DATA,Request.PLAYER.REFRESH);
			}
		}
	}
	
	public void receiveMapData(ClientManager c, InputStream cIn) throws IOException {
//		byte[] data = new byte[11];
//		ArrayList<Byte> temp = new ArrayList<Byte>();
//		try {for(int i = 0; i < 11; i ++)temp.add((byte) cIn.read());} catch (IOException e) {}
//		for(int i = 0; i < temp.size(); i++)data[i]=temp.get(i);
//		map.setID(ConvertData.B2I(temp), ConvertData.B2I(temp), temp.remove(0), ConvertData.B2S(temp), null, false);
		map.setID(IOConverter.receiveInt(cIn), IOConverter.receiveInt(cIn), cIn.read(), IOConverter.receiveShort(cIn), null, false);
//		System.out.println("got map data");
		
//		for(ClientManager cm : clients) {
//			if(cm!=c) {
//				cm.sendRequest2Client(data,Request.MAP_DATA);
//			}
//		}
	}
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
		System.out.println("new connection");
		clients.add(new ClientManager(s, this, c));
		for(ClientManager cm : clients) {
			if(cm.id==c) {
				for(ClientManager cmt : clients) {
					if(cmt.id!=c) {
						cm.send2Client(new byte[]{Request.PLAYER_DATA,Request.PLAYER.NEW,(byte) cmt.id});
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
			System.out.println("Client "+id+" disconnected");
		} catch (IOException e) {
			System.out.println("Client "+id+" disconnected by error");
		}
	}
	
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
	
	public ClientManager getClient(int id) {
		for(int i = 0; i < clients.size(); i++)if(clients.get(i).id==id)return clients.get(i);
		return null;
	}
}
