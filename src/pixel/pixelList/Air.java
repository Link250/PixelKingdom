package pixel.pixelList;

import pixel.UDS;

import map.Map;
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
		frontLightReduction = new int[] {-1, -1, -1};
		backLightReduction = new int[] {0, 0, 0};
		requiredType = MINING_TYPE_NONE;
		solidity = Map.SOLID_NONE;
	}
}
