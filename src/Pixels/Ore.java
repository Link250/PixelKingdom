package Pixels;

import gfx.Screen;
import Maps.Map;

public abstract class Ore extends Material{

	public int melt;
	public int ingot;
	
	public Ore(){
		ID = 15;
		name = "Ore";
		usePickaxe = 1;
		tick = false;
	}

	public void heatUp(short heat, short heatup, Map map){
		AdditionalData ad = map.getAD(x, y, l);
		if(ad==null){
			map.setAD(x, y, l, new AdditionalData(2));
			ad = map.getAD(x, y, l);
		}
		if(ad.getshort(0)>=melt){
			map.setID(x, y, l, 0);
			map.setID(x, y, 2, 2);
			((Lava)PixelList.GetMat(x, y, map, 2)).setMat(ingot, map);
		}else{
			if(heat>=ad.getshort(0))ad.setshort(0, (short)(ad.getshort(0)+heatup));
		}
	}
	
	public void render(Map map, Screen screen, int layer) {
		screen.renderMaterial(x, y, ID, layer);
		AdditionalData ad = map.getAD(x, y, l+1);
		if(melt>0 && ad!=null){
			int r = (int) (ad.getshort(0)/(((double)melt)/250));if(r>255)r=255;
			screen.renderPixelScaled(x, y, 0x00ff0000 | (r<<24));
		}
	}
}
