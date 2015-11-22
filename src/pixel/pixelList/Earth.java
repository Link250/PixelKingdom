package pixel.pixelList;

import pixel.AD;
import pixel.Material;

public class Earth extends Material<AD>{

	public Earth(){
		super(null);
		ID = 2;
		name = "Earth";
		usePickaxe = 1;
		tick = false;
	}
	
}
