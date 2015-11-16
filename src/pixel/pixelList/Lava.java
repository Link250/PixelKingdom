package pixel.pixelList;

import map.Map;
import pixel.AdditionalData;
import pixel.Liquid;

public class Lava extends Liquid{

	public Lava(){
		ID = 2;
		name = "Lava";
		viscosity = 10;
		adl = 4;
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		map.setlighter(x, y, (byte) (Math.random()*4+60));
		if(numTick%3==0)flow(x, y, l, map);
		
		AdditionalData ad = map.getAD(x, y, l);
		if(ad!=null){
			if(ad.getshort(1)==0){
				map.setID(x, y, Map.LAYER_LIQUID, 0);
				map.setID(x, y, Map.LAYER_FRONT, ad.getshort(0));
			}else{
				ad.setshort(1, (short)(ad.getshort(1)-1));
			}
		}
		if(map.getID(x+1, y, l)==1||map.getID(x-1, y, l)==1||map.getID(x, y+1, l)==1||map.getID(x, y-1, l)==1){
			if(ad!=null){
				map.setID(x, y, Map.LAYER_LIQUID, 0);
				map.setID(x, y, Map.LAYER_FRONT, ad.getshort(0));
			}else{
				map.setID(x, y, Map.LAYER_LIQUID, 0);
				map.setID(x, y, Map.LAYER_FRONT, 1);
			}
		}
		return true;
	}
	
	public void setMat(int x, int y, int l, int ID, Map map){
		map.getAD(x, y, l).setshort(0, (short) ID);
		map.getAD(x, y, l).setshort(1, (short) 3000);
	}
}
