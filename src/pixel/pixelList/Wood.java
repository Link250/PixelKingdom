package pixel.pixelList;

import pixel.UDS;
import pixel.Material;

public class Wood extends Material<UDS>{

	public Wood(){
		super(null);
		ID = 4;
		name = "Wood";
		displayName = "Wood";
		usePickaxe = 1;
		burnable = 20;
		tick = false;
		loadTexture();
	}
	
}
