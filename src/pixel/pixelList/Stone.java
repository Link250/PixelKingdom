package pixel.pixelList;

import pixel.UDS;
import pixel.Material;

public class Stone extends Material<UDS>{

	public Stone(){
		super(null);
		ID = 1;
		name = "Stone";
		displayName = "Stone";
		usePickaxe = 1.5;
		tick = false;
		loadTexture();
	}
	
}
