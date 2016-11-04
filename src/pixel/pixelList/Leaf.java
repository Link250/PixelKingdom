package pixel.pixelList;

import pixel.UDS;
import pixel.Material;

public class Leaf extends Material<UDS>{

	public Leaf(){
		super(null);
		ID = 5;
		name = "Leaf";
		displayName = "Leaf";
		usePickaxe = 1;
		burnable = 10;
		tick = false;
		frontLightReduction = 6;
		backLightReduction = 200;
		loadTexture();
	}
}
