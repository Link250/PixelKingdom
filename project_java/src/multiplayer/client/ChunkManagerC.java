package multiplayer.client;

import java.io.IOException;
import java.util.ArrayList;

import dataUtils.conversion.InConverter;
import main.Game;
import map.Chunk;
import map.Map;
import multiplayer.Request;

/**
 * This is the Chunk Manager for the Client,
 * it is able to get its Chunks from the Server
 * @author Daniel
 *
 */
public class ChunkManagerC implements InputReceiver, Map.ChunkManager{
	
	private ArrayList<ChunkLoader> cloaders = new ArrayList<>();
	private Map map;
	private Chunk[][] chunks;
	private ServerManager serverManager;
	
	public ChunkManagerC(Map map, Chunk[][] chunks, ServerManager manager) {
		this.map = map;
		this.chunks = chunks;
		this.serverManager = manager;
	}
	
	public void useInput(InConverter in) throws IOException {
		Game.logInfo("using receiving map data");
		int l = in.readInt();
		int cx = in.readInt();
		int cy = in.readInt();
		byte[] data = new byte[l];
		for(int i = 0; i < l; i++) {
			data[i]=in.readByte();
		}
		for(ChunkLoader cl : cloaders) {
			if(cx==cl.chunk.x && cy==cl.chunk.y) {
				cl.setData(data);
				return;
			}
		}
	}
	
	public void loadChunk(int cx, int cy){
		for(ChunkLoader cl : cloaders) {
			if(cx==cl.chunk.x && cy==cl.chunk.y) {
				return;
			}
		}
		if(chunks[cx][cy]==null) {
			ChunkLoader l = new ChunkLoader(new Chunk(null, cx, cy, map), this);
			cloaders.add(l);
			Thread t = new Thread(l);
			t.setName("Client_cl"+cx+"_"+cy);
			t.start();
			try {
				this.serverManager.sendChunkRequest(cx, cy);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void cancelChunkLoading() {
		for(ChunkLoader cloader : cloaders)cloader.canceled=true;
		this.cloaders.clear();
	}
	
	private void finishedLoading(ChunkLoader loader) {
		if(!loader.canceled) {
			this.chunks[loader.chunk.x][loader.chunk.y] = loader.chunk;
			this.chunks[loader.chunk.x][loader.chunk.y].refreshLight();
		}
		this.cloaders.remove(loader);
	}
	
	public byte requestType() {return Request.CHUNK_DATA;}
	
	public class ChunkLoader implements Runnable{
		public Chunk chunk;
		public boolean canceled;
		private ChunkManagerC manager;
		private byte[] chunkData;
		
		public ChunkLoader(Chunk chunk, ChunkManagerC manager) {
			this.chunk=chunk;
			this.canceled=false;
			this.manager = manager;
		}
		
		public void setData(byte[] chunkData) {
			this.chunkData = chunkData;
		}
		
		public void run() {
			//waiting for the chunkData from the Server...
			while(chunkData == null && !canceled){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e){e.printStackTrace();}
			}
			//data arrived or loading was canceled
			if(!canceled) {
				try {
					chunk.load(chunkData);
				} catch (IOException e) {e.printStackTrace();}
			}
			manager.finishedLoading(this);
		}
	}
}
