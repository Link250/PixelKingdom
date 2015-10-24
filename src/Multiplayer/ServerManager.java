package Multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ServerManager implements Runnable {
	
	boolean running = true;
	Client client;
	InputStream server;
	public ArrayList<InputReceiver> requests = new ArrayList<InputReceiver>();
	
	public ServerManager(Client c, InputStream s){
		server = s;
		client = c;
	}

	public void run() {
		int input=0;
		try {
			while (running){
				Thread.sleep(1);
				if(input==0)input = server.read();
				if(input==1) {running=false;client.close();}
				for(int i = 0; i < requests.size(); i++) {
					if(requests.get(i).requestType()==input) {
						if(requests.get(i).useInput(server)) {
							requests.remove(i);
							i--;
						}
						input=0;
					}
				}
			}
		} catch (IOException | InterruptedException e) {running = false;}
	}
	
	public static void request(byte rNumber, byte[] data) throws IOException{
		byte[] b = new byte[data.length+1];
		b[0]=rNumber;
		for(int i = 0; i < data.length; i++)b[i+1]=data[i];
		Client.send2Server(b);
	}
}
