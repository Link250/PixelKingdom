package pixel.pixelList;

import map.Map;
import pixel.UDS;
import pixel.Liquid;
import pixel.PixelList;

public class Lava extends Liquid<Lava.LavaAD>{

	public Lava(){
		super(new LavaAD());
		ID = 2;
		name = "Lava";
		displayName = "Lava";
		viscosity = 10;
		light = new int[] {152, 152, 152};
		loadTexture();
	}
	
	public boolean tick(int x, int y, int l, Map map, int numTick) {
		if((map.getID(x+1, y, l)==1 || map.getID(x-1, y, l)==1 || map.getID(x, y+1, l)==1 || map.getID(x, y-1, l)==1) && map.getID(x, y, Map.LAYER_FRONT)==0){
			map.setID(x, y, l, 0);
			map.setID(x, y, Map.LAYER_FRONT, PixelList.getPixelID("Stone"));
		}
		return Liquid.flow(x, y, l, viscosity, map, numTick);
	}
	
	public void setMat(int x, int y, int l, int ID, Map map){
		uds = map.getUDS(x, y, l);
		uds.ID = (short) ID;
		uds.heat = 60;
	}
	
	public static class LavaAD extends UDS {
		public short ID;
		public byte heat;
	}
}
