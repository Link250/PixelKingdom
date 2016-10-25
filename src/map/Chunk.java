package map;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.tukaani.xz.XZInputStream;

import dataUtils.conversion.ConverterQueue;
import gfx.Screen;
import main.Game;
import pixel.AD;
import pixel.Material;
import pixel.PixelList;

public class Chunk{
	public String path;
	/**the base 2 log of width/height -> used for bitshifts*/
	public static final int wLog = 10,
							hLog = 10;
	/**the size of a chunk. will be a base 2 potent*/
	public static final int width = (int) Math.pow(2, wLog),
							height = (int) Math.pow(2, hLog);
	/**the bits that can be covered by width/height -> used for & and | */
	public static final int wBit = width-1,
							hBit = height-1;
	/**the total 1D length of a chunk*/
	public static final int length = height*width;
	public int x,y;
	private short[] front;
	private short[] liquid;
	private short[] back;
	public byte[] light;
//	private AdditionalData[][] AD;
	private HashMap<Integer,AD> AD = new HashMap<>();
	private boolean[][][] updating = new boolean[width][height][Map.LAYER_ALL.length];
	private Map map;
	
	private int[][][] textureChunks;
	private boolean[][][] textureUpdates;
	
	private boolean finishedLoading = false;
	
	public Chunk(String path, int x, int y, Map map){
		this.path = path;
		this.x = x;
		this.y = y;
		this.map = map;
		this.textureChunks = new int[width/Screen.RENDER_CHUNK_SIZE][height/Screen.RENDER_CHUNK_SIZE][Map.LAYER_ALL.length];
		this.textureUpdates = new boolean[width/Screen.RENDER_CHUNK_SIZE][height/Screen.RENDER_CHUNK_SIZE][Map.LAYER_ALL.length];
	}
	
	public int getID(int x, int y, int layer){
		switch (layer){
		case Map.LAYER_FRONT :return(front[x + y*width]);
		case Map.LAYER_LIQUID :return(liquid[x + y*width]);
		case Map.LAYER_BACK :return(back[x + y*width]);
		default : return 0;
		}
	}
	
	public void setID(int x, int y, short ID, int layer){
		switch (layer){
		case Map.LAYER_FRONT :front[x + y*width] = ID;break;
		case Map.LAYER_LIQUID :liquid[x + y*width] = ID;break;
		case Map.LAYER_BACK :back[x + y*width] = ID;break;
		}
	}
	
	public boolean isUpdating(int x, int y, int l){
		return updating[x%width][y%height][l];
	}
	
	public boolean setUpdating(int x, int y, int l){
		if(!updating[x][y][l]){
			updating[x][y][l]=true;
			textureUpdates[x/Screen.RENDER_CHUNK_SIZE][y/Screen.RENDER_CHUNK_SIZE][l] = true;
			return true;
		}else{
			return false;
		}
	}
	
