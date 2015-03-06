package Pixels;

import Maps.Map;

public class Water extends Liquid{

	public Water(){
		ID = 1;
		name = "Water";
	}
	
	public void tick(int numTick, Map map) {
		flow(map);
	}
}
