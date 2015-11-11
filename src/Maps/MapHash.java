package Maps;

import gfx.Screen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import Main.Game;
import Multiplayer.Client;
import Multiplayer.MapManager;
import Multiplayer.Server;
import Pixels.AdditionalData;
import Pixels.Material;
import Pixels.PixelList;

public class MapHash  extends Map{

	public static final int LAYER_BACK=0, LAYER_LIQUID = 1, LAYER_FRONT = 2, LAYER_LIGHT = 3,
			MAX_LIGHT=64,
			GT_SP=0,GT_CLIENT=1,GT_SERVER=2;
	public static final int[] LAYER_ALL = {LAYER_BACK,LAYER_LIQUID,LAYER_FRONT,LAYER_LIGHT},
			LAYER_ALL_PIXEL = {LAYER_BACK,LAYER_LIQUID,LAYER_FRONT},
			LAYER_ALL_MATERIAL = {LAYER_BACK,LAYER_FRONT};
	public String path;
	public Screen screen;
	public int width = 1024;
	public int height = 1024;
	protected UpdateManager updates = new UpdateManager();
	public int updatecount = 0;
	private ArrayList<MapManager.chunkLoader> cloaders = new ArrayList<>();
	private int gametype = 0;
	private MapManager mapManager;
	private MapUpdater mapUpdater = new MapUpdater();

	private HashMap<Long,Chunk> chunks = new HashMap<>();
//	private Chunk[][] chunks = new Chunk[width][height];
	
	public MapHash(String path, Screen screen){
		super(path, screen);
	}
	
	public void setGametype(int gt){
		gametype = gt;
		if(gt==GT_CLIENT&mapManager==null)mapManager = new MapManager(this);
	}
	
	public void tick(int tickCount){
		Material m;
		int x,y,l;
		int ID;
		int size = updates.startUpdate();
		for(int i = 0; i < size; i++){
			int[] co = updates.activate(i);
			x=co[0];y=co[1];l=co[2];
			if(l!=Map.LAYER_LIGHT){
				ID = getID(x, y, l);
				if(ID!=0 & getUpdate(x,y,l)){
					if(l==Map.LAYER_LIQUID) m = PixelList.GetLiquid(ID);
					else m = PixelList.GetMat(ID);
					if(m.tick(x, y, l, tickCount, this))addBlockUpdate(x, y, l);
				}
			}else{
				if(getUpdate(x,y,l))if(updateLight(x, y))addLightUpdate(x,y);
			}
			updatecount++;
		}
	}
	
	public void sendMapUpdates(int tickCount) {
		if(mapUpdater.hasUpdates())
		switch(gametype) {
		case GT_CLIENT:
			try {
				Client.send2Server(mapUpdater.compUpdates());
			} catch (IOException e) {Game.logError("Exception sending Map Updates");}
			//send to server
			break;
		case GT_SERVER:
			try {
				Server.sendMapData(mapUpdater.compUpdates());
			} catch (IOException e) {Game.logError("Exception sending Map Updates");}
			break;
		}
	}
	
	public void receiveMapUpdates(InputStream in) {
		mapUpdater.decompUpdates(in, this, gametype==GT_CLIENT);
	}
	
	public boolean isUpdating(int x, int y, int l){
		if(isInsideChunk(x, y)){
			return getChunkAbs(x, y).isUpdating(x, y, l);
		}else return false;
	}
	
	public boolean setUpdating(int x, int y, int l){
		if(isInsideChunk(x, y)){
			return getChunkAbs(x, y).setUpdating(x, y, l);
		}else return false;
	}
	
	public boolean getUpdate(int x, int y, int l){
		if(isInsideChunk(x, y)){
			return getChunkAbs(x, y).getUpdate(x, y, l);
		}else return false;
	}
	
	public void addBlockUpdate(int x, int y, int l){
		int Sx;if(Math.random()<0.5)Sx=1;else Sx=-1;
		int Sy;if(Math.random()<0.5)Sy=1;else Sy=-1;
		for(int X = 0; X <= 2; X++){
			for(int Y = 0; Y <= 2; Y++){
				if(gametype != GT_CLIENT) {
					for(int L : Map.LAYER_ALL){
						if(setUpdating(x+Sx*X-Sx, y+Sy*Y-Sy, L))updates.addUpdate(x+Sx*X-Sx, y+Sy*Y-Sy, L);
					}
				}else{
					if(setUpdating(x+Sx*X-Sx, y+Sy*Y-Sy, Map.LAYER_LIGHT))updates.addUpdate(x+Sx*X-Sx, y+Sy*Y-Sy, Map.LAYER_LIGHT);
				}
			}
		}
	}
	
