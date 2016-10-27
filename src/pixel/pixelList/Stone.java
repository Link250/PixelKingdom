package pixel.pixelList;

import pixel.AD;
import pixel.Material;

public class Stone extends Material<AD>{

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
