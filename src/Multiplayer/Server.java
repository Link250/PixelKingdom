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
	}
	
	public void sendChunk(ClientManager c, InputStream cIn) throws IOException {
		int x = IOConverter.receiveInt(cIn);
		int y = IOConverter.receiveInt(cIn);
		while(!map.loadChunk(x*1024, y*1024)) {try {Thread.sleep(1);} catch (InterruptedException e) {}}
		System.out.println("sending chunk");
		byte[] mapd = map.compressedChunk(x, y);
		c.sendToClient(Request.CHUNK_DATA);
		c.sendToClient(ConvertData.I2B(mapd.length));
		c.sendToClient(mapd);
	}
	
	public void receivePlayerData(ClientManager c, InputStream cIn) throws IOException {
		byte[] data = new byte[8];
		ArrayList<Byte> temp = new ArrayList<Byte>();
		try {for(int i = 0; i < 8; i ++)temp.add((byte) cIn.read());} catch (IOException e) {}
		for(int i = 0; i < temp.size(); i++)data[i]=temp.get(i);
		c.player.x = ConvertData.B2I(temp);
		c.player.y = ConvertData.B2I(temp);
		 
		for(ClientManager cm : clients) {
			if(cm!=c) {
				cm.sendToClient(Request.PLAYER_DATA);
				cm.sendToClient(3);
				cm.sendToClient(c.id);
				cm.sendToClient(data);
			}
		}
	}
	
	public void receiveMapData(ClientManager c, InputStream cIn) throws IOException {
		byte[] data = new byte[16];
		ArrayList<Byte> temp = new ArrayList<Byte>();
		try {for(int i = 0; i < 16; i ++)temp.add((byte) cIn.read());} catch (IOException e) {}
		for(int i = 0; i < temp.size(); i++)data[i]=temp.get(i);
		map.setID(ConvertData.B2I(temp), ConvertData.B2I(temp), ConvertData.B2I(temp), ConvertData.B2I(temp), null, true);
//		System.out.println("got map data");
		
		for(ClientManager cm : clients) {
			if(cm!=c) {
				cm.sendToClient(Request.MAP_DATA);
				cm.sendToClient(data);
			}
		}
	}
	public static void sendMapData(int x, int y, int l, int ID) throws IOException {
		byte[] data = new byte[16];
		ArrayList<Byte> temp = new ArrayList<Byte>();
		ConvertData.I2B(temp, x);
		ConvertData.I2B(temp, y);
		ConvertData.I2B(temp, l);
		ConvertData.I2B(temp, ID);
		for(int i = 0; i < temp.size(); i++)data[i]=temp.get(i);
		for(ClientManager cm : clients) {
				cm.sendToClient(Request.MAP_DATA);
				cm.sendToClient(data);
		}
	}
	
	public void newConnection(int c, Socket s) throws IOException {
		sendClients(Request.PLAYER_DATA);
		sendClients(1);
		sendClients(c);
		System.out.println("new connection");
		clients.add(new ClientManager(s, this, c));
		for(ClientManager cm : clients) {
			if(cm.id==c) {
				for(ClientManager cmt : clients) {
					if(cmt.id!=c) {
						cm.sendToClient(Request.PLAYER_DATA);
						cm.sendToClient(1);
						cm.sendToClient(cmt.id);
					}
				}
			}
		}
	}
	
	public void closeConnection(int id){
		ClientManager c = getClient(id);
		try {
			c.sendToClient(Request.CLOSE_CONNECTION);
			c.closeConnection();
			clients.remove(c);
			sendClients(Request.PLAYER_DATA);
			sendClients(2);
			sendClients(id);
			System.out.println("Client "+id+" disconnected");
		} catch (IOException e) {
			System.out.println("Client "+id+" disconnected by error");
		}
	}
	
	public void sendClients(int i) throws IOException{
		for(ClientManager c : clients){
			c.sendToClient(i);
		}
	}
	public void sendClients(byte[] b) throws IOException{
		for(ClientManager c : clients){
			c.sendToClient(b);
		}
	}
	
	public ClientManager getClient(int id) {
		for(int i = 0; i < clients.size(); i++)if(clients.get(i).id==id)return clients.get(i);
		return null;
	}
}