	public void addLightUpdate(int x, int y){
		if(setUpdating(x+1,y,Map.LAYER_LIGHT))updates.addUpdate(x+1, y, Map.LAYER_LIGHT);
		if(setUpdating(x-1,y,Map.LAYER_LIGHT))updates.addUpdate(x-1, y, Map.LAYER_LIGHT);
		if(setUpdating(x,y+1,Map.LAYER_LIGHT))updates.addUpdate(x, y+1, Map.LAYER_LIGHT);
		if(setUpdating(x,y-1,Map.LAYER_LIGHT))updates.addUpdate(x, y-1, Map.LAYER_LIGHT);
	}
	
	public boolean updateLight(int x, int y){
		byte light,tempL,c,startL;
		startL=getlight(x,y);
		if(getID(x,y,LAYER_BACK)==0){
			light = (byte) MAX_LIGHT;
		}else{
			light=0;
			if(getID(x,y,Map.LAYER_FRONT)==0)c=1;
			else c=2;
			if((tempL = getlight(x+1,y))>light)light = (byte) (tempL-c);
			if((tempL = getlight(x-1,y))>light)light = (byte) (tempL-c);
			if((tempL = getlight(x,y+1))>light)light = (byte) (tempL-c);
			if((tempL = getlight(x,y-1))>light)light = (byte) (tempL-c);
			if(light<0)light = 0;
		}
		setlight(x,y,light);
		if(light!=startL)return true;
		else return false;
	}
	
	public void render(){
		int ID;
		Material m;
		int X,Y;
		short light;

		for(int l = Map.LAYER_BACK; l <= Map.LAYER_LIGHT; l++){
			for(int y = 0; y <screen.height/3; y++){
				for(int x = 0; x <screen.width/3; x++){
					X=x+screen.xOffset;Y=y+screen.yOffset;
					
					light = (byte) ((MAX_LIGHT-getlight(X,Y)));
					ID = getID(X,Y,l);
					if(ID==-1){
						loadChunk(X,Y);
//						try{ID = cloaders.get(0).chunk.getID(X%1024, Y%1024, l);}catch(NullPointerException|IndexOutOfBoundsException e) {}
//						light = 0;
					}else {
						if(light == MAX_LIGHT){
							screen.drawShadow(X, Y, 0xff000000);
						}else{
							if(ID!=0){
								if(l!=Map.LAYER_LIQUID)m = PixelList.GetMat(ID);
								else m = PixelList.GetLiquid(ID);
								m.render(X, Y, l, this,screen);
							}
							if(l==Map.LAYER_LIGHT)screen.drawShadow(X, Y, ((MAX_LIGHT-getlight(X,Y))<<26));
						}
					}
				}
			}
		}
	}
	
	public boolean loadChunk(int x, int y){
		int cx = x/width,cy = y/height;
		for(int i = 0; i < cloaders.size(); i++) {
			if(cloaders.get(i).finished) {
				try{
					MapManager.chunkLoader cl = cloaders.remove(i);
					if(!cl.canceled) {
						setChunk(cl.chunk.x, cl.chunk.y, cl.chunk);
						getChunk(cl.chunk.x, cl.chunk.y).refreshUpdates();
						return true;
					}
				}catch(IndexOutOfBoundsException e) {}
				return false;
			}
			if(cx==cloaders.get(i).chunk.x & cy==cloaders.get(i).chunk.y)return false;
		}
		if(!isChunk(cx, cy)) {
			MapManager.chunkLoader l = new MapManager.chunkLoader(new Chunk(path, cx, cy, this));
			cloaders.add(l);
			Thread t = new Thread(l);
			t.setName("ChunkLoader"+cx+"_"+cy);
			t.start();
			return false;
		}else {
			return true;
		}
	}
	
	public void cancelChunkLoading() {
		for(int i = 0; i < cloaders.size(); i++)cloaders.get(i).canceled=true;
	}
	
	private Chunk lastChunk;
	
	/**
	 * This method takes the absolute <b>x,y</b>
	 * Coordinates of a Pixel on the Map and returns the Chunk
	 * to which this Pixel belongs
	 * @param x the absolute x Coordinate
	 * @param y the absolute y Coordinate
	 * @return returns the Chunk where the Coordinate <b>x,y</b> lies
	 * @see this Method is equal to <code>getChunk(x/1024,y/1024);</code>
	 */
	public Chunk getChunkAbs(int x, int y) {
		if(lastChunk == null) {
			lastChunk = chunks.get(((((long)x)/width)<<32)|(((long)y)/height));
		}
		if(!(x/width == lastChunk.x && y/height == lastChunk.y)) {
			lastChunk = chunks.get(((((long)x)/width)<<32)|(((long)y)/height));
		}
		return lastChunk;
	}
	
