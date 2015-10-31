package Pixels;

import Main.Game;
import Maps.Map;
import gfx.Screen;

public abstract class Material {
	
	public int ID = 0;
	public byte burnable = 0; //0-no burning; 100-max burning
	public String name = "Unnamed";
	public double usePickaxe = 0;
	public boolean tick = false;
	public boolean solid = true;
	public int adl = 0;

	
	public Material(){
		
	}
	
	public final double usePickaxe(){
		return usePickaxe;
	}

	public final String getName(){
		return name;
	}

	public final void createAD(int x, int y, int l, Map map, int adl){
		if(adl>0)map.setAD(x, y, l, new AdditionalData(adl));
		else map.setAD(x, y, l, null);
	}

	public final void createAD(int x, int y, int l, Map map){
		if(adl>0)map.setAD(x, y, l, new AdditionalData(adl));
		else map.setAD(x, y, l, null);
	}

	public final void checkAD(int x, int y, int l, Map map){
		AdditionalData temp = map.getAD(x, y, l);
		if(temp == null){
			if(adl>0){map.setAD(x, y, l, new AdditionalData(adl));Game.logError("Corrupted AD");}
		}else{
			if(adl==0){temp = null;}
		}
	}


	public boolean tick(int x, int y, int l, int numTick, Map map){return false;}

	
	public void render(int x, int y, int l, Map map, Screen screen) {
		screen.drawMaterial(x, y, ID, l);
	}
}
