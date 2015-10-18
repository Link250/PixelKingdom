package Maps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZInputStream;
import org.tukaani.xz.XZOutputStream;

import Main.ConvertData;
import Pixels.AdditionalData;
import Pixels.Material;
import Pixels.PixelList;

public class Chunk{
	public String path;
	public final int width = 1024, height = 1024;
	public int x,y;
	private short[] front;
	private short[] mid;
	private short[] back;
	public byte[] light;
	private AdditionalData[][] AD;
	private boolean[][][] updating = new boolean[width][height][4];
	private Map map;
	
	public Chunk(String path, int x, int y, Map map){
		this.path = path;
		this.x = x;
		this.y = y;
		this.map = map;
	}
	
	public int getID(int x, int y, int layer){
		switch (layer){
		case 1 :return(front[x + y*width]);
		case 2 :return(mid[x + y*width]);
		case 3 :return(back[x + y*width]);
		default : return 0;
		}
	}
	
	public void setID(int x, int y, short ID, int layer){
		switch (layer){
		case 1 :front[x + y*width] = ID;break;
		case 2 :mid[x + y*width] = ID;break;
		case 3 :back[x + y*width] = ID;break;
		}
	}
	
	public boolean isUpdating(int x, int y, int l){
		return updating[x][y][l];
	}
	
	public boolean setUpdating(int x, int y, int l){
		if(updating[x][y][l]==false){
			updating[x][y][l]=true;
			return true;
		}else{
			return false;
		}
	}
	
	public boolean getUpdate(int x, int y, int l){
		boolean u = updating[x][y][l];
		updating[x][y][l]=false;
		return u;
	}
	
	public AdditionalData getAD(int x, int y, int layer){
		return(AD[x + y*width][layer-1]);
	}
	
	public void setAD(int x, int y, int layer, AdditionalData ad){
		AD[x + y*width][layer-1] = ad;
	}
		
