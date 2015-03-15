package Pixels;

import Maps.Map;

public class Lava extends Liquid{

	public Lava(){
		ID = 2;
		name = "Lava";
	}
	
	public boolean tick(int numTick, Map map) {
		map.setlighter(x, y, (byte) (Math.random()*4+60));
		if(numTick%3==0)flow(map);
		
		AdditionalData ad = map.getAD(x, y, l);
		if(ad!=null){
			if(ad.getshort(1)==0){
				map.setID(x, y, 0, 2);
				map.setID(x, y, ad.getshort(0), 1);
			}else{
				ad.setshort(1, (short)(ad.getshort(1)-1));
			}
		}
		if(map.getID(x+1, y, l)==1|map.getID(x-1, y, l)==1|map.getID(x, y+1, l)==1|map.getID(x, y-1, l)==1){
			if(ad!=null){
				map.setID(x, y, 0, 2);
				map.setID(x, y, ad.getshort(0), 1);
			}else{
				map.setID(x, y, 0, 2);
				map.setID(x, y, 1, 1);
			}
		}
		return true;
	}
	
	public void setMat(int ID, Map map){
		createAD(x, y, l, map, 4);
		map.getAD(x, y, l).setshort(0, (short)ID);
		map.getAD(x, y, l).setshort(1, (short) 3000);
	}
}
