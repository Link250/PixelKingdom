package Pixels;

import gfx.Screen;
import Maps.Map;

public class Oven extends Material{

	short maxHeat = 1000;
	short exchange = 5;
	short cooldown = 5;
	short heatup = 10;
	
	public Oven(){
		ID = 64;
		name = "Oven";
		usePickaxe = 1;
		tick = true;
		adl = 2;
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		AdditionalData ad, ado;
		ad = map.getAD(x, y, l);
		map.setlighter(x, y, (byte) (ad.getshort(0)/(maxHeat/16)));
		int id;
		Ore ore;
		for(int X=-1; X<=1; X++){
			for(int Y=-1; Y<=1; Y++){
				for(int L : Map.LAYER_ALL_PIXEL){
					id = map.getID(x+X, y+Y, L);
					if(id==32){
						if(ad.getshort(0)<maxHeat)ad.setshort(0, (short) (ad.getshort(0)+heatup));
					}
					if(id==64){
						ado = map.getAD(x+X, y+Y, L);
						if(ad.getshort(0)>ado.getshort(0) && ad.getshort(0)>5){
							ad.setshort(0, (short) (ad.getshort(0)-exchange));
							ado.setshort(0, (short) (ado.getshort(0)+exchange));
						}
					}
					if(X==0 & Y==0){
						try{
							ore = (Ore) PixelList.GetMat(id);
							ado = map.getAD(x+X, y+Y, L);
							if(ore.melt>0){
								ore.heatUp(x+X, y+Y, L, ad.getshort(0),(short) 1,map);
							}
						}catch(ClassCastException e){}
					}
					
					if(id==0 & (numTick%60==20| numTick%60==41) & ad.getshort(0)>2 & L==l)ad.setshort(0, (short) (ad.getshort(0)-cooldown));
				}
			}
		}
		return true;
	}
	
	public void render(int x, int y, int l, Map map, Screen screen) {
		screen.drawMaterial(x, y, ID, l);
		int r = (int) (map.getAD(x, y, l).getshort(0)/(((double)maxHeat)/127));if(r>255)r=255;
		screen.drawPixelScaled(x, y, 0x00ff0000 | (r<<24));
	}
}