	public void refreshUpdates(){
		Material m;
		int ID;
		for(int l=1; l<6; l++){
			for(int Y=y*1024; Y<y*1024+height; Y++){
				for(int X=x*1024; X<x*1024+width; X++){
					if(l<4){
						ID = getID(X%1024, Y%1024, l);
						if(ID!=0){
							if(l==2) m = PixelList.GetLiquid(ID);
							else m = PixelList.GetMat(ID);
							m.SetPos(X, Y, l);
							if(m.tick(0, map))if(map.setUpdating(X, Y, l))map.updates.addUpdate(X, Y, l);
						}
					}else{
						if(l==4){
							map.updateLight(X, Y);
						}else{
							map.updateLight(1023-X+x*2048, 1023-Y+y*2048);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	public void load(byte[] rawfile){
		ArrayList<Byte> filedata = new ArrayList<Byte>();
		front = new short[width*height];
		mid = new short[width*height];
		back = new short[width*height];
		light = new byte[width*height];
		AD = new AdditionalData[width*height][3];
		
		if(rawfile==null) {
			File file = new File(path + File.separator + "c-"+x+"-"+y+".pmap");
			InputStream fIS = null;
			if(true) {
				rawfile = new byte[(int) file.length()];
				try {
					fIS = new FileInputStream(file);
					fIS.read(rawfile);
					fIS.close();
				} catch (FileNotFoundException e) {
					create();
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
				for(int i = 0; i < rawfile.length; i++){
					filedata.add(rawfile[i]);
				}
			}else {
				try {
					fIS = new FileInputStream(file);
					fIS = new XZInputStream(fIS);
					ArrayList<byte[]> bytes = new ArrayList<byte[]>();
					int s = 0;
					byte[] b = new byte[8192];
					while((s = fIS.read(b)) == 8192)
						{bytes.add(b);b = new byte[8192];}bytes.add(b);
					fIS.close();
					s = (bytes.size()-1)*8192 + s;
					int j = 0;
					try {
						for(byte[] btemp : bytes) {
							for(int i = 0; i < btemp.length && filedata.size() < s; i++) {
								filedata.add(btemp[i]);
							}
						}
					}catch(ArrayIndexOutOfBoundsException e) {}
				}catch (FileNotFoundException e) {System.out.println("no file found");create();return;} catch (IOException e) {e.printStackTrace();}
			}
		}
		else
		for(int i = 0; i < rawfile.length; i++){
			filedata.add(rawfile[i]);
		}
		
		int x = 0,l;
		short id=0,ID;
		while(filedata.size()>0){
			try{
				ID=id;
				id = ConvertData.B2S(filedata);
//				System.out.print(id+" ");
			}catch(IndexOutOfBoundsException e){
				System.out.println((int)(x/(1024*1024))+" "+x%(1024*1024));
				break;
			}
			if(id < 0){
				ID = ConvertData.B2S(filedata);
				for(int n = 0; n < -id; n++){
					l = (int)(x/(1024*1024));
					switch(l){
					case 0 :	front[x%(1024*1024)]=ID;	break;
					case 1 :	mid[x%(1024*1024)]=ID;		break;
					case 2 :	back[x%(1024*1024)]=ID;		break;
					}
					x++;
				}
			}else{
				if(id==32767){
					(AD[(x-1)%(1024*1024)][(int)((x-1)/(1024*1024))] = new AdditionalData()).load(filedata);
//					System.out.println("AD loaded on id "+ID+" on layer "+(int)(x/(1024*1024)));
				}else{
					l = (int)(x/(1024*1024));
					switch(l){
					case 0 :	front[x%(1024*1024)]=id;	break;
					case 1 :	mid[x%(1024*1024)]=id;		break;
					case 2 :	back[x%(1024*1024)]=id;		break;
					}
					x++;
				}
			}
		}
		System.out.println("Chunk loaded at X:"+this.x+" Y:"+this.y);
//		refreshUpdates();
	}
	
	public void create(){
		short[][][] newMap;
		if(y==512){
			if(x%2==0)newMap = MapGenerator.Generate((byte)1);
			else newMap = MapGenerator.Generate((byte)2);
		}
		else{
			if(y<512)newMap = MapGenerator.Generate((byte)0);
			else newMap = MapGenerator.Generate((byte)51);
		}
		File f = new File(path + File.separator + "c-"+x+"-"+y+".pmap");
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int x = 0; x < 1024; x++){
			for(int y = 0; y < 1024; y++){
				front[y*1024+x] = newMap[0][x][y];
				mid[y*1024+x] = newMap[1][x][y];
				back[y*1024+x] = newMap[2][x][y];
			}
		}
		System.out.println("New Chunk created at X:"+x+" Y:"+y);
	}
	
	public void save(){
		try {
			FileOutputStream fos = new FileOutputStream(path + File.separator + "c-"+x+"-"+y+".pmap");
			fos.write(compress());
			fos.close();
			
//			XZOutputStream out = new XZOutputStream(fos, new LZMA2Options());
//			out.write(compress());
//			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] compress() {
		ArrayList<Byte> rawFile = new ArrayList<Byte>();
		ArrayList<short[]> layers = new ArrayList<short[]>();
		layers.add(front);
		layers.add(mid);
		layers.add(back);
		for(int l = 0; l < layers.size(); l++){
			for(int i = 0; i < layers.get(l).length; i++){
				short ID = layers.get(l)[i], n = 0;
				AdditionalData ad = AD[i][l];
				if(ad==null){for(n = 0; layers.get(l)[i+n]==ID & n < 32767 & i+n < layers.get(l).length-1; n++){}}
				if(n > 1){
					ConvertData.S2B(rawFile, (short) -n);
					i+=(n-1);
				}
				ConvertData.S2B(rawFile, ID);
				if(ad!=null){
					try{
						ad.save(rawFile);
					}catch(NullPointerException e){
						System.out.println("Error with "+ID);
					}
				}
			}
		}

		byte[] newFile = new byte[rawFile.size()];
		for(int i = 0; i < newFile.length; i++){newFile[i]=rawFile.get(i);}
		
		return newFile;
	}
}
