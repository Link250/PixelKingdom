package pixel.pixelList;

import map.Map;
import pixel.UDS;
import pixel.Liquid;

public class Water extends Liquid<UDS>{

	public Water(){
		super(null);
		ID = 1;
		name = "Water";
		displayName = "Water";
		viscosity = 1000;
		frontLightReduction = 2;
		backLightReduction = 100;
		loadTexture();
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		if(flow(x, y, l, map)) return true;
		else return false;
	}
}
