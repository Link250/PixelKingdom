package Pixels;

import Maps.Map;

public class Sand extends Material{

	public Sand(){
		ID = 8;
		name = "Sand";
		usePickaxe = 1;
		tick = true;
	}
	
	public void tick(int numTick, Map map) {
		if(map.getID(x,y+1,l)==0){
			map.movePixel(x, y, l, x, y+1, l);
			y++;
			return;
		}
		if(map.tickrev){
			if(map.getID(x+1,y,l)==0 & map.getID(x+1,y+1,l)==0){
				map.movePixel(x, y, l, x+1, y+1, l);
				y++;x++;
				return;
			}
		}else{
			if(map.getID(x-1,y,l)==0 & map.getID(x-1,y+1,l)==0){
				map.movePixel(x, y, l, x-1, y+1, l);
				y++;x--;
				return;
			}
		}
	}

}
