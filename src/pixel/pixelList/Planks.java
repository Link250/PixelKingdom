package pixel.pixelList;

import pixel.AD;
import pixel.Material;

public class Planks extends Material<AD>{

	public Planks(){
		super(null);
		ID = 6;
		name = "Planks";
		usePickaxe = 1;
		tick = false;
		burnable = 15;
	}
	
}
