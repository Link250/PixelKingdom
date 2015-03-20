package Pixels;

import Maps.Map;

public class Sand extends Material{

	public Sand(){
		ID = 8;
		name = "Sand";
		usePickaxe = 1;
		tick = true;
	}
	
	public boolean tick(int numTick, Map map) {
		if(map.getID(x,y+1,l)==0){
			map.movePixel(x, y, l, x, y+1, l);
			y++;
			return true;
		}
		if(Math.random()<0.5){
			if(falltoside(-1, map))return true;
		}else{
			if(falltoside(1, map))return true;
		}
		return false;
	}
	
	private boolean falltoside(int side, Map map){
		if(map.getID(x+side,y,l)==0 & map.getID(x+side,y+1,l)==0){
			map.movePixel(x, y, l, x+side, y+1, l);
			y++;x--;
			return true;
		}return false;
	}

}
