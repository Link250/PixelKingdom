package pixel.pixelList;

import pixel.Material;
import pixel.UDS;
import pixel.interfaces.Burnable;

public class Ore_Coal extends Material<UDS> implements Burnable{

	public Ore_Coal(){
		super(null);
		ID = 16;
		name = "CoalOre";
		displayName = "Coal Ore";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1.8;
		loadTexture();
	}
	
	public byte getBurnStrength() {return 50;}
	
}
