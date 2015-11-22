package multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import main.ConvertData;
import main.Game;
import main.IOConverter;
import map.Chunk;
import map.Map;
import multiplayer.conversion.ConverterInStream;

public class MapManager implements InputReceiver{

	private Map map;
	
	public MapManager(Map m) {
		map = m;
		Client.server.requests.add(this);
	}
	
	public boolean useInput(ConverterInStream in) throws IOException { //is used when the Server sends Map data
		map.receiveMapUpdates(in);
		return false;
	}

	public int requestType() {return Request.MAP_DATA;}

	public static class chunkLoader implements Runnable, InputReceiver{
		public Chunk chunk;
		public boolean finished;
		public boolean canceled;
		
		public chunkLoader(Chunk c) {chunk=c;finished=false;canceled=false;}

		public void run() {
			if(chunk.path!=null) {
				try {chunk.load(null);} catch (IOException e) {}
			}
			else{
				byte[] data = new byte[8];
				ArrayList<Byte> temp = new ArrayList<Byte>();
				ConvertData.I2B(temp, chunk.x); ConvertData.I2B(temp, chunk.y); for(int i = 0; i < temp.size(); i++)data[i]=temp.get(i);
				try {
					ServerManager.request(Request.CHUNK_DATA, data);
				} catch (IOException e1) {canceled=true;}
				Client.server.requests.add(this);
				while(!finished & !canceled){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e){e.printStackTrace();}
				}
			}
			finished=true;
		}
		
		public boolean useInput(ConverterInStream in) throws IOException {
			Game.logInfo("used receiving map data");
			int l = IOConverter.receiveInt(in);
			byte[] data = new byte[l];
			for(int i = 0; i < l; i++) {
				data[i]=(byte)in.read();
			}chunk.load(data);
			finished = true;
			return true;
		}

		public int requestType() {return Request.CHUNK_DATA;}

	}
}
