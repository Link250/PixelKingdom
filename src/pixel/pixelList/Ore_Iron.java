package pixel.pixelList;

import map.Map;
import pixel.Material;
import pixel.interfaces.Heatable;
import pixel.interfaces.Smeltable;

public class Ore_Iron extends Material<Heatable.DataStorage> implements Smeltable{
	
	public Ore_Iron(){
		super(new DataStorage());
		ID = 17;
		name = "IronOre";
		displayName = "Iron Ore";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 2;
		loadTexture();
	}

	public int render(int x, int y, int l, Map map) {
		return Smeltable.super.render(x, y, l, map);
	}
	
	public boolean tick(int x, int y, int l, Map map, int numTick) {
		return Smeltable.super.tick(x, y, l, map, numTick);
	}
	
	public int getMoltenID() {return 33;}
	
	public int getMeltingPoint() {return 1536/*+273*/;}

	public int getBoilingPoint() {return 2861;}
	
	public int getHeatExchange() {return 42;}
	
}
