package map;

import gfx.Screen;
import main.Game;
import multiplayer.Client;
import multiplayer.MapManager;
import multiplayer.Server;
import multiplayer.conversion.ConverterInStream;
import pixel.AD;
import pixel.Material;
import pixel.PixelList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Map {

	public static final byte LAYER_BACK=0, LAYER_LIQUID = 1, LAYER_FRONT = 2, LAYER_LIGHT = 3,
			MAX_LIGHT=64,
			GT_SP=0,GT_CLIENT=1,GT_SERVER=2;
	public static final byte[] LAYER_ALL = {LAYER_BACK,LAYER_LIQUID,LAYER_FRONT,LAYER_LIGHT},
			LAYER_ALL_PIXEL = {LAYER_BACK,LAYER_LIQUID,LAYER_FRONT},
			LAYER_ALL_MATERIAL = {LAYER_BACK,LAYER_FRONT};
	public String path;
	public Screen screen;
	public int width = 1024;
	public int height = 1024;
	protected UpdateManager updatesPixel = new UpdateManager();
	protected UpdateManager updatesLight = new UpdateManager();
	public int updatecount = 0;
	private ArrayList<MapManager.chunkLoader> cloaders = new ArrayList<>();
	private int gametype = 0;
	private MapManager mapManager;
	private MapUpdater mapUpdater = new MapUpdater();

	private Chunk[][] chunks = new Chunk[width][height];
	
	public Map(String path, Screen screen){
		this.path = path;
		this.screen = screen;
	}
	
	public void setGametype(int gt){
		gametype = gt;
		if(gt==GT_CLIENT && mapManager==null)mapManager = new MapManager(this);
	}
	
	public void tick(int tickCount){
		Material<?> m;
		int x,y,l;
		int ID;
		int size = updatesPixel.startUpdate();
		for(int i = 0; i < size; i++){
			int[] co = updatesPixel.activate(i);
			x=co[0];y=co[1];l=co[2];
			ID = getID(x, y, l);
			if(getUpdate(x,y,l) && ID!=0){
				m = PixelList.GetPixel(ID, l);
				if(m.tick(x, y, l, tickCount, this))addBlockUpdate(x, y, l);
			}
			updatecount++;
		}
		size = updatesLight.startUpdate();
		for(int i = 0; i < size; i++){
			int[] co = updatesLight.activate(i);
			x=co[0];y=co[1];l=co[2];
			if(getUpdate(x,y,l))updateLight(x, y);
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
	
	public void receiveMapUpdates(ConverterInStream in){
		mapUpdater.decompUpdates(in, this, gametype==GT_CLIENT);
	}
	
	public boolean isUpdating(int x, int y, int l){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			return chunks[cx][cy].isUpdating(x, y, l);
		}else return false;
	}
	
	public boolean setUpdating(int x, int y, int l){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			return chunks[cx][cy].setUpdating(x, y, l);
		}else return false;
	}
	
	public boolean getUpdate(int x, int y, int l){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			return chunks[cx][cy].getUpdate(x, y, l);
		}else return false;
	}
	
	public void addBlockUpdate(int x, int y, int l){
		int Sx;if(Math.random()<0.5)Sx=1;else Sx=-1;
		int Sy;if(Math.random()<0.5)Sy=1;else Sy=-1;
		for(int X = 0; X <= 2; X++){
			for(int Y = 0; Y <= 2; Y++){
				if(gametype != GT_CLIENT) {
					for(int L : Map.LAYER_ALL_PIXEL){
						if(setUpdating(x+Sx*X-Sx, y+Sy*Y-Sy, L)) {
							updatesPixel.addUpdate(x+Sx*X-Sx, y+Sy*Y-Sy, L);
						}
					}
					if(setUpdating(x+Sx*X-Sx, y+Sy*Y-Sy, Map.LAYER_LIGHT)) {
						updatesLight.addUpdate(x+Sx*X-Sx, y+Sy*Y-Sy, Map.LAYER_LIGHT);
					}
				}else{
					if(setUpdating(x+Sx*X-Sx, y+Sy*Y-Sy, Map.LAYER_LIGHT)) {
						updatesLight.addUpdate(x+Sx*X-Sx, y+Sy*Y-Sy, Map.LAYER_LIGHT);
					}
				}
			}
		}
	}
	
	public void addLightUpdate(int x, int y){
		if(setUpdating(x+1,y,Map.LAYER_LIGHT))updatesLight.addUpdate(x+1, y, Map.LAYER_LIGHT);
		if(setUpdating(x-1,y,Map.LAYER_LIGHT))updatesLight.addUpdate(x-1, y, Map.LAYER_LIGHT);
		if(setUpdating(x,y+1,Map.LAYER_LIGHT))updatesLight.addUpdate(x, y+1, Map.LAYER_LIGHT);
		if(setUpdating(x,y-1,Map.LAYER_LIGHT))updatesLight.addUpdate(x, y-1, Map.LAYER_LIGHT);
	}
	
	public void updateLight(int x, int y){
		byte light,tempL,c;
		if(getID(x,y,LAYER_BACK)==0){
			light = (byte) MAX_LIGHT;
		}else{
			light=0;
			c = (byte) (getID(x,y,Map.LAYER_FRONT)==0 ? 1 : 2);
			if((tempL = getlight(x+1,y))-c>light)light = (byte) (tempL-c);
			if((tempL = getlight(x-1,y))-c>light)light = (byte) (tempL-c);
			if((tempL = getlight(x,y+1))-c>light)light = (byte) (tempL-c);
			if((tempL = getlight(x,y-1))-c>light)light = (byte) (tempL-c);
			if(light<0)light = 0;
		}
		setlight(x,y,light);
	}
	
	public void render(){
		int ID;
		Material<?> m;
		int X,Y;
		short light;

		for(int l = Map.LAYER_BACK; l <= Map.LAYER_LIGHT; l++){
			for(int y = 0; y <screen.height/3; y++){
				for(int x = 0; x <screen.width/3; x++){
					X=x+screen.xOffset;Y=y+screen.yOffset;
					
					light = (byte) ((MAX_LIGHT-getlight(X,Y)));
					ID = getID(X,Y,l);
					if(ID==-1){
						loadChunk(X/1024,Y/1024);
//						try{ID = cloaders.get(0).chunk.getID(X%1024, Y%1024, l);}catch(NullPointerException|IndexOutOfBoundsException e) {}
//						light = 0;
					}else {
						if(light == MAX_LIGHT){
							screen.drawShadow(X, Y, 0xff000000);
						}else{
							if(ID!=0){
								m=PixelList.GetPixel(ID, l);
								m.render(X, Y, l, this,screen);
							}
							if(l==Map.LAYER_LIGHT)screen.drawShadow(X, Y, ((MAX_LIGHT-getlight(X,Y))<<26));
						}
					}
				}
			}
		}
	}
	
	public boolean loadChunk(int cx, int cy){
		for(MapManager.chunkLoader cl : cloaders) {
			if(cl.finished) {
				cloaders.remove(cl);
				if(!cl.canceled) {
					chunks[cl.chunk.x][cl.chunk.y]=cl.chunk;
					chunks[cl.chunk.x][cl.chunk.y].refreshUpdates();
//					return true;
				}
				return false;
			}
			if(cx==cl.chunk.x && cy==cl.chunk.y)return false;
		}
		if(chunks[cx][cy]==null) {
			MapManager.chunkLoader l = new MapManager.chunkLoader(new Chunk(path, cx, cy, this));
			cloaders.add(l);
			Thread t = new Thread(l);
			t.setName(Thread.currentThread().getName()+"_cl"+cx+"_"+cy);
			t.start();
			return false;
		}else {
			return true;
		}
	}
	
	public void cancelChunkLoading() {
		for(int i = 0; i < cloaders.size(); i++)cloaders.get(i).canceled=true;
	}
	
	public int getID(int x, int y, int layer){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			return chunks[cx][cy].getID(x, y, layer);
		}else{
			return -1;
		}
	}
	public void setID(int x, int y, int l, int ID){
		setID(x,y,l,ID,null,false);
	}
	public void setID(int x, int y, int l, int ID, AD ad, boolean skipUpdate){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			addBlockUpdate(x, y, l);
			if(ad!=null)setAD(x,y,l,ad);
			else PixelList.GetPixel(ID, l).createAD(x, y, l, this);
			chunks[cx][cy].setID(x%1024, y%1024, (short) ID, l);
			
			if(!skipUpdate && (gametype== GT_SERVER || gametype == GT_CLIENT)) {
				if(ad==null)ad = chunks[cx][cy].getAD(x%1024, y%1024, l);
				mapUpdater.addUpdateID(new int[] {x,y,l,ID}, ad);
			}
		}
	}
	
	public void movePixel(int xs, int ys, int ls, int xf, int yf, int lf){
		setID(xf,yf,lf,getID(xs,ys,ls),getAD(xs,ys,ls),false);
		setID(xs,ys,ls,0);
	}
	
	public <ADType extends AD> ADType getAD(int x, int y, int layer){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			return chunks[cx][cy].getAD(x, y, layer);
		}else{
			return null;
		}
	}
	public void setAD(int x, int y, int layer, AD ad){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			chunks[cx][cy].setAD(x, y, layer, ad);
			if(ad!=null) {
				mapUpdater.addUpdateAD(new int[] {x,y,layer}, ad);
			}
		}
	}

	public byte getlight(int x, int y){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			return chunks[cx][cy].light[x+y*width];
		}else{
			return 0;
		}
	}
	public void setlight(int x, int y, byte b){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			if(chunks[cx][cy].light[x+y*width]!=b) {
				chunks[cx][cy].light[x+y*width]=b;
				addLightUpdate(cx*1024+x,cy*1024+y);
			}
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
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(chunks[x][y]!=null)chunks[x][y].save();
			}
		}
	}
	
	public byte[] compressedChunk(int x, int y) {
		try {
			return chunks[x][y].compress();
		} catch (IOException e) {/*dont worry, this should never ever happen C: */}
		return null;
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