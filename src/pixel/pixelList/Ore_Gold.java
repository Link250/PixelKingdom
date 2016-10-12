package pixel.pixelList;

import pixel.Ore;
import pixel.ads.OreAD;

public class Ore_Gold extends Ore<OreAD>{

	public Ore_Gold(){
		super(new OreAD());
		melt = 400;
		ID = 18;
		ingot = 34;
		name = "GoldOre";
		displayName = "Gold Ore";
		usePickaxe = 2.0;
		tick = false;
	}
}
