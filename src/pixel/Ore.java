package pixel;

import map.Map;
import pixel.ads.OreAD;
import pixel.pixelList.Lava;

public abstract class Ore<OreADType extends OreAD> extends Material<OreADType>{

	public int melt;
	public int ingot;
	
	public Ore(OreADType ad){
		super(ad);
		name = "Ore";
		usePickaxe = 1;
		tick = false;
	}
	
	public void heatUp(int x, int y, int l, short heat, short heatup, Map map){
		this.ad = map.getAD(x, y, l);
		if(ad.heat>=melt){
			map.setID(x, y, l, 0);
			map.setID(x, y, Map.LAYER_LIQUID, 2);
			((Lava)PixelList.GetPixel(2, Map.LAYER_LIQUID)).setMat(x, y, Map.LAYER_LIQUID, ingot, map);
		}else{
			if(heat>=ad.heat) {
				ad.heat += heatup;
				map.addADUpdate(x, y, l, ad);
			}
		}
	}
	
	public int render(int x, int y, int l, Map map) {
		int color = super.render(x, y, l, map);
		ad = map.getAD(x, y, l);
		if(melt>0 && ad!=null && ad.heat>0){
			int r = (ad.heat*8/melt);if(r>7)r=7;
//			color = 0xff000000 | 1;
		}
		return color;
	}
}
