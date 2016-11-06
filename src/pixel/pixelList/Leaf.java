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
		usePickaxe = 1;
		frontLightReduction = 6;
		backLightReduction = 200;
		loadTexture();
	}
	
	public byte getBurnStrength() {return 10;}
	
}
