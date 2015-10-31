package Multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Main.Game;

public class ClientAccepter implements Runnable {
	boolean running = true;
	
	Server server;
	
	public ClientAccepter(Server s){
		server = s;
	}

	public void run() {
		
		try {
			ServerSocket serverSocket = new ServerSocket(Game.PORT);
			int c = 1;
			while(running){
				Socket clientSocket = serverSocket.accept();
				server.newConnection(c, clientSocket);
				Thread t = new Thread(Server.clients.get(Server.clients.size()-1));
				t.setName("Client"+c);c++;
				t.start();
				Game.logInfo("Client "+clientSocket.getInetAddress().getHostAddress()+" connected");
			}serverSocket.close();
		} catch (IOException e) {e.printStackTrace();}
	}

}
