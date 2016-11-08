package pixel.pixelList;

import pixel.UDS;
import pixel.Material;

public class Gold extends Material<UDS>{

	public Gold(){
		super(null);
		ID = 34;
		name = "Gold";
		displayName = "Gold";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 2;
		loadTexture();
	}
}
