package multiplayer.client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Player;

import static main.Game.*;

import main.MainConfig;
import main.conversion.ConverterInStream;
import main.conversion.ConverterQueue;
import main.conversion.ConverterOutStream;
import multiplayer.ConnectionManager;
import multiplayer.MapUpdater.UpdateList;
import multiplayer.Request;

public class ServerManager implements Runnable, ConnectionManager {
	
	private boolean running;
	private Client client;
	private ConverterInStream serverIn;
	private ConverterOutStream serverOut;
	private HashMap<Byte,InputReceiver> receivers;
	
	public ServerManager(Client client, Socket serverConnection){
		try {
			this.serverIn = new ConverterInStream(serverConnection.getInputStream());
			this.serverOut = new ConverterOutStream(serverConnection.getOutputStream());
		} catch (IOException e) {e.printStackTrace();}
		this.client = client;
		this.receivers = new HashMap<>();
	}
	
	public void startThread() {
		if(this.running) {
			return;
		}
		this.running = true;
		
		Thread t = new Thread(this);
		t.setName("ServerConnection");
		t.start();
	}

	public void run() {
		byte request = 0, lastRequest;
		int length;
		while (running){
			try {
				Thread.sleep(1);
				length = serverIn.readInt()-1;
				lastRequest = request;
				request = serverIn.readByte();
				if(request==Request.CLOSE_CONNECTION || request==Request.END_OF_STREAM) {
					this.running=false;
					this.client.close();
				}else if(this.receivers.containsKey(request)){
					ConverterQueue data = new ConverterQueue();
					for (int i = 0; i < length; i++) {
						data.addByte(this.serverIn.readByte());
					}
					try{
						this.receivers.get(request).useInput(data);
					}catch(NullPointerException e) {e.printStackTrace();}
				}else{
					logWarning("Skipping "+length+" bytes of request type "+request+"\nlast request type: "+lastRequest);
					this.serverIn.skipBytes(length);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				this.running = false;
				this.client.close();
			}
		}
	}
	
	public void addInputReceiver(InputReceiver ir) {
		this.receivers.put(ir.requestType(), ir);
	}
	
	private synchronized void sendToServer(ConverterQueue data) throws IOException {
		this.sendToServer(data.emptyToArray());
	}
	
	private synchronized void sendToServer(byte ... data) throws IOException {
		this.serverOut.writeInt(data.length);
		this.serverOut.write(data);
		this.serverOut.flush();
	}
	
	public void disconnect() throws IOException {
		this.sendToServer(Request.CLOSE_CONNECTION);
	}
	
	public void sendPlayerColor(Player player) throws IOException {
		ConverterQueue data = new ConverterQueue();
		data.addByte(Request.PLAYER_DATA);
		data.addByte(Request.PLAYER_COLOR);
		data.addInt(MainConfig.PlrCol);
		this.sendToServer(data);
	}
	
	public void sendPlayerUpdate(Player player) throws IOException {
		ConverterQueue data = new ConverterQueue();
		data.addByte(Request.PLAYER_DATA);
		data.addByte(Request.PLAYER_REFRESH);
		data.addInt(player.x);
		data.addInt(player.y);
		data.addByte(player.getAnim());
		data.addByte(player.getDir());
		this.sendToServer(data);
	}
	
	public void sendMapUpdates(ArrayList<UpdateList> UpdateLists) throws IOException {
		for (UpdateList updateList : UpdateLists) {
			this.sendToServer(updateList.compress());
		}
	}
	
	public void sendChunkRequest(int chunkX, int chunkY) throws IOException {
		ConverterQueue data = new ConverterQueue();
		data.addByte(Request.CHUNK_DATA);
		data.addInt(chunkX);
		data.addInt(chunkY);
		this.sendToServer(data);
	}
}
