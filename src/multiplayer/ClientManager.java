package multiplayer;

import java.io.IOException;
import java.net.Socket;

import entities.MPlayer;
import main.Game;
import multiplayer.conversion.ConverterInStream;
import multiplayer.conversion.ConverterOutStream;

public class ClientManager implements Runnable {
	
	boolean running = true;
	ConverterInStream clientIn;
	ConverterOutStream clientOut;
	Server server;
	public MPlayer player;
	int id;
	
	public ClientManager(Socket c, Server s, int n){
		server = s;
		player = new MPlayer(null, null, n);
		try {
			id = n;
			clientIn = new ConverterInStream(c.getInputStream());
			clientOut = new ConverterOutStream(c.getOutputStream());
		} catch (IOException e) {e.printStackTrace();}
	}

	public void run() {
		int in;
		try {
			while (running){
				in = clientIn.read();
				switch(in) {
				case Request.END_OF_STREAM:
				case Request.CLOSE_CONNECTION:
					running = false;
					server.closeConnection(id);
					break;
				case Request.PLAYER_DATA:
					server.receivePlayerData(this, clientIn);
					break;
				case Request.PLAYER_COLOR:
					server.receivePlayerColor(this, clientIn);
					break;
				case Request.MAP_DATA:
					server.receiveMapData(this, clientIn);
					break;
				case Request.MAP_CHUNK_DATA:
					server.sendChunk(this, clientIn);
					break;
				default:
					Game.logError("Non existing request received with ID "+in);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			running = false;
			server.closeConnection(id);
		}
	}
	
	public synchronized void send2Client(int i) throws IOException{
		clientOut.write(i);
		clientOut.flush();
	}
	
	public synchronized void send2Client(byte[] b) throws IOException{
		clientOut.write(b);
		clientOut.flush();
	}
	
	public synchronized void send2Client(byte[][] b, int l) throws IOException{
		byte[] d = new byte[l];l=0;
		for (int x = 0; x < b.length; x++) {
			for (int y = 0; y < b[x].length; y++) {
				d[l]=b[x][y];l++;
			}
		}
		clientOut.write(d);
		clientOut.flush();
	}
	
	public synchronized void send2Client(byte[][] b) throws IOException{
		int n = 0;
		for (int i = 0; i < b.length; i++)
			n += b[i].length;
		byte[] d = new byte[n]; n=0;
		for (int x = 0; x < b.length; x++) {
			for (int y = 0; y < b[x].length; y++) {
				d[n]=b[x][y];n++;
			}
		}
		clientOut.write(d);
		clientOut.flush();
	}
	
	public synchronized void sendRequest2Client(byte[] b, byte request) throws IOException{
		byte[] d = new byte[b.length+1];
		d[0]=request;
		for(int i = 0; i < b.length; i++)d[i+1]=b[i];
		clientOut.write(d);
		clientOut.flush();
	}
	
	public synchronized void sendRequest2Client(byte[] b, byte request, byte sub) throws IOException{
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
