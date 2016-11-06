package pixel.pixelList;

import pixel.MultiPixel;
import pixel.UDS;

public class Chair extends MultiPixel<UDS> {

	public Chair() {
		super(new DataStorage(), 10, 18);
		ID = 65;
		name = "Chair";
		displayName = "Chair";
		usePickaxe = 1;
		frontLightReduction = 1;
		backLightReduction = 0;
		loadTexture();
	}

}
