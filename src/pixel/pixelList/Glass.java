package pixel.pixelList;

import pixel.Material;
import pixel.UDS;

public class Glass extends Material<UDS> {

	public Glass() {
		super(null);
		this.backLightReduction = 4;
		this.displayName = "Glass";
		this.frontLightReduction = 4;
		this.ID = 35;
		this.name = "Glass";
		this.usePickaxe = 1.0;
		loadTexture();
	}
}
