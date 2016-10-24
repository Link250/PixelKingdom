package pixel.pixelList;

import pixel.AD;
import pixel.Material;

public class Iron extends Material<AD>{

	public Iron(){
		super(null);
		ID = 33;
		name = "Iron";
		displayName = "Iron";
		usePickaxe = 2;
		tick = false;
		loadTexture();
	}
}
