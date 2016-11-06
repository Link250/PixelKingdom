package pixel.pixelList;

import map.Map;
import pixel.UDS;
import pixel.Material;

public class Torch extends Material<UDS>{
	
	public Torch(){
		super(null);
		usePickaxe=1;
		ID = 48;
		name = "Torch";
		displayName = "Torch";
		solid = false;
		frontLightReduction = 0;
		backLightReduction = 0;
		loadTexture();
	}

	public short tickLight(int x, int y, int l, Map map) {
		return (short) (Map.MAX_LIGHT*0.6);
	}
}
