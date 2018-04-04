package pixel.pixelList;

import map.Map;
import pixel.Material;
import pixel.interfaces.Heatable;

public class Glass extends Material<Heatable.DataStorage> implements Heatable{

	public Glass() {
		super(new DataStorage());
		this.backLightReduction = 4;
		this.displayName = "Glass";
		this.frontLightReduction = 4;
		this.ID = 35;
		this.name = "Glass";
		this.requiredType = MINING_TYPE_PICKAXE;
		this.requiredTier = 1;
		this.miningResistance = 1;
		loadTexture();
	}

	public int render(int x, int y, int l, Map map) {
		return Heatable.super.render(x, y, l, map);
	}
	
	public boolean tick(int x, int y, int l, Map map, int numTick) {
		return Heatable.super.tick(x, y, l, map, numTick);
	}
	
	public int getMeltingPoint() {return 1723/*+273*/;}

	public int getBoilingPoint() {return 2230;}
	
	public int getHeatExchange() {return 33;}
	
}
