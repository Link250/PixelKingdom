package pixel.pixelList;

import pixel.UDS;
import pixel.Material;

public class Iron extends Material<UDS>{

	public Iron(){
		super(null);
		ID = 33;
		name = "Iron";
		displayName = "Iron";
		usePickaxe = 2;
		tick = false;
		loadTexture();
	}
}
