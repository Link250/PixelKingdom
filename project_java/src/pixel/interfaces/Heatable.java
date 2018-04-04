package pixel.interfaces;

import gfx.Screen;
import map.Map;
import pixel.Liquid;
import pixel.Material;
import pixel.PixelList;
import pixel.UDS;

/**
 * This interface can be used for anything that should should be able to transfer or store heat, for example an Oven and smeltable Ores
 * @author QuantumHero
 *
 */
public interface Heatable{
	
	/**
	 * 
	 * @param heatup the amount of heat that should be added to this Pixel
	 * @return the amout of heat that was actually used
	 */
	default public int heatUp(int x, int y, int l, int heat, int heatup, Map map) {
		DataStorage uds = map.getUDS(x, y, l);
		if(heat>=uds.heat && uds.heat<getMaxHeat()) {
			if(uds.heat+heatup>heat) {heatup = (heat-uds.heat)/2;}
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
	
	default public int render(int x, int y, int l, Map map) {
		int color = PixelList.GetMat(map.getID(x, y, l)).getColor(x, y);
		DataStorage uds = map.getUDS(x, y, l);
		uds = map.getUDS(x, y, l);
		if(uds!=null && uds.heat>0){
			int r = (uds.heat*255/getMeltingPoint());if(r>255)r=255;
			return Screen.combineColors(color, 0x00ff0000 | (r<<24));
		}
		return color;
	}
	
	default boolean tick(int x, int y, int l, Map map, int numTick) {
		DataStorage uds = map.getUDS(x, y, l);
		if(uds!=null) {
			boolean change = exchangeHeat(x, y, l, map, numTick, uds);
			if(uds.heat>this.getMeltingPoint()) {
				change |= Liquid.flow(x, y, l, uds.heat-getMeltingPoint(), map, numTick);
			}
			return change;
		}else {
			return false;
		}
	}

	default boolean exchangeHeat(int x, int y, int l, Map map, int numTick, DataStorage uds) {
		short heat = uds.heat;
		if(numTick%6==0)heat -= exchangeHeatTo(x, y-1, l, heat, map, numTick);
		if(numTick%6==1)heat -= exchangeHeatTo(x-1, y, l, heat, map, numTick);
		if(numTick%6==2)heat -= exchangeHeatTo(x+1, y, l, heat, map, numTick);
		if(numTick%6==3)heat -= exchangeHeatTo(x, y+1, l, heat, map, numTick);
		if(numTick%6==4)heat -= exchangeHeatTo(x, y, Map.LAYER_LIQUID, heat, map, numTick);
		if(numTick%6==5)heat -= exchangeHeatTo(x, y, l == Map.LAYER_BACK ? Map.LAYER_FRONT : Map.LAYER_BACK, heat, map, numTick);
		if(uds.heat!=heat)map.addUDSUpdate(x, y, l, uds);
		uds.heat = heat;
		return uds.heat>0;
	}
	
	default int exchangeHeatTo(int x, int y, int l, int heat, Map map, int numTick) {
		if(map.getID(x, y, Map.LAYER_LIQUID) == 1) {
			return heat>=100 ? 100: heat;
		}else {
			Material<?> m = PixelList.GetPixel(map.getID(x, y, l), l);
			if(m.ID!=0)
				return m instanceof Heatable ? ((Heatable)m).heatUp(x, y, l, heat, heat>=getHeatExchange() ? getHeatExchange() : heat/2, map) : 0;
			else
				return (heat>0) ? getCoolDown() : 0;
		}
	}
	
	default int getMaxHeat() {return Short.MAX_VALUE;}
	
	default int getMeltingPoint() {return Short.MAX_VALUE;}
	
	default int getBoilingPoint() {return Short.MAX_VALUE;}
	
	default int getHeatExchange() {return 1;}
	
	default int getCoolDown() {return 1;}
	
	default int getCDFrequency() {return 10;}
	
	public static class DataStorage extends UDS{
		public short heat;
	}
}
