package pixel.interfaces;

import map.Map;
import pixel.PixelList;
import pixel.pixelList.Lava;

public interface Smeltable extends Heatable {

	default public int heatUp(int x, int y, int l, int heat, int heatup, Map map) {
		Heatable.DataStorage uds = map.getUDS(x, y, l);
		if(uds.heat>=getMaxHeat()){
			map.setID(x, y, l, 0);
			map.setID(x, y, Map.LAYER_LIQUID, 2);
			((Lava)PixelList.GetPixel(2, Map.LAYER_LIQUID)).setMat(x, y, Map.LAYER_LIQUID, getMoltenID(), map);
		}else{
			if(heat>=uds.heat) {
				uds.heat += heatup;
				map.addUDSUpdate(x, y, l, uds);
				return heatup;
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @return the ID of the Pixel this Material converts to if molten an hardened
	 */
	int getMoltenID();

}
