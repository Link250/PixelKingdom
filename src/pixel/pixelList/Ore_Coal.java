package pixel.pixelList;

import pixel.Ore;

public class Ore_Coal extends Ore<Ore.OreAD>{

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