	public boolean getUpdate(int x, int y, int l){
		if(updating[x%width][y%height][l]) {
			updating[x%width][y%height][l]=false;
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public <ADType> ADType getAD(int x, int y, int l){
		return (ADType) this.AD.get(l*length+y*width+x);
//		return(AD[x + y*width][layer]);
	}
	public AD getAD(int xy, int l){
		return this.AD.get(l*length+xy);
	}
	
	public void setAD(int x, int y, int l, AD ad){
		this.AD.put(l*length+y*width+x, ad);
//		AD[x + y*width][layer] = ad;
	}
	public void setAD(int xy, int l, AD ad){
		this.AD.put(l*length+xy, ad);
	}
		
	public void refreshUpdates(){
		Material<?> m;
		int ID;
		for(int l : Map.LAYER_ALL){
			for(int Y=y*1024; Y<y*1024+height; Y++){
				for(int X=x*1024; X<x*1024+width; X++){
					if(l!=Map.LAYER_LIGHT){
						ID = getID(X%1024, Y%1024, l);
						if(ID!=0){
							if(l==Map.LAYER_LIQUID) m = PixelList.GetLiquid(ID);
							else m = PixelList.GetMat(ID);
							if(m.tick(X, Y, l, 0, map))if(map.setUpdating(X, Y, l))map.updatesPixel.addUpdate(X, Y, l);
						}
					}else{
						map.updateLight(X, Y);
						//inverted directions for Light Updates
						map.updateLight(1023-X+x*2048, 1023-Y+y*2048);
					}
				}
			}
		}
	}
	
	public void refreshLight(){
		for(int Y=y*1024; Y<y*1024+height; Y++){
			for(int X=x*1024; X<x*1024+width; X++){
				map.updateLight(X, Y);
				//inverted directions for Light Updates
				map.updateLight(1023-X+x*2048, 1023-Y+y*2048);
			}
		}
	}
	
	@SuppressWarnings("unused")
	public void load(byte[] rawfile) throws IOException{
		ConverterQueue filedata = new ConverterQueue();
		front = new short[length];
		liquid = new short[length];
		back = new short[length];
		light = new byte[length];
		AD = new HashMap<>();
		
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
					filedata.writeByte(rawfile[i]);
				}
			}else{
				try{
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
							for(int i = 0; i < btemp.length && filedata.length() < s; i++) {
								filedata.writeByte(btemp[i]);
							}
						}
					}catch(ArrayIndexOutOfBoundsException e) {}
				}catch (FileNotFoundException e) {Game.logWarning("no file found");create();return;} catch (IOException e) {e.printStackTrace();}
			}
		}
		else
		for(int i = 0; i < rawfile.length; i++){
			filedata.writeByte(rawfile[i]);
		}
		
		int x = 0,l;
		short id=0,ID;
		while(filedata.length()>0){
			try{
				ID=id;
				id = filedata.readShort();
			}catch(NullPointerException e){
				Game.logWarning((int)(x/length)+" "+x%length);
				break;
			}
			if(id < 0){
				ID = filedata.readShort();
				if(ID==0) {
					x-=id;
					continue;
				}
				for(int n = 0; n < -id; n++){
					l = (int)(x/length);
					switch(l){
					case Map.LAYER_FRONT :	front[x%length]=ID;		break;
					case Map.LAYER_LIQUID :	liquid[x%length]=ID;	break;
					case Map.LAYER_BACK :	back[x%length]=ID;		break;
					}
					x++;
				}
			}else{
				if(id==0x7fff){
					setAD((x-1)%length, (int)((x-1)/length), PixelList.GetPixel(ID, (x/length)).getNewAD().load(filedata));
				}else{
					l = (x/length);
					switch(l){
					case Map.LAYER_FRONT :	front[x%length]=id;		break;
					case Map.LAYER_LIQUID :	liquid[x%length]=id;	break;
					case Map.LAYER_BACK :	back[x%length]=id;		break;
					}
					x++;
				}
			}
		}
		Game.logInfo("Chunk loaded at X:"+this.x+" Y:"+this.y);
		this.finishedLoading = true;
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
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				front[y*width+x] = newMap[0][x][y];
				liquid[y*width+x] = newMap[1][x][y];
				back[y*width+x] = newMap[2][x][y];
			}
		}
		Game.logInfo("New Chunk created at X:"+x+" Y:"+y);
		this.finishedLoading = true;
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

	public byte[] compress() throws IOException {
		ConverterQueue data = new ConverterQueue();
		ArrayList<short[]> layers = new ArrayList<short[]>();
		layers.add(back);
		layers.add(liquid);
		layers.add(front);
		for(int l = 0; l < layers.size(); l++){
			for(int i = 0; i < layers.get(l).length; i++){
				short ID = layers.get(l)[i], n = 0;
				AD ad = getAD(i,l);
				if(ad==null){for(n = 0; layers.get(l)[i+n]==ID && n < 0x7fff && i+n < layers.get(l).length-1; n++){}}
				if(n > 1){
					data.writeShort((short)-n);
					i+=(n-1);
				}
				data.writeShort(ID);
				if(ad!=null){
					try{
						ad.save(data,true);
					}catch(NullPointerException e){
						Game.logError("Error with "+ID);
					}
				}
			}
		}

		byte[] newFile = new byte[data.length()];
		for(int i = 0; i < newFile.length; i++){newFile[i]=data.readByte();}
		
		return newFile;
	}
	
	public boolean finishedLoading() {
		return this.finishedLoading;
	}
	
	public int getRenderChunk(int x, int y, int l) {
		x/=Screen.RENDER_CHUNK_SIZE;y/=Screen.RENDER_CHUNK_SIZE;
		int i = this.textureChunks[x][y][l];
		if(i == 0 || this.textureUpdates[x][y][l])genTextures(x,y,l);
		return i;
	}
	
	private void genTextures(int xPos, int yPos, int l){
		if(this.textureChunks[xPos][yPos][l]>0) {
			glDeleteTextures(this.textureChunks[xPos][yPos][l]);
		}
		int ID;
		int X,Y;
		int pixel;
		short lightP;
		ByteBuffer pixelBuffer = BufferUtils.createByteBuffer(Screen.RENDER_CHUNK_SIZE * Screen.RENDER_CHUNK_SIZE * 4);
		for (int y = 0; y < Screen.RENDER_CHUNK_SIZE; y++) {
			for (int x = 0; x < Screen.RENDER_CHUNK_SIZE; x++) {
				X=x+xPos*Screen.RENDER_CHUNK_SIZE;Y=y+yPos*Screen.RENDER_CHUNK_SIZE;
				if(l==Map.LAYER_LIGHT) {
					lightP = (light[X+Y*width]);
					pixelBuffer.put((byte)0); //RED
					pixelBuffer.put((byte)0);  //GREEN
					pixelBuffer.put((byte)0);		  //BLUE
					pixelBuffer.put((byte)(lightP == 0 ? 0xff : (Map.MAX_LIGHT-lightP))); //ALPHA
				}else {
					ID = getID(X,Y,l);
					if(ID>=0){
						pixel = ID == 0 ? 0 : PixelList.GetPixel(ID, l).render(X+this.x*Chunk.width, Y+this.y*Chunk.height, l, map);
						pixelBuffer.put((byte)((pixel >> 16) & 0xFF)); //RED
						pixelBuffer.put((byte)((pixel >> 8) & 0xFF));  //GREEN
						pixelBuffer.put((byte)(pixel & 0xFF));		  //BLUE
						pixelBuffer.put((byte)((pixel >> 24) & 0xFF)); //ALPHA
					}
				}
			}
		}
		pixelBuffer.flip();
		this.textureChunks[xPos][yPos][l] = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, this.textureChunks[xPos][yPos][l]);
		
//		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, l==Map.LAYER_LIGHT ? GL_LINEAR : GL_NEAREST);
//		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, l==Map.LAYER_LIGHT ? GL_LINEAR : GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Screen.RENDER_CHUNK_SIZE, Screen.RENDER_CHUNK_SIZE, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);
		this.textureUpdates[xPos][yPos][l] = false;
	}
}
