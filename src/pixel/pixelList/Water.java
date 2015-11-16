package pixel.pixelList;

import map.Map;
import pixel.Liquid;

public class Water extends Liquid{

	public Water(){
		ID = 1;
		name = "Water";
		viscosity = 1000;
	}
	
	@Override
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		if(flow(x, y, l, map)) return true;
		else return false;
	}
}
