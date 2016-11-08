package pixel.pixelList;

import pixel.UDS;
import pixel.interfaces.Burnable;
import pixel.Material;

public class Leaf extends Material<UDS> implements Burnable{

	public Leaf(){
		super(null);
		ID = 5;
		name = "Leaf";
		displayName = "Leaf";
		frontLightReduction = 6;
		backLightReduction = 200;
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 0.5;
		loadTexture();
	}
	
	public byte getBurnStrength() {return 10;}
	
}
