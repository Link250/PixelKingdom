package pixel.pixelList;

import map.Map;
import pixel.Material;
import pixel.PixelList;
import pixel.ads.FireAD;

public class Fire extends Material<FireAD>{

	public Fire(){
		super(new FireAD());
		ID = 32;
		name = "Fire";
		displayName = "Fire";
		tick = true;
		solid = false;
		frontLightReduction = 0;
		backLightReduction = 0;
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		for (int X = -1; X <= 1; X++) {
			for (int Y = -1; Y <= 1; Y++) {
				if(map.getID(x+X, y+Y, Map.LAYER_LIQUID)==1) {map.setID(x, y, l, 0);return true;}
			}
		}
		ad = map.getAD(x, y, l);
		if(Math.random()*500 <= 1){
			spread(x, y, l, map);
		}
		if(numTick%60==30){
			if(ad.burntime>0){
				ad.burntime = (byte) (ad.burntime-1);
				map.addADUpdate(x, y, l, ad);
			}else{
				map.setID(x, y, l, 0);return true;
			}
		}
		return true;
	}
	
	public short tickLight(int x, int y, int l, Map map) {
		return (byte) (map.<FireAD>getAD(x, y, l).burntime*Map.MAX_LIGHT/100);
	}
	
	public int render(int x, int y, int l, Map map) {
		try {
			return 0xffff0000;
//			return( 0xffff0000 | (((byte)(map.<FireAD>getAD(x, y, l).burntime*2+Math.random()*55))<<8));
		}catch(NullPointerException e) {
			System.out.println("Fire.render()");
			//can happen if the AD is changed while this pixel is being rendered
		}
		return 0xffff0000;
	}
	
	public void spread(int x, int y, int l, Map map){
		byte burntime;
		int Xt,Yt;
		byte Xd = (byte) (Math.random() < 0.5 ? 1 : -1),
			 Yd = (byte) (Math.random() < 0.5 ? 1 : -1);
		for(int Y=-2; Y<=2; Y++){
			for(int X=-2; X<=2; X++){
				for(int L : Map.LAYER_ALL_PIXEL){
					Xt = x+X*Xd;Yt = y+Y*Yd;
					burntime = PixelList.GetMat(Xt, Yt, map, L).burnable;
					if((int)(Math.random()*100+1)<=burntime){
						map.setID(Xt, Yt, L, 32);
						try {
						((Fire)PixelList.GetMat(Xt, Yt, map, L)).setTime(Xt, Yt, L, burntime,map);
						}catch(NullPointerException e) {}
					}
				}
			}
		}
	}
	
	public void setTime(int x, int y, int l, byte time, Map map){
		ad = map.getAD(x, y, l);
		ad.burntime=time;
	}
}
