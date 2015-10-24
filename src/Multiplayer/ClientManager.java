package Multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import entities.MPlayer;

public class ClientManager implements Runnable {
	
	boolean running = true;
	InputStream clientIn;
	OutputStream clientOut;
	Server server;
	public MPlayer player;
	int id;
	
	public ClientManager(Socket c, Server s, int n){
		server = s;
		player = new MPlayer(null, null, n);
		try {
			id = n;
			clientIn = c.getInputStream();
			clientOut = c.getOutputStream();
		} catch (IOException e) {}
	}

	public void run() {
		int in;
		try {
			while (running){
				in = clientIn.read();
				switch(in) {
				case Request.CLOSE_CONNECTION:
					running = false;
					server.closeConnection(id);
					break;
				case Request.CHUNK_DATA:
					server.sendChunk(this, clientIn);
					break;
				case Request.PLAYER_DATA:
					server.receivePlayerData(this, clientIn);
					break;
				case Request.MAP_DATA:
					server.receiveMapData(this, clientIn);
					break;
				default:
					System.err.println("Non existing request received with ID "+in);
					break;
				}
			}
		} catch (IOException e) {
			running = false;
			server.closeConnection(id);
		}
	}
	
	public void send2Client(int i) throws IOException{
		clientOut.write(i);
		clientOut.flush();
	}
	
	public void send2Client(byte[] b) throws IOException{
		clientOut.write(b);
		clientOut.flush();
	}
	
	public void sendRequest2Client(byte[] b, byte request) throws IOException{
		byte[] d = new byte[b.length+1];
		d[0]=request;
		for(int i = 0; i < b.length; i++)d[i+1]=b[i];
		clientOut.write(d);
		clientOut.flush();
	}
	
	public void sendRequest2Client(byte[] b, byte request, byte sub) throws IOException{
		byte[] d = new byte[b.length+2];
		d[0]=request;
		d[1]=sub;
		for(int i = 0; i < b.length; i++)d[i+2]=b[i];
		clientOut.write(d);
		clientOut.flush();
	}
	
	public void closeConnection() {
		try {
			clientOut.close();
			clientIn.close();
		} catch (IOException e) {e.printStackTrace();}
	}
}
