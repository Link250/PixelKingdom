package pixel.interfaces;

import map.Map;
import pixel.UDS;

/**
 * This interface can be used for anything that should should be able to transfer or store heat, for example an Oven and smeltable Ores
 * @author QuantumHero
 *
 */
public interface Heatable{
	
	/**
	 * 
	 * @param heat the amount of heat that should be added to this Pixel
	 * @return the amout of heat that was actually used
	 */
	default public int heatUp(int x, int y, int l, int heat, int heatup, Map map) {
		DataStorage uds = map.getUDS(x, y, l);
		if(heat>=uds.heat && uds.heat<getMaxHeat()) {
			if(uds.heat+heatup>getMaxHeat()) {heatup = getMaxHeat()-uds.heat;}
			uds.heat += heatup;
			map.addUDSUpdate(x, y, l, uds);
			return heatup;
		}
		return 0;
	}
	
	/**
	 * 
	 * @return the heat of this Pixel as an Integer
	 */
	default int getHeat(int x, int y, int l, Map map) {
		return map.<DataStorage>getUDS(x, y, l).heat;
	}
	
	int getMaxHeat();
	
	public static class DataStorage extends UDS{
		public short heat;
	}
}
