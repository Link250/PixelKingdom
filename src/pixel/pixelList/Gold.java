package pixel.pixelList;

import pixel.AD;
import pixel.Material;

public class Gold extends Material<AD>{

	public Gold(){
		super(null);
		ID = 34;
		name = "Gold";
		usePickaxe = 2.0;
		tick = false;
	}
}
