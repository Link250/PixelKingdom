package pixel.pixelList;

import gfx.Screen;
import map.Map;
import pixel.interfaces.Heatable;
import pixel.Material;
import pixel.PixelList;

public class Oven extends Material<Heatable.DataStorage> implements Heatable{
	short maxHeat = 1800;
	short cooldown = 1;
	int cooldownFrequency = 10;
	short heatup = 50;
	
	public Oven(){
		super(new DataStorage());
		ID = 64;
		name = "Oven";
		displayName = "Oven";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		uds = map.getUDS(x, y, l);
		short heat = uds.heat;
		if(numTick%6==0)heat -= exchangeHeat(x, y-1, l, heat, map, numTick);
		if(numTick%6==1)heat -= exchangeHeat(x-1, y, l, heat, map, numTick);
		if(numTick%6==2)heat -= exchangeHeat(x+1, y, l, heat, map, numTick);
		if(numTick%6==3)heat -= exchangeHeat(x, y+1, l, heat, map, numTick);
		if(numTick%6==4)heat -= exchangeHeat(x, y, Map.LAYER_LIQUID, heat, map, numTick);
		if(numTick%6==5)heat -= exchangeHeat(x, y, l == Map.LAYER_BACK ? Map.LAYER_FRONT : Map.LAYER_BACK, heat, map, numTick);
		if(uds.heat!=heat)map.addUDSUpdate(x, y, l, uds);
		uds.heat = heat;
		return uds.heat>0;
	}
	
	private int exchangeHeat(int x, int y, int l, int heat, Map map, int numTick) {
		Material<?> m = PixelList.GetPixel(map.getID(x, y, l), l);
		if(m.ID!=0)
			return m instanceof Heatable ? ((Heatable)m).heatUp(x, y, l, heat, heat>=heatup ? heatup : heat/2, map) : 0;
		else
			return (heat>0) ? cooldown : 0;
	}
	
 /*	public boolean tick(int x, int y, int l, int numTick, Map map) {
		uds = map.getUDS(x, y, l);
		short heat = uds.heat;
		int id;
		for(int X=-1; X<=1; X++){
			for(int Y=-1; Y<=1; Y++){
				for(int L : Map.LAYER_ALL_PIXEL){
					id = map.getID(x+X, y+Y, L);
					if(id==0) {
						if((numTick%60==20|| numTick%60==41) && (L==l || (X==0 && Y==0)) && uds.heat>0) {
							if(uds.heat>=cooldown) {
								uds.heat -= cooldown;
							}else {
								uds.heat = 0;
							}
						}continue;
					}
					if(id==32){
						if(uds.heat<maxHeat) {
							uds.heat+=heatup;
						}continue;
					}
					if(id==64){
						adOven = map.getUDS(x+X, y+Y, L);
						if(uds.heat>exchange && uds.heat>adOven.heat){
							uds.heat -= exchange;
							adOven.heat += exchange;
						}continue;
					}
					if(X==0 && Y==0){
						if(PixelList.GetMat(id) instanceof Ore) {
							ore = (Ore<?>) PixelList.GetMat(id);
							adOre = map.getUDS(x+X, y+Y, L);
							if(ore.melt>0){
								ore.heatUp(x+X, y+Y, L, uds.heat,(short) 1,map);
							}
						}
					}
				}
			}
		}
		if(uds.heat!=heat)map.addUDSUpdate(x, y, l, uds);
		return uds.heat>0;
	}*/
	
	public int getMaxHeat() {return maxHeat;}
	
	public short tickLight(int x, int y, int l, Map map) {
		return (short) (map.<DataStorage>getUDS(x, y, l).heat/(maxHeat/16));
	}
	
	public int render(int x, int y, int l, Map map) {
		int r = (int) (map.<DataStorage>getUDS(x, y, l).heat/(((double)maxHeat)/127));if(r>255)r=255;
		return Screen.combineColors(texture[x%textureWidth + (y%textureHeight)*textureWidth], 0x00ff0000 | (r<<24));
	}
}
