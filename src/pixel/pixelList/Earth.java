package pixel.pixelList;

import pixel.UDS;
import pixel.Material;

public class Earth extends Material<UDS>{

	public Earth(){
		super(null);
		ID = 2;
		name = "Earth";
		displayName = "Earth";
		usePickaxe = 1;
		loadTexture();
	}
	
}
