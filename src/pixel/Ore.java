package pixel;

import gfx.Screen;
import map.Map;
import pixel.pixelList.Lava;

public abstract class Ore<OreADType extends Ore.OreAD> extends Material<OreADType>{

	public int melt;
	public int ingot;
	
	public Ore(OreADType ad){
		super(ad);
		name = "Ore";
		usePickaxe = 1;
		tick = false;
	}
	
	public void heatUp(int x, int y, int l, short heat, short heatup, Map map){
		this.uds = map.getUDS(x, y, l);
		if(uds.heat>=melt){
			map.setID(x, y, l, 0);
			map.setID(x, y, Map.LAYER_LIQUID, 2);
			((Lava)PixelList.GetPixel(2, Map.LAYER_LIQUID)).setMat(x, y, Map.LAYER_LIQUID, ingot, map);
		}else{
			if(heat>=uds.heat) {
				uds.heat += heatup;
				map.addUDSUpdate(x, y, l, uds);
			}
		}
	}
	
	public int render(int x, int y, int l, Map map) {
		int color = super.render(x, y, l, map);
		uds = map.getUDS(x, y, l);
		if(melt>0 && uds!=null && uds.heat>0){
			int r = (uds.heat*255/melt);if(r>255)r=255;
			return Screen.combineColors(color, 0x00ff0000 | (r<<24));
		}
		return color;
	}

	public static class OreAD extends UDS {
		public short heat;
	}
}
