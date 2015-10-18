package Pixels;

import Maps.Map;

public abstract class Liquid extends Material{

	public int viscosity = 100;
	
	public Liquid(){
		ID = 0;
		name = "Liquid";
		tick = true;
	}
	
	public boolean flow(Map map) {
		if(map.getID(x,y+1,1)==0 & map.getID(x,y+1,2)==0){
			map.movePixel(x, y, 2, x, y+1, 2);
			y++;
			return true;
		}
		if(Math.random()<0.5){
			if(flowtoside(-1, map))return true;
			if(flowtoside(1, map))return true;
		}else{
			if(flowtoside(1, map))return true;
			if(flowtoside(-1, map))return true;
		}
		return false;
	}
	
	private boolean flowtoside(int side, Map map){
		if(map.getID(x+side,y,1)==0 & map.getID(x+side,y,2)==0){
			int i = 0;
			if(side<0)for(i = side; i > viscosity*side && map.getID(x+i,y+1,2)!=0; i+=side){}
			if(side>0)for(i = side; i < viscosity*side && map.getID(x+i,y+1,2)!=0; i+=side){}
			if(map.getID(x+i,y+1,1)==0 & map.getID(x+i,y+1,2)==0){
				map.movePixel(x, y, 2, x+i, y+1, 2);
				return true;
			}
		}return false;
	}
}
