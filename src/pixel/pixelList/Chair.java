package pixel.pixelList;

import pixel.MultiPixel;

public class Chair extends MultiPixel<MultiPixel.DataStorage> {

	public Chair() {
		super(new DataStorage(), 6, 8);
		ID = 65;
		itemID = 500;
		name = "Chair";
		displayName = "Chair";
		frontLightReduction = new int[] {-1, -1, -1};
		backLightReduction = new int[] {0, 0, 0};
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}
}
