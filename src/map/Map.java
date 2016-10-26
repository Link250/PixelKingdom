package map;

import gfx.Screen;
import multiplayer.MapUpdater;
import multiplayer.client.ChunkManagerC;
import multiplayer.client.ServerManager;
import pixel.AD;
import pixel.Material;
import pixel.PixelList;

import java.io.File;
import java.io.IOException;

public class Map {

	public static final byte LAYER_BACK=0, LAYER_LIQUID = 1, LAYER_FRONT = 2, LAYER_LIGHT = 3,
			GT_SP=0,GT_CLIENT=1,GT_SERVER=2;
	public static final short MAX_LIGHT = 255;
	public static final byte[] LAYER_ALL = {LAYER_BACK,LAYER_LIQUID,LAYER_FRONT,LAYER_LIGHT},
			LAYER_ALL_PIXEL = {LAYER_BACK,LAYER_LIQUID,LAYER_FRONT},
			LAYER_ALL_MATERIAL = {LAYER_BACK,LAYER_FRONT};
	public static final int widthh = 1024, heighth = 1024;
	public String path;
	public Screen screen;
	protected UpdateManager updatesPixel = new UpdateManager();
	public int updateCountPixel = 0;
	protected UpdateManager updatesLight = new UpdateManager();
	public int updateCountLight = 0;
	private ChunkManager chunkMaganer;
	private int gametype = 0;
	private MapUpdater mapUpdater;

	private Chunk[][] chunks = new Chunk[1024][1024];
	
	private int regularUpdateX = -Screen.width/2-Screen.RENDER_CHUNK_SIZE;
	private int regularUpdateY = -Screen.height/2-Screen.RENDER_CHUNK_SIZE;
	
	public Map(String path, Screen screen){
		this.path = path;
		this.screen = screen;
		//the standard Chunk Manager is the one for Single Player
		chunkMaganer = new ChunkManagerSP(this, chunks, path);
	}
	
	public void setMapUpdater(MapUpdater mapUpdater){
		this.gametype = mapUpdater.gametype;
		this.mapUpdater = mapUpdater;
	}
	
