package pixel.pixelList;

import pixel.Material;
import pixel.UDS;

public class Ore_Coal extends Material<UDS>{

	public Ore_Coal(){
		super(null);
		ID = 16;
		name = "CoalOre";
		displayName = "Coal Ore";
		usePickaxe = 1.9;
		burnable = 50;
		tick = false;
		loadTexture();
	}
}
