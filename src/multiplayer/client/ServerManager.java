package multiplayer.client;

import java.io.IOException;
import java.util.ArrayList;

import main.conversion.ConverterInStream;
import multiplayer.InputReceiver;

public class ServerManager implements Runnable {
	
	boolean running = true;
	Client client;
	ConverterInStream server;
	public ArrayList<InputReceiver> requests = new ArrayList<InputReceiver>();
	
	public ServerManager(Client c, ConverterInStream s){
		server = s;
		client = c;
	}

	public void run() {
		int input=0;
		try {
			while (running){
				Thread.sleep(1);
				input = server.read();
				if(input==1) {running=false;client.close();}
				for (InputReceiver inputReceiver : requests) {
					if(inputReceiver.requestType() == input) {
						if(inputReceiver.useInput(server)) {
							requests.remove(inputReceiver);
						}
						break;
					}
				}
			}
		} catch (IOException | InterruptedException e) {running = false;client.close();}
	}
	
	public static void request(byte rNumber, byte[] data) throws IOException{
		byte[] b = new byte[data.length+1];
		b[0]=rNumber;
		for(int i = 0; i < data.length; i++)b[i+1]=data[i];
		Client.send2Server(b);
	}
}
