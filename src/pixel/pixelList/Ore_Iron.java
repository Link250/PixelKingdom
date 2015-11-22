package pixel.pixelList;

import pixel.Ore;
import pixel.ads.OreAD;

public class Ore_Iron extends Ore<OreAD>{

	public Ore_Iron(){
		super(new OreAD());
		adl = 2;
		melt = 600;
		ID = 17;
		ingot = 33;
		name = "IronOre";
		usePickaxe = 2.0;
		tick = false;
	}
}
