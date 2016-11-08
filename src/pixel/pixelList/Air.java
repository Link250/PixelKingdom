package pixel.pixelList;

import pixel.UDS;
import pixel.Material;

public class Air extends Material<UDS>{

	public Air(){
		super(null);
		ID = 0;
		name = "Air";
		displayName = "Air";
		texture = new int[1];
		textureWidth = 1;
		textureHeight = 1;
		frontLightReduction = 1;
		backLightReduction = 0;
		requiredType = MINING_TYPE_NONE;
	}
	
}
