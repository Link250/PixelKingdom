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
	private UpdateManager updates = new UpdateManager();
	public int updatecount = 0;
//	private ArrayList<int[]> updateold = new ArrayList<int[]>();

	private Chunk[][] chunks = new Chunk[width][height];
	
	public Map(String path, Screen screen){
		this.path = path;
		this.screen = screen;
	}
	
	public void tick(int tickCount){
		Material m;
		int X,Y,c,l;
		int ID;
		byte light,tempL;
		int size = updates.startUpdate();
		for(int i = 0; i < size; i++){
			int[] co = updates.activate(i);
			X=co[0];Y=co[1];l=co[2];
			ID = getID(X, Y, l);
			if(ID!=0 & getUpdate(X,Y,l)){
				if(l==2) m = PixelList.GetLiquid(ID);
				else m = PixelList.GetMat(ID);
				m.SetPos(X, Y, l);
				if(m.tick(tickCount, this))updateBlock(X, Y, l);
			}
			updatecount++;
//			if(SinglePlayer.debuginfo)updateold.add(co);
		}
		for(int y = 0; y <screen.height/3; y++){
			for(int x = 0; x <screen.width/3; x++){
//				if(System.currentTimeMillis()%1000==0)System.out.println(updates.length());
				if(tickCount%2==0){
					X = x+screen.xOffset;
					Y = y+screen.yOffset;
					tickrev = false;
				}else{
					X = screen.width/3-x+screen.xOffset;
					Y = screen.height/3-y+screen.yOffset;
					tickrev = true;
				}

				setlight(X,Y,(byte) 0);

/*				ID = getID(X,Y,3);
				if(ID!=0){
					m = PixelList.GetMat(ID);
					if(m.tick){m.SetPos(X, Y, 3);m.tick(tickCount, this);}
				}

				ID = getID(X,Y,2);
				if(ID!=0){
					m = PixelList.GetLiquid(ID);
					if(m.tick){m.SetPos(X, Y, 2);m.tick(tickCount, this);}
				}

*/				ID = getID(X,Y,1);/*
				if(ID!=0){
					m = PixelList.GetMat(ID);
					if(m.tick){m.SetPos(X, Y, 1);m.tick(tickCount, this);}
				}
				*/
				if(getID(X,Y,3)==0){
					light = (byte) 64;
				}else{
					light = getlight(X,Y);
					if(ID==0)c=1;
					else c=2;
					if(x<width/3-1	&& (tempL = getlight(X+1,Y))>light)light = (byte) (tempL-c);
					if(x>0			&& (tempL = getlight(X-1,Y))>light)light = (byte) (tempL-c);
					if(y<height/3-1	&& (tempL = getlight(X,Y+1))>light)light = (byte) (tempL-c);
					if(y>0			&& (tempL = getlight(X,Y-1))>light)light = (byte) (tempL-c);
					if(light<0)light = 0;
				}
				
				setlight(X,Y,light);
			}
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
					if(light == 64){
						screen.renderShadow(X, Y, 0xff000000);
					}else{
						ID = getID(X,Y,l);
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
//		if(SinglePlayer.debuginfo){
//			while(updateold.size()>0){
//				int[] co = updateold.remove(0);
//				X=co[0];Y=co[1];
//				screen.renderGUIScaled((X), (Y), 0xffff0000);
//			}
//		}
	}
	
	public int getID(int x, int y, int layer){
		int cx = x/1024,cy = y/1024;
		x %= 1024;y %= 1024;
		if(chunks[cx][cy]!=null){
			return chunks[cx][cy].getID(x, y, layer);
		}else{
			Chunk c = new Chunk(path, cx, cy, this);c.load();
			chunks[cx][cy]=c;
			return getID(x, y, layer);
		}
	}
	public void setID(int x, int y, int ID, int layer){
		setID(x,y,ID,layer,null);
	}
	public void setID(int x, int y, int ID, int layer, AdditionalData ad){
		int cx = x/1024,cy = y/1024;
		if(chunks[cx][cy]!=null){
			updateBlock(x, y, layer);
			if(ad!=null)setAD(x,y,layer,ad);
			else PixelList.GetMat(ID).createAD(x, y, layer, this);
			x %= 1024;y %= 1024;
			chunks[cx][cy].setID(x, y, (short) ID, layer);
		}
	}
	
	public void updateBlock(int x, int y, int l){
		for(int X = -1; X < 2; X++){
			for(int Y = -1; Y < 2; Y++){
				for(int L = 1; L < 4; L++){
					if(setUpdating(x+X, y+Y, L))updates.addUpdate(x+X, y+Y, L);
				}
			}
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
			Chunk c = new Chunk(path, cx, cy, this);c.load();
			chunks[cx][cy]=c;
			return getAD(x, y, layer);
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
			Chunk c = new Chunk(path, cx, cy, this);c.load();
			chunks[cx][cy]=c;
			return getlight(x, y);
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