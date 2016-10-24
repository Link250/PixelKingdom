package pixel.pixelList;

import map.Map;
import pixel.AD;
import pixel.Material;

public class Torch extends Material<AD>{
	
	public Torch(){
		super(null);
		usePickaxe=1;
		ID = 48;
		name = "Torch";
		displayName = "Torch";
		tick = true;
		solid = false;
		loadTexture();
	}

	public byte tickLight(int x, int y, int l, Map map) {
		return Map.MAX_LIGHT;
	}
}
