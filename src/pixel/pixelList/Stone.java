package pixel.pixelList;

import pixel.UDS;
import pixel.Material;

public class Stone extends Material<UDS>{

	public Stone(){
		super(null);
		ID = 1;
		name = "Stone";
		displayName = "Stone";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1.5;
		loadTexture();
	}
	
}
