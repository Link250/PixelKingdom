package Maps;

import gfx.Screen;

import java.io.File;
import Pixels.AdditionalData;
import Pixels.Material;
import Pixels.PixelList;

public class Map {

	public static final int LAYER_LIGHT = 0, LAYER_FRONT = 1, LAYER_LIQUID = 2, LAYER_BACK=3;
	public String path;
	public Screen screen;
	public int width = 1024;
	public int height = 1024;
	public boolean tickrev;
	protected UpdateManager updates = new UpdateManager();
	public int updatecount = 0;

	private Chunk[][] chunks = new Chunk[width][height];
	
	public Map(String path, Screen screen){
		this.path = path;
		this.screen = screen;
	}
	
	public void tick(int tickCount){
		Material m;
		int x,y,l;
		int ID;
		int size = updates.startUpdate();
		for(int i = 0; i < size; i++){
			int[] co = updates.activate(i);
			x=co[0];y=co[1];l=co[2];
			if(l>0){
				ID = getID(x, y, l);
				if(ID!=0 & getUpdate(x,y,l)){
					if(l==2) m = PixelList.GetLiquid(ID);
					else m = PixelList.GetMat(ID);
					m.SetPos(x, y, l);
					if(m.tick(tickCount, this))addBlockUpdate(x, y, l);
				}
			}else{
				if(getUpdate(x,y,l))if(updateLight(x, y))addLightUpdate(x,y);
			}
			updatecount++;
		}

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
		for(int X = -1; X < 2; X++){
			for(int Y = -1; Y < 2; Y++){
				for(int L = 0; L < 4; L++){
					if(setUpdating(x+X, y+Y, L))updates.addUpdate(x+X, y+Y, L);
				}
			}
		}
	}
	
	public void addLightUpdate(int x, int y){
		if(setUpdating(x+1,y,0))updates.addUpdate(x+1, y, 0);
		if(setUpdating(x-1,y,0))updates.addUpdate(x-1, y, 0);
		if(setUpdating(x,y+1,0))updates.addUpdate(x, y+1, 0);
		if(setUpdating(x,y-1,0))updates.addUpdate(x, y-1, 0);
	}
	
	public boolean updateLight(int x, int y){
		byte light,tempL,c,startL;
		startL=getlight(x,y);
		if(getID(x,y,LAYER_BACK)==0){
			light = (byte) 64;
		}else{
			light=0;
			if(getID(x,y,1)==0)c=1;
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

		for(int l = 3; l >0; l--){
			for(int y = 0; y <screen.height/3; y++){
				for(int x = 0; x <screen.width/3; x++){
					X=x+screen.xOffset;Y=y+screen.yOffset;
					
					light = (byte) ((64-getlight(X,Y)));
					ID = getID(X,Y,l);
					if(ID==-1){
						loadChunk(X,Y);
						ID = getID(X,Y,l);
					}
					if(light == 64){
						screen.renderShadow(X, Y, 0xff000000);
					}else{
						if(ID!=0){
							if(l!=2)m = PixelList.GetMat(ID);
							else m = PixelList.GetLiquid(ID);
							m.SetPos(X, Y, l-1);
							m.render(this,screen,l);
						}
						if(l==1)screen.renderShadow(X, Y, ((64-getlight(X,Y))<<26));
					}
				}
			}
		}
	}
	
	public void loadChunk(int x, int y){
		int cx = x/1024,cy = y/1024;
		Chunk c = new Chunk(path, cx, cy, this);c.load();
		chunks[cx][cy]=c;
		chunks[cx][cy].refreshUpdates();
	}
	
	public int getID(int x, int y, int layer){
		int cx = x/1024,cy = y/1024;
		x %= 1024;y %= 1024;
		if(chunks[cx][cy]!=null){
			return chunks[cx][cy].getID(x, y, layer);
		}else{
			return -1;
		}
	}
	public void setID(int x, int y, int ID, int layer){
		setID(x,y,ID,layer,null);
	}
	public void setID(int x, int y, int ID, int layer, AdditionalData ad){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			addBlockUpdate(x, y, layer);
			if(ad!=null)setAD(x,y,layer,ad);
			else PixelList.GetMat(ID).createAD(x, y, layer, this);
			x %= 1024;y %= 1024;
			chunks[cx][cy].setID(x, y, (short) ID, layer);
		}
	}
	
	public void movePixel(int xs, int ys, int ls, int xf, int yf, int lf){
		if(getAD(xs,ys,ls)!=null)setID(xf,yf,getID(xs,ys,ls),lf,new AdditionalData(getAD(xs,ys,ls)));
		else setID(xf,yf,getID(xs,ys,ls),lf,null);
		setID(xs,ys,0,ls);
	}
	
	public AdditionalData getAD(int x, int y, int layer){
		int cx = x/1024,cy = y/1024;
		x %= 1024;y %= 1024;
		if(chunks[cx][cy]!=null){
			return chunks[cx][cy].getAD(x, y, layer);
		}else{
			return null;
//			Chunk c = new Chunk(path, cx, cy, this);c.load();
//			chunks[cx][cy]=c;
//			return getAD(x, y, layer);
		}
	}
	public void setAD(int x, int y, int layer, AdditionalData ad){
		int cx = x/1024,cy = y/1024;
		x %= 1024;y %= 1024;
		if(chunks[cx][cy]!=null){
			chunks[cx][cy].setAD(x, y, layer, ad);
		}
	}

	public byte getlight(int x, int y){
		int cx = x/1024,cy = y/1024;
		x %= 1024;y %= 1024;
		if(chunks[cx][cy]!=null){
			return chunks[cx][cy].light[x+y*width];
		}else{
			return 0;
		}
	}
	public void setlight(int x, int y, byte b){
		int cx = x/1024,cy = y/1024;
		x %= 1024;y %= 1024;
		if(chunks[cx][cy]!=null){
			chunks[cx][cy].light[x+y*width]=b;
		}
	}

	public void setlighter(int x, int y, byte b) {
		if(getlight(x,y)<b)setlight(x, y, b);
	}

	public boolean isSolid(int x, int y){
		if(getID(x, y, 1)!=0 & PixelList.GetMat(getID(x, y, 1)).solid)return true;
		else return false;
	}

	public void save(){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < width; y++){
				if(chunks[x][y]!=null)chunks[x][y].save();
			}
		}
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