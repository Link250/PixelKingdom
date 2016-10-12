package pixel.pixelList;

import pixel.AD;
import pixel.Material;

public class Loam extends Material<AD>{

	public Loam(){
		super(null);
		ID = 7;
		name = "Loam";
		displayName = "Loam";
		usePickaxe = 1.5;
		tick = false;
	}
	
}
