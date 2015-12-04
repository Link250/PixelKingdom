package multiplayer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import main.Game;
import multiplayer.ConnectionManager;
import multiplayer.MapUpdater.UpdateList;

public class ClientsManager implements Runnable, ConnectionManager {
	private boolean running = true;
	
	private Server server;
	private ServerSocket serverSocket;
	
	private HashMap<Byte, ClientManager> clients = new HashMap<>();
	private HashMap<Byte, InputReceiver> receivers;
	
	public ClientsManager(Server server, ServerSocket socket){
		this.server = server;
		this.serverSocket = socket;
		this.receivers = new HashMap<>();
		
		Thread t = new Thread(this);
		t.setName("ClientsManager");
		t.start();
		Game.logInfo("Clients Manager started !");
	}
	
	public void run() {
		try {
			byte c = 1;
			while(running){
				Socket clientSocket = serverSocket.accept();
				this.newConnection(c, clientSocket);c++;
				Game.logInfo("Client "+clientSocket.getInetAddress().getHostAddress()+" connected");
			}serverSocket.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void addInputReceiver(InputReceiver ir) {
		this.receivers.put(ir.requestType(), ir);
	}
	
	private void newConnection(byte ID, Socket clientSocket) throws IOException {
		Game.logInfo("new connection");
		ClientManager clientManager = new ClientManager(clientSocket, this, this.receivers, ID);
		for (ClientManager client : this.clients.values()) {
			client.sendPlayerConnect(ID);
			clientManager.sendPlayerConnect(client.ID);
			clientManager.sendPlayerColor(client.ID, client.player.color);
		}
		this.clients.put(ID, clientManager);
	}
	
	protected void closeConnection(byte ID){
		ClientManager client = clients.get(ID);
		try {
			client.closeConnection();
			Game.logInfo("Client "+ID+" disconnected");
		} catch (IOException e) {
			Game.logError("Client "+ID+" disconnected by error");
		} catch(NullPointerException e) {
			Game.logError("Client "+ID+" was already missing");
		} finally {
			this.server.save();
			clients.remove(ID);
			for (ClientManager clientManager : clients.values()) {
				clientManager.sendPlayerDisconnect(ID);
			}
		}
	}
	
	public void sendPlayerColor(byte ID, int color){
		for (ClientManager clientManager : clients.values()) {
			if(clientManager.ID!=ID) {
				clientManager.sendPlayerColor(ID, color);
			}
		}
	}
	
	public void sendPlayerRefresh(byte ID){
		for (ClientManager clientManager : clients.values()) {
			if(clientManager.ID!=ID) {
				clientManager.sendPlayerRefresh(ID);
			}
		}
	}
	
	public void sendMapUpdates(ArrayList<UpdateList> UpdateLists){
		for (ClientManager clientManager : clients.values()) {
			clientManager.sendMapUpdates(UpdateLists);
		}
	}
	
	public ClientManager getClient(byte ID) {
		return this.clients.get(ID);
	}
}
