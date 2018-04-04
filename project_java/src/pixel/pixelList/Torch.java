package pixel.pixelList;

import map.Map;
import pixel.UDS;
import pixel.Material;

public class Torch extends Material<UDS>{
	
	public Torch(){
		super(null);
		ID = 48;
		name = "Torch";
		displayName = "Torch";
		solidity = Map.SOLID_NONE;
		frontLightReduction = 0;
		backLightReduction = 0;
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}

	public short tickLight(int x, int y, int l, Map map) {
		return (short) (Map.MAX_LIGHT*0.6);
	}
}
