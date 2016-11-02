package pixel.pixelList;

import pixel.Ore;

public class Ore_Iron extends Ore<Ore.OreAD>{

	public Ore_Iron(){
		super(new OreAD());
		melt = 600;
		ID = 17;
		ingot = 33;
		name = "IronOre";
		displayName = "Iron Ore";
		usePickaxe = 2.0;
		tick = false;
		loadTexture();
	}
}
