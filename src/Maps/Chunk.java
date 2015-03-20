package Maps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Main.ConvertData;
import Pixels.AdditionalData;

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
	private byte[] rawfile;
	
	public Chunk(String path, int x, int y, Map map){
		this.path = path;
		this.x = x;
		this.y = y;
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

	public void load(){
		File file = new File(path + File.separator + "c-"+x+"-"+y+".pmap");
		FileInputStream fileInputStream = null;
		rawfile = new byte[(int) file.length()];
		ArrayList<Byte> filedata = new ArrayList<Byte>();
		front = new short[width*height];
		mid = new short[width*height];
		back = new short[width*height];
		light = new byte[width*height];
		AD = new AdditionalData[width*height][3];
		
		try {
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(rawfile);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			create();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		for(int i=0; i<light.length; i++)if(back[i]==0)light[i]=64;
		System.out.println("Chunk loaded at X:"+this.x+" Y:"+this.y);
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

		try {
			FileOutputStream fos = new FileOutputStream(path + File.separator + "c-"+x+"-"+y+".pmap");
			fos.write(newFile);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