	/**
	 * This method returns the Chunk with the Coordinate <b>x,y</b>
	 * @param x the x Coordinate of the Chunk
	 * @param y the y Coordinate of the Chunk
	 * @return returns Chunk with the Coordinate <b>x,y</b>
	 */
	public Chunk getChunk(long x, long y) {
		return chunks.get((x<<32)|y);
	}
	
	/**
	 * Puts the Chunk <b>c</b> at the Coordinates <b>x,y</b>
	 * @param x
	 * @param y
	 * @param c
	 */
	public void setChunk(long x, long y, Chunk c) {
		chunks.put((x<<32)|y, c);
	}
	
	/**
	 * Returns true if the Coordinates <b>x,y</b> are inside an existing Chunk
	 * @param x
	 * @param y
	 * @return <code>chunks.containsKey(((x/1024)<<32)|(y/1024))</code>
	 */
	public boolean isInsideChunk(int x, int y) {
		if(lastChunk != null && lastChunk.x == x/1024 && lastChunk.y==y/1024) {
			return true;
		}
		return chunks.containsKey(((((long)x)/width)<<32)|(((long)y)/height));
	}

	/**
	 * Returns true if there is a Chunk on the Coordinates <b>x,y</b>
	 * @param x
	 * @param y
	 * @return <code>chunks.containsKey((x<<32)|y)</code>
	 */
	public boolean isChunk(long x, long y) {
		return chunks.containsKey((x<<32)|y);
	}
	
	public int getID(int x, int y, int layer){
		if(isInsideChunk(x, y)){
			return getChunkAbs(x, y).getID(x%width, y%height, layer);
		}else{
			return -1;
		}
	}
	public void setID(int x, int y, int l, int ID){
		setID(x,y,l,ID,null,false);
	}
	public void setID(int x, int y, int l, int ID, AdditionalData ad, boolean skipUpdate){
		if(isInsideChunk(x, y)){
			addBlockUpdate(x, y, l);
			if(ad!=null)setAD(x,y,l,ad);
			else PixelList.GetPixel(ID, l).createAD(x, y, l, this);
			getChunkAbs(x, y).setID(x%width, y%height, (short) ID, l);
			
			if(!skipUpdate && (gametype== GT_SERVER || gametype == GT_CLIENT)) {
				if(ad==null)ad = getChunkAbs(x, y).getAD(x%width, y%height, l);
				mapUpdater.addUpdateID(new int[] {x,y,l,ID}, ad);
			}
		}
	}
	
	public void movePixel(int xs, int ys, int ls, int xf, int yf, int lf){
		if(getAD(xs,ys,ls)!=null)setID(xf,yf,lf,getID(xs,ys,ls),new AdditionalData(getAD(xs,ys,ls)),false);
		else setID(xf,yf,lf,getID(xs,ys,ls),null,false);
		setID(xs,ys,ls,0);
	}
	
	public AdditionalData getAD(int x, int y, int layer){
		if(isInsideChunk(x, y)){
			return getChunkAbs(x, y).getAD(x%width, y%height, layer);
		}else{
			return null;
		}
	}
	public void setAD(int x, int y, int layer, AdditionalData ad){
		if(isInsideChunk(x, y)){
			getChunkAbs(x, y).setAD(x%width, y%height, layer, ad);
		}
	}

	public byte getlight(int x, int y){
		if(isInsideChunk(x, y)){
			return getChunkAbs(x, y).light[(x%width)+(y%height)*width];
		}else{
			return 0;
		}
	}
	public void setlight(int x, int y, byte b){
		if(isInsideChunk(x, y)){
			getChunkAbs(x, y).light[(x%width)+(y%height)*width]=b;
		}
	}

	public void setlighter(int x, int y, byte b) {
		if(getlight(x,y)<b)setlight(x, y, b);
	}

	public boolean isSolid(int x, int y){
		if(getID(x, y, Map.LAYER_FRONT)!=0 && PixelList.GetMat(getID(x, y, Map.LAYER_FRONT)).solid)return true;
		else return false;
	}

	public void save(){
		for (Chunk c : chunks.values()) {
			c.save();
		}
	}
	
	public byte[] compressedChunk(int x, int y) {
		return getChunk(x, y).compress();
	}
	
	public static void newMap(String path,String name){
		int n = 1;
		File dir = new File(path + File.separator +  name + Integer.toString(n));
		while(dir.isDirectory()){
			n ++;
			dir = new File(path + File.separator +  name + Integer.toString(n));
		}
		dir.mkdirs();
	}
}