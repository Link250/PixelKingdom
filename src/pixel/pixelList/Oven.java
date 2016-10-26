package pixel.pixelList;

import map.Map;
import pixel.Material;
import pixel.Ore;
import pixel.PixelList;
import pixel.ads.OreAD;
import pixel.ads.OvenAD;

public class Oven extends Material<OvenAD>{
	OvenAD adOven;
	OreAD adOre;
	Ore<?> ore;

	short maxHeat = 1000;
	short exchange = 5;
	short cooldown = 5;
	short heatup = 10;
	
	public Oven(){
		super(new OvenAD());
		ID = 64;
		name = "Oven";
		displayName = "Oven";
		usePickaxe = 1;
		tick = true;
		loadTexture();
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		ad = map.getAD(x, y, l);
		short heat = ad.heat;
		int id;
		for(int X=-1; X<=1; X++){
			for(int Y=-1; Y<=1; Y++){
				for(int L : Map.LAYER_ALL_PIXEL){
					id = map.getID(x+X, y+Y, L);
					if(id==0) {
						if((numTick%60==20|| numTick%60==41) && (L==l || (X==0 && Y==0)) && ad.heat>0) {
							if(ad.heat>=cooldown) {
								ad.heat -= cooldown;
							}else {
								ad.heat = 0;
							}
						}continue;
					}
					if(id==32){
						if(ad.heat<maxHeat) {
							ad.heat+=heatup;
						}continue;
					}
					if(id==64){
						adOven = map.getAD(x+X, y+Y, L);
						if(ad.heat>exchange && ad.heat>adOven.heat){
							ad.heat -= exchange;
							adOven.heat += exchange;
						}continue;
					}
					if(X==0 && Y==0){
						if(PixelList.GetMat(id) instanceof Ore) {
							ore = (Ore<?>) PixelList.GetMat(id);
							adOre = map.getAD(x+X, y+Y, L);
							if(ore.melt>0){
								ore.heatUp(x+X, y+Y, L, ad.heat,(short) 1,map);
							}
						}
					}
				}
			}
		}
		if(ad.heat!=heat)map.addADUpdate(x, y, l, ad);
		return ad.heat>0;
	}
	
	public short tickLight(int x, int y, int l, Map map) {
		return (short) (map.<OvenAD>getAD(x, y, l).heat/(maxHeat/16));
	}
	
	public int render(int x, int y, int l, Map map) {
		return super.render(x, y, l, map);
//		screen.drawMaterial(x, y, ID, l);
//		int r = (int) (map.<OvenAD>getAD(x, y, l).heat/(((double)maxHeat)/127));if(r>255)r=255;
//		screen.drawMapPixelScaled(x, y, 0x00ff0000 | (r<<24));
	}
}
