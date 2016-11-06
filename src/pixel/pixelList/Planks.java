package pixel.pixelList;

import pixel.UDS;
import pixel.interfaces.Burnable;
import pixel.Material;

public class Planks extends Material<UDS> implements Burnable{

	public Planks(){
		super(null);
		ID = 6;
		name = "Planks";
		displayName = "Planks";
		usePickaxe = 1;
		loadTexture();
	}
	
	public byte getBurnStrength() {return 15;}
	
}
