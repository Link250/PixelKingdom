package Pixels;

import Maps.Map;

public class Water extends Liquid{

	public Water(){
		ID = 1;
		name = "Water";
	}
	
	public boolean tick(int numTick, Map map) {
		if(flow(map)) return true;
		else return false;
	}
}
