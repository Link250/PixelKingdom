package pixel.pixelList;

import map.Map;
import pixel.AD;
import pixel.Liquid;

public class Water extends Liquid<AD>{

	public Water(){
		super(null);
		ID = 1;
		name = "Water";
		displayName = "Water";
		viscosity = 1000;
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		if(flow(x, y, l, map)) return true;
		else return false;
	}
}
