package Pixels;

import Maps.Map;

public class Wood extends Material{

	public Wood(){
		ID = 4;
		name = "Wood";
		usePickaxe = 1;
		burnable = 20;
		tick = false;
	}
	
	public void tick(int numTick, Map map) {
		
	}

}
