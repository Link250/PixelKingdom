package pixel.pixelList;

import pixel.UDS;
import pixel.interfaces.Burnable;
import pixel.Material;

public class Wood extends Material<UDS> implements Burnable{

	public Wood(){
		super(null);
		ID = 4;
		name = "Wood";
		displayName = "Wood";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}

	public byte getBurnStrength() {return 20;}
	
}
