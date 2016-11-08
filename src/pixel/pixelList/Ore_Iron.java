package pixel.pixelList;

import gfx.Screen;
import map.Map;
import pixel.Material;
import pixel.interfaces.Heatable;
import pixel.interfaces.Smeltable;

public class Ore_Iron extends Material<Heatable.DataStorage> implements Smeltable{
	
	public Ore_Iron(){
		super(new DataStorage());
		ID = 17;
		name = "IronOre";
		displayName = "Iron Ore";
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 2;
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
	
	public int getMaxHeat() {return 1538/*+273*/;}
	public int getMoltenID() {return 33;}

}
