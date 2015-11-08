package Pixels;

import Maps.Map;

public class Grass extends Material{

	public Grass(){
		ID = 3;
		name = "Grass";
		usePickaxe = 1;
		tick = true;
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		if(Math.random()*100 < 1){
			for(int X = -1; X <= 1; X++){for(int Y = -1; Y <= 1; Y++){
				if(map.getID(x+X, y+Y, Map.LAYER_FRONT)==2){
					for(int aX = -1; aX <= 1; aX++){for(int aY = -1; aY <= 1; aY++){
						if(map.getID(x+X+aX, y+Y+aY, Map.LAYER_FRONT)==0 && map.getID(x+X+aX, y+Y+aY, Map.LAYER_LIQUID)==0){map.setID(x+X, y+Y, Map.LAYER_FRONT, 3);return true;}
					}}
				}
			}}
			for(int X = -1; X <= 1; X++){for(int Y = -1; Y <= 1; Y++){
				if(map.getID(x+X, y+Y, Map.LAYER_FRONT)==0 && map.getID(x+X, y+Y, Map.LAYER_LIQUID)==0)return true;
			}}
			map.setID(x, y, Map.LAYER_FRONT, 2);
		}
		return false;
	}

}
