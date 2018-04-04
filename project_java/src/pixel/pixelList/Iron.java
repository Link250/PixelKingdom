package pixel.pixelList;

import pixel.interfaces.Heatable;
import map.Map;
import pixel.Material;

public class Iron extends Material<Heatable.DataStorage> implements Heatable{

	public Iron(){
		super(new DataStorage());
		ID = 33;
		name = "Iron";
		displayName = "Iron";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 2;
		loadTexture();
	}

	public int render(int x, int y, int l, Map map) {
		return Heatable.super.render(x, y, l, map);
	}
	
	public boolean tick(int x, int y, int l, Map map, int numTick) {
		return Heatable.super.tick(x, y, l, map, numTick);
	}
	
	public int getMeltingPoint() {return 1536/*+273*/;}

	public int getBoilingPoint() {return 2861;}
	
	public int getHeatExchange() {return 42;}
	
}
