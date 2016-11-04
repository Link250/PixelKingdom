package pixel.pixelList;

import pixel.UDS;
import pixel.Material;

public class Planks extends Material<UDS>{

	public Planks(){
		super(null);
		ID = 6;
		name = "Planks";
		displayName = "Planks";
		usePickaxe = 1;
		tick = false;
		burnable = 15;
		loadTexture();
	}
	
}
