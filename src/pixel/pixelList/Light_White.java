package pixel.pixelList;

import map.Map;
import pixel.UDS;
import pixel.Material;

public class Light_White extends Material<UDS>{
	
	public Light_White(){
		super(null);
		ID = 49;
		name = "Light";
		displayName = "White Light";
		solidity = Map.SOLID_NONE;
		frontLightReduction = new int[] {-1, -1, -1};
		backLightReduction = new int[] {0, 0, 0};
		light = new int[] {152, 152, 152};
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}
	
	protected void loadTexture() {
		texture = new int[] {0xffffffff};
		this.textureWidth = 1;
		this.textureHeight = 1;
	}
}
