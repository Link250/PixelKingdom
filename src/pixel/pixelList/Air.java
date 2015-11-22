package pixel.pixelList;

import pixel.AD;
import pixel.Material;

public class Air extends Material<AD>{

	public Air(){
		super(null);
		ID = 0;
		name = "Air";
		usePickaxe = 0;
		tick = false;
	}
	
}
