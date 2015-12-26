package main;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import main.InputHandler.Key;
import main.conversion.ConvertData;

public class Configs {
	
	public int length;
	public File file;
	public byte[] opt;
	
	public int[] FieldPosX;
	public int[] FieldPosY;
	public int PlrCol;
	public int resX,resY;
	public byte stacktype;
	
	public Map<Integer, Key> keyConfig;

	public Configs(){
		file = new File(Game.GAME_PATH+"Options.optn");
		if(!file.exists()){
			create();
		}
		FieldPosX = new int[8];
		FieldPosY = new int[8];
		keyConfig = new TreeMap<>();
		
		load();
		
	}
	
	public void setDefaultKeys() {
		this.keyConfig.put(KeyEvent.VK_W,Game.input.up);
		this.keyConfig.put(KeyEvent.VK_S,Game.input.down);
		this.keyConfig.put(KeyEvent.VK_A,Game.input.left);
		this.keyConfig.put(KeyEvent.VK_D,Game.input.right);
		this.keyConfig.put(KeyEvent.VK_SPACE,Game.input.jump);
		this.keyConfig.put(KeyEvent.VK_SHIFT,Game.input.crouch);
		this.keyConfig.put(KeyEvent.VK_ESCAPE,Game.input.menu);
		this.keyConfig.put(KeyEvent.VK_H,Game.input.hotbar);
		this.keyConfig.put(KeyEvent.VK_I,Game.input.inv);
		this.keyConfig.put(KeyEvent.VK_C,Game.input.craft);
		this.keyConfig.put(KeyEvent.VK_E,Game.input.equip);
		this.keyConfig.put(KeyEvent.VK_F3,Game.input.debugInfo);
		this.keyConfig.put(KeyEvent.VK_F5,Game.input.debugMode);
		this.keyConfig.put(KeyEvent.VK_X,Game.input.debugPixel);
	}
	
	public void create(){
		try {
			File dir = new File(Game.GAME_PATH);
			dir.mkdir();
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FieldPosX = new int[8];
		FieldPosY = new int[8];
		for(int i = 0; i < FieldPosX.length; i++){FieldPosX[i]=10;}
		for(int i = 0; i < FieldPosY.length; i++){FieldPosY[i]=10;}
		PlrCol = 0xff02e707;
		resX = 320*3;
		resY = resX/12*9;
		stacktype = 1;
		save();
	}
	
	public void load(){
		opt = new byte[(int)file.length()];
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(opt);
			fileInputStream.close();
			Game.logInfo("Config File loaded");
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		
		try{
			int i;
			for(i = 0; i < FieldPosX.length*8; i+=8){
				FieldPosX[i/8]=ConvertData.B2I(opt[i+0],opt[i+1],opt[i+2],opt[i+3]);
				FieldPosY[i/8]=ConvertData.B2I(opt[i+4],opt[i+5],opt[i+6],opt[i+7]);
			}
			PlrCol = ConvertData.B2I(opt[i],opt[i+1],opt[i+2],opt[i+3]);i+=4;
			resX = ConvertData.B2I(opt[i],opt[i+1],opt[i+2],opt[i+3]);i+=4;
			resY = ConvertData.B2I(opt[i],opt[i+1],opt[i+2],opt[i+3]);i+=4;
			stacktype = opt[i];
		}catch(ArrayIndexOutOfBoundsException e){
			Game.logInfo("New Config File created ");
			create();
			load();
		}
	}
	
	public void save(){
		byte[] Save = new byte[FieldPosX.length*8+13];
		
		int i;
		for(i = 0; i < FieldPosX.length*8; i+=8){
			Save[i+0] = (byte) ((FieldPosX[i/8]&0xff000000) >>24);
			Save[i+1] = (byte) ((FieldPosX[i/8]&0x00ff0000) >>16);
			Save[i+2] = (byte) ((FieldPosX[i/8]&0x0000ff00) >>8);
			Save[i+3] = (byte) ( FieldPosX[i/8]&0x000000ff);
			Save[i+4] = (byte) ((FieldPosY[i/8]&0xff000000) >>24);
			Save[i+5] = (byte) ((FieldPosY[i/8]&0x00ff0000) >>16);
			Save[i+6] = (byte) ((FieldPosY[i/8]&0x0000ff00) >>8);
			Save[i+7] = (byte) ( FieldPosY[i/8]&0x000000ff);
		}
		Save[i+0] = (byte) ((PlrCol&0xff000000) >>24);
		Save[i+1] = (byte) ((PlrCol&0x00ff0000) >>16);
		Save[i+2] = (byte) ((PlrCol&0x0000ff00) >>8);
		Save[i+3] = (byte) (PlrCol&0x000000ff); i+=4;
		Save[i+0] = (byte) ((resX&0xff000000) >>24);
		Save[i+1] = (byte) ((resX&0x00ff0000) >>16);
		Save[i+2] = (byte) ((resX&0x0000ff00) >>8);
		Save[i+3] = (byte) (resX&0x000000ff); i+=4;
		Save[i+0] = (byte) ((resY&0xff000000) >>24);
		Save[i+1] = (byte) ((resY&0x00ff0000) >>16);
		Save[i+2] = (byte) ((resY&0x0000ff00) >>8);
		Save[i+3] = (byte) (resY&0x000000ff);i+=4;
		Save[i] = stacktype;
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(Save);
			fos.close();
		} catch (IOException e) {e.printStackTrace();}
	}
}