package map;

import gfx.Screen;
import item.Item;
import item.Tool;
import multiplayer.MapUpdater;
import multiplayer.client.ChunkManagerC;
import multiplayer.client.ServerManager;
import pixel.UDS;
import pixel.Material;
import pixel.PixelList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import entities.Entity;
import entities.Mob;
import entities.entityList.ItemEntity;

public class Map {

	public static final byte LAYER_BACK=0, LAYER_LIQUID = 1, LAYER_FRONT = 2, LAYER_LIGHT = 3,
			GT_SP=0,GT_CLIENT=1,GT_SERVER=2;
	public static final short MAX_LIGHT = 255;
	public static final byte[] LAYER_ALL = {LAYER_BACK,LAYER_LIQUID,LAYER_FRONT,LAYER_LIGHT},
			LAYER_ALL_PIXEL = {LAYER_BACK,LAYER_LIQUID,LAYER_FRONT},
			LAYER_ALL_MATERIAL = {LAYER_BACK,LAYER_FRONT};
	public static final int widthh = 1024, heighth = 1024;
	public String path;
	protected UpdateManager updatesPixel = new UpdateManager();
	public int updateCountPixel = 0;
	protected UpdateManager updatesLight = new UpdateManager();
	public int updateCountLight = 0;
	private ChunkManager chunkMaganer;
	private int gametype = 0;
	private MapUpdater mapUpdater;

	private ArrayList<Mob> mobEntityList = new ArrayList<>();
	private ArrayList<Entity> entityList = new ArrayList<>();
	private ArrayList<ItemEntity> itemEntityList = new ArrayList<>();
	private Chunk[][] chunks = new Chunk[1024][1024];
	
	private int regularUpdateX = -Screen.width/2-Screen.RENDER_CHUNK_SIZE;
	private int regularUpdateY = -Screen.height/2-Screen.RENDER_CHUNK_SIZE;
	
	public Map(String path){
		this.path = path;
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
		this.mobEntityList.forEach(e->e.tick(tickCount));
		this.itemEntityList.forEach(e->e.tick(tickCount));
		this.entityList.forEach(e->e.tick(tickCount));
		
		for (int i = this.itemEntityList.size()-1; i >= 0; i--) {
			if(itemEntityList.get(i).item.getStack()<=0) {
				itemEntityList.remove(i);
			}else {
				itemEntityList.get(i).combine(itemEntityList);
			}
		}
		
		
		if(tickCount%4==0) {
			this.mobEntityList.forEach(e->e.applyGravity());
			this.entityList.forEach(e->e.applyGravity());
			this.itemEntityList.forEach(e->e.applyGravity());
		}
		
		
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
					/*dont worry, can happen in MP if some UDS Objects are not loaded*/
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
		if(gametype!=GT_SERVER)setTextureUpdating(regularUpdateX+Screen.xOffset, regularUpdateY+Screen.yOffset, Map.LAYER_LIGHT);
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
	
	public void addUDSUpdate(int x, int y, int l, UDS uds){
		if(gametype == GT_SERVER || gametype == GT_CLIENT) {
			mapUpdater.addUpdateUDS(new int[]{x,y,l}, uds);
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
	
	public void renderMobs(){
		mobEntityList.forEach(e->e.render());
	}
	
	public void renderEntities(){
		entityList.forEach(e->e.render());
	}
	
	public void renderItemEntities(){
		itemEntityList.forEach(e->e.render());
	}
	
	public void addMobEntity(Mob mob) {
		this.mobEntityList.add(mob);
	}
	
	public void addItemEntity(ItemEntity e) {
		this.itemEntityList.add(e);
	}
	
	public void spawnItemEntity(Item item, int x, int y) {
		this.itemEntityList.add(new ItemEntity(item, this, x, y));
	}
	
	public void addEntity(Entity e) {
		this.entityList.add(e);
	}
	
	public List<Entity> getEntities(Predicate<Entity> area) {
		List<Entity> list = new LinkedList<>();
		this.entityList.stream().filter(area).forEach(e->list.add(e));
		return list;
	}
	
	public List<ItemEntity> getItemEntities(Predicate<ItemEntity> area) {
		List<ItemEntity> list = new LinkedList<>();
		this.itemEntityList.stream().filter(area).filter(e->e.item.getStack()>0).forEach(e->list.add(e));
		return list;
	}
	
	public void removeMob(Mob mob) {
		this.mobEntityList.remove(mob);
	}
	
	public void removeEntitiy(Entity e) {
		this.entityList.remove(e);
	}
	
	public void removeItemEntitiy(ItemEntity e) {
		this.itemEntityList.remove(e);
	}
	
	public List<Mob> getMobs(Predicate<Mob> area) {
		List<Mob> list = new LinkedList<>();
		this.mobEntityList.stream().filter(area).forEach(e->list.add(e));
		return list;
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
	
	public void setID(int x, int y, int l, int ID, UDS uds, boolean skipUpdate){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			addPixelUpdate(x, y, l);
			setUDS(x,y,l,uds!=null ? uds : PixelList.GetPixel(ID, l).createUDS(),true);
			chunks[cx][cy].setID(x%1024, y%1024, (short) ID, l);
			
			if(!skipUpdate && (gametype== GT_SERVER || gametype == GT_CLIENT)) {
				if(uds==null)uds = chunks[cx][cy].getUDS(x%1024, y%1024, l);
				mapUpdater.addUpdatePixel(new int[] {x,y,l,ID}, uds);
			}
		}
	}
	
	public void breakPixel(int x, int y, int l, Tool tool){
		Material<?> m = PixelList.GetPixel(getID(x, y, l), l);
		if(tool.)
	}
	
	public void movePixelRel(int xs, int ys, int ls, int xr, int yr, int lf){
		setID(xs+xr,ys+yr,lf,getID(xs,ys,ls),getUDS(xs,ys,ls),false);
		setID(xs,ys,ls,0);
	}
	
	public void movePixelAbs(int xs, int ys, int ls, int xf, int yf, int lf){
		setID(xf,yf,lf,getID(xs,ys,ls),getUDS(xs,ys,ls),false);
		setID(xs,ys,ls,0);
	}
	
	public <UDSType extends UDS> UDSType getUDS(int x, int y, int layer){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			return chunks[cx][cy].getUDS(x, y, layer);
		}else{
			return null;
		}
	}
	
	public void setUDS(int x, int y, int l, UDS uds, boolean skipcheck){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			x %= 1024;y %= 1024;
			chunks[cx][cy].setUDS(x, y, l, uds);
			if(!skipcheck && uds!=null) {
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