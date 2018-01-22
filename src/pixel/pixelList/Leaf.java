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
		frontLightReduction = new int[] {-6, -2, -6};
		backLightReduction = new int[] {-200, -100, -200};
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 0.5;
		loadTexture();
	}
	
	public byte getBurnStrength() {return 10;}
	
}
