package Pixels;

import Maps.Map;

public abstract class Liquid extends Material{

	public Liquid(){
		ID = 0;
		name = "Liquid";
		tick = true;
	}
	
	public void flow(Map map) {
		if(map.lticked(x, y))return;
		else map.ltick(x, y);
		if(map.getID(x,y+1,1)==0 & map.getID(x,y+1,2)==0){
			map.movePixel(x, y, 2, x, y+1, 2);
			y++;
			map.ltick(x, y);
			return;
		}
		if(map.tickrev){
			if(map.getID(x+1,y,1)==0 & map.getID(x+1,y,2)==0){
				int i = 0;
				for(i = 1; i < 100 && map.getID(x+i,y+1,2)!=0; i++){}
				if(map.getID(x+i,y+1,1)==0 & map.getID(x+i,y+1,2)==0){
					map.movePixel(x, y, 2, x+i, y+1, 2);
					return;
				}
			}
		}else{
			if(map.getID(x-1,y,1)==0 & map.getID(x-1,y,2)==0){
				int i = 0;
				for(i =-1; i >-100 && map.getID(x+i,y+1,2)!=0; i--){}
				if(map.getID(x+i,y+1,1)==0 & map.getID(x+i,y+1,2)==0){
					map.movePixel(x, y, 2, x+i, y+1, 2);
					return;
				}
			}
		}
	}
}
