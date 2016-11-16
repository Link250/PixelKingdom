package pixel.interfaces;

import map.Map;

public interface Smeltable extends Heatable {

	default public int heatUp(int x, int y, int l, int heat, int heatup, Map map) {
		int heatupDone = Heatable.super.heatUp(x, y, l, heat, heatup, map);
		if(map.<DataStorage>getUDS(x, y, l).heat>getMeltingPoint()) {
			map.setID(x, y, l, getMoltenID(), map.getUDS(x, y, l), false);
		}
		return heatupDone;
	}
	
	/**
	 * 
	 * @return the ID of the Pixel this Material converts to if molten an hardened
	 */
	int getMoltenID();

}
