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
				}
			}
		} catch (IOException e) {
			running = false;
			server.closeConnection(id);
		}
	}
	
	public void sendToClient(byte[] b, String reason) throws IOException{
		clientOut.write(b);
		clientOut.flush();
		System.out.println("sent b array for "+reason);
	}

	public void sendToClient(int i, String reason) throws IOException{
//		byte[] b = {(byte) (i>>24),(byte) (i>>16),(byte) (i>>8),(byte) i};
//		sendToClient(b);
		clientOut.write(i);
		clientOut.flush();
		System.out.println("sent int for "+reason);
	}
	
	public void closeConnection() {
		try {
			clientOut.close();
			clientIn.close();
		} catch (IOException e) {e.printStackTrace();}
	}
}