	public ChunkManagerC setChunkManager(ServerManager manager){
		ChunkManagerC chunkManager = new ChunkManagerC(this, chunks, manager);
		this.chunkMaganer = chunkManager;
		return chunkManager;
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
				try {
					if(m.tick(x, y, l, tickCount, this))addPixelUpdate(x, y, l);
				}catch(NullPointerException e) {
					e.printStackTrace();
					/*dont worry, can happen in MP if some ADs are not loaded*/
				}
			}
			updateCountPixel++;
		}
		for (int n = 0; n < 8; n++) {
			size = updatesLight.startUpdate();
			for(int i = 0; i < size; i++){
				int[] co = updatesLight.activate(i);
				x=co[0];y=co[1];l=co[2];
				if(getUpdate(x,y,l)) {
					updateLight(x, y);
				}
				updateCountLight++;
			}
		}
		if( regularUpdateX < Screen.width/2+Screen.RENDER_CHUNK_SIZE) {
			regularUpdateX+=Screen.RENDER_CHUNK_SIZE;
		}else{
			if(regularUpdateY < Screen.height/2+Screen.RENDER_CHUNK_SIZE) {
				regularUpdateY+=Screen.RENDER_CHUNK_SIZE;
			}else {
				regularUpdateY = -Screen.height/2-Screen.RENDER_CHUNK_SIZE;
			}
			regularUpdateX = -Screen.width/2-Screen.RENDER_CHUNK_SIZE;
		}
		setTextureUpdating(regularUpdateX+Screen.xOffset, regularUpdateY+Screen.yOffset, Map.LAYER_LIGHT);
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
	
	public void setTextureUpdating(int x, int y, int l) {
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			chunks[cx][cy].setTextureUpdating(x%1024, y%1024, l);
		}
	}
	
	public boolean getUpdate(int x, int y, int l){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			return chunks[cx][cy].getUpdate(x, y, l);
		}else return false;
	}
	
	public void addADUpdate(int x, int y, int l, AD ad){
		if(gametype == GT_SERVER || gametype == GT_CLIENT) {
			mapUpdater.addUpdateAD(new int[]{x,y,l}, ad);
		}
	}
	
	public void addPixelUpdate(int x, int y, int l){
		int Sx = Math.random()<0.5 ? 1 : -1,
			Sy = Math.random()<0.5 ? 1 : -1;
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
					for (int L : LAYER_ALL_PIXEL) {
						setTextureUpdating(x+Sx*X-Sx, y+Sy*Y-Sy, L);
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
		short light,tempL,c;
		if(getID(x,y,LAYER_BACK)==0){
			light = MAX_LIGHT;
		}else{
			light=(short) (MAX_LIGHT-PixelList.GetMat(x, y, this, LAYER_BACK).backLightReduction());
			for (int L= 0; L < LAYER_ALL_PIXEL.length; L++) {
				tempL = PixelList.GetPixel(getID(x, y, L),L).tickLight(x, y, L, this);
				if(tempL>light)light = tempL;
			}
			if((tempL = getlight(x+1,y))-(c=PixelList.GetMat(x, y, this, LAYER_FRONT).frontLightReduction())>light)light = (short) (tempL-c);
			if((tempL = getlight(x-1,y))-(c=PixelList.GetMat(x, y, this, LAYER_FRONT).frontLightReduction())>light)light = (short) (tempL-c);
			if((tempL = getlight(x,y+1))-(c=PixelList.GetMat(x, y, this, LAYER_FRONT).frontLightReduction())>light)light = (short) (tempL-c);
			if((tempL = getlight(x,y-1))-(c=PixelList.GetMat(x, y, this, LAYER_FRONT).frontLightReduction())>light)light = (short) (tempL-c);
			if(light<0)light = 0;
		}
		setlight(x,y,(byte) light);
	}
	
	public void render(){
		Screen.drawMap(this);
	}
	
	public void loadChunk(int cx, int cy){
		this.chunkMaganer.loadChunk(cx, cy);
	}
	
	public void cancelChunkLoading() {
		this.chunkMaganer.cancelChunkLoading();
	}
	
	public boolean hasLoadedChunk(int cx, int cy) {
		return this.chunks[cx][cy]!=null && this.chunks[cx][cy].finishedLoading();
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
			addPixelUpdate(x, y, l);
			setAD(x,y,l,ad!=null ? ad : PixelList.GetPixel(ID, l).createAD(),true);
			chunks[cx][cy].setID(x%1024, y%1024, (short) ID, l);
			
			if(!skipUpdate && (gametype== GT_SERVER || gametype == GT_CLIENT)) {
				if(ad==null)ad = chunks[cx][cy].getAD(x%1024, y%1024, l);
				mapUpdater.addUpdatePixel(new int[] {x,y,l,ID}, ad);
			}
		}
	}
	
	public void movePixelRel(int xs, int ys, int ls, int xr, int yr, int lf){
		setID(xs+xr,ys+yr,lf,getID(xs,ys,ls),getAD(xs,ys,ls),false);
		setID(xs,ys,ls,0);
	}
	
	public void movePixelAbs(int xs, int ys, int ls, int xf, int yf, int lf){
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
	
	public void setAD(int x, int y, int l, AD ad, boolean skipcheck){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			chunks[cx][cy].setAD(x, y, l, ad);
			if(!skipcheck && ad!=null) {
//				mapUpdater.addUpdateAD(new int[] {x,y,l}, ad);
			}
		}
	}

	public short getlight(int x, int y){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			short temp = chunks[cx][cy].light[x+y*1024];
			return (short) (temp<0 ? temp+256 : temp);
		}else{
			return 0;
		}
	}
	public void setlight(int x, int y, byte b){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			if(chunks[cx][cy].light[x+y*1024]!=b) {
				chunks[cx][cy].light[x+y*1024]=b;
				addLightUpdate(cx*1024+x,cy*1024+y);
			}
		}
	}

	public void setlighter(int x, int y, short b) {
		if(getlight(x,y)<b)setlight(x, y, (byte)b);
	}

	public boolean isSolid(int x, int y){
		if(getID(x, y, Map.LAYER_FRONT)!=0 && PixelList.GetMat(getID(x, y, Map.LAYER_FRONT)).solid)return true;
		else return false;
	}
	
	public int getRenderChunk(int x, int y, int l) {
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			return chunks[cx][cy].getRenderChunk(x%1024, y%1024, l);
		}else {
			loadChunk(cx, cy);
			return 0;
		}
	}
	
	public void save(){
		for(int x = 0; x < Map.widthh; x++){
			for(int y = 0; y < Map.heighth; y++){
				if(chunks[x][y]!=null)chunks[x][y].save();
			}
		}
	}
	
	public byte[] compressedChunk(int x, int y) {
		try {
			return chunks[x][y].compress();
		} catch (IOException e) {e.printStackTrace();/*dont worry, this should never ever happen C: */}
		return null;
	}
	
	public static void newMap(String path,String name){
		int n = 1;
		File dir = new File(path + File.separator +  name);
		while(dir.isDirectory()){
			n ++;
			dir = new File(path + File.separator +  name + Integer.toString(n));
		}
		dir.mkdirs();
	}
	
	public static interface ChunkManager{
		public void loadChunk(int cx, int cy);
		public void cancelChunkLoading();
	}
}