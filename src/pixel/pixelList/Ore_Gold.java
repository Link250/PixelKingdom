package pixel.pixelList;

import gfx.Screen;
import map.Map;
import pixel.Material;
import pixel.interfaces.Heatable;
import pixel.interfaces.Smeltable;

public class Ore_Gold extends Material<Heatable.DataStorage> implements Smeltable{

	public Ore_Gold(){
		super(new DataStorage());
		ID = 18;
		name = "GoldOre";
		displayName = "Gold Ore";
		usePickaxe = 2.0;
		tick = false;
		loadTexture();
	}

	public int render(int x, int y, int l, Map map) {
		int color = super.render(x, y, l, map);
		uds = map.getUDS(x, y, l);
		if(uds!=null && uds.heat>0){
			int r = (uds.heat*255/getMaxHeat());if(r>255)r=255;
			return Screen.combineColors(color, 0x00ff0000 | (r<<24));
		}
		return color;
	}
	
	public int getMaxHeat() {return 1064/*+273*/;}
	public int getMoltenID() {return 34;}

}
