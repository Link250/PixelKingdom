package map;

import java.io.IOException;
import java.util.ArrayList;

public class ChunkManagerSP implements Map.ChunkManager{
	
	private ArrayList<ChunkLoader> cloaders = new ArrayList<>();
	private Map map;
	private Chunk[][] chunks;
	private String mapPath;
	
	public ChunkManagerSP(Map map, Chunk[][] chunks, String mapPath) {
		this.map = map;
		this.chunks = chunks;
		this.mapPath = mapPath;
	}
	
	public void loadChunk(int cx, int cy){
		for(ChunkLoader cl : cloaders) {
			if(cx==cl.chunk.x && cy==cl.chunk.y)return;
		}
		if(chunks[cx][cy]==null) {
			ChunkLoader l = new ChunkLoader(new Chunk(mapPath, cx, cy, map), this);
			cloaders.add(l);
			Thread t = new Thread(l);
			t.setName(Thread.currentThread().getName()+"_cl"+cx+"_"+cy);
			t.start();
		}
	}
	
	public void cancelChunkLoading(){
		this.cloaders.clear();
	}
	
	public boolean hasLoadedChunk(int cx, int cy) {
		return this.chunks[cx][cy].finishedLoading();
	}
	
	private void finishedLoading(ChunkLoader loader) {
		this.chunks[loader.chunk.x][loader.chunk.y] = loader.chunk;
		this.chunks[loader.chunk.x][loader.chunk.y].refreshUpdates();
		this.cloaders.remove(loader);
	}
	
	public class ChunkLoader implements Runnable{
		public Chunk chunk;
		private ChunkManagerSP chunkManager;
		
		public ChunkLoader(Chunk c, ChunkManagerSP chunkManager) {
			this.chunk = c;
			this.chunkManager = chunkManager;
		}

		public void run() {
			if(chunk.path!=null) {
				try {chunk.load(null);} catch (IOException e) {}
			}
			chunkManager.finishedLoading(this);
		}
	}
}
