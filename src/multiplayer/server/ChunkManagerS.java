package multiplayer.server;

import java.io.IOException;

import dataUtils.conversion.InConverter;
import map.Map;
import multiplayer.Request;

public class ChunkManagerS implements InputReceiver {
	
	private ClientsManager clientsManager;
	private Map map;
	
	public ChunkManagerS(ClientsManager clientsManager, Map map) {
		this.clientsManager = clientsManager;
		this.map = map;
	}
	
	public void useInput(InConverter in, byte ID) throws IOException {
		int cx = in.readInt(),
			cy = in.readInt();
		if(map.hasLoadedChunk(cx, cy)) {
			this.clientsManager.getClient(ID).sendChunk(cx, cy, map.compressedChunk(cx, cy));
		}else {
			this.map.loadChunk(cx, cy);
			(new Thread(new ChunkLoader(cx, cy, this.map, this.clientsManager.getClient(ID)))).start();
		}
	}

	public byte requestType() {return Request.CHUNK_DATA;}
	
	private class ChunkLoader implements Runnable{
		
		private int cx, cy;
		private Map map;
		private ClientManager manager;
		
		public ChunkLoader(int cx, int cy, Map map, ClientManager manager) {
			this.cx = cx;
			this.cy = cy;
			this.map = map;
			this.manager = manager;
		}
		
		public void run() {
			while(!map.hasLoadedChunk(cx, cy)) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			this.manager.sendChunk(cx, cy, map.compressedChunk(cx, cy));
		}
	}
}
