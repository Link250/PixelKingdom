package pixel.pixelList;

import pixel.UDS;
import pixel.Material;

public class Loam extends Material<UDS>{

	public Loam(){
		super(null);
		ID = 7;
		name = "Loam";
		displayName = "Loam";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1.5;
		loadTexture();
	}
	
}
