package pixel.pixelList;

import map.Map;
import pixel.AD;
import pixel.Liquid;

public class Lava extends Liquid<Lava.LavaAD>{

	public Lava(){
		super(new LavaAD());
		ID = 2;
		name = "Lava";
		displayName = "Lava";
		viscosity = 10;
		frontLightReduction = 0;
		backLightReduction = 0;
		loadTexture();
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		if(numTick%3==0)flow(x, y, l, map);
		
		ad = map.getAD(x, y, l);
		if(ad!=null){
			if(ad.heat==0){
				map.setID(x, y, Map.LAYER_LIQUID, 0);
				map.setID(x, y, Map.LAYER_FRONT, ad.ID);
			}else{
				if(numTick%60==0) {
					ad.heat--;
					map.addADUpdate(x, y, l, ad);
				}
			}
		}
		if(map.getID(x+1, y, l)==1||map.getID(x-1, y, l)==1||map.getID(x, y+1, l)==1||map.getID(x, y-1, l)==1){
			if(ad!=null){
				map.setID(x, y, Map.LAYER_LIQUID, 0);
				map.setID(x, y, Map.LAYER_FRONT, ad.ID);
			}else{
				map.setID(x, y, Map.LAYER_LIQUID, 0);
				map.setID(x, y, Map.LAYER_FRONT, 1);
			}
		}
		return true;
	}
	
	public short tickLight(int x, int y, int l, Map map) {
		return (short) (Map.MAX_LIGHT*0.6);
	}
	
	public void setMat(int x, int y, int l, int ID, Map map){
		ad = map.getAD(x, y, l);
		ad.ID = (short) ID;
		ad.heat = 60;
	}
	
	public static class LavaAD extends AD {
		public short ID;
		public byte heat;
	}
}
