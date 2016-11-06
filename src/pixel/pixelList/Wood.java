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
		usePickaxe = 1;
		loadTexture();
	}

	public byte getBurnStrength() {return 20;}
	
}
