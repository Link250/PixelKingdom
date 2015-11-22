package pixel.pixelList;

import gfx.Screen;
import map.Map;
import pixel.Material;
import pixel.PixelList;
import pixel.ads.FireAD;

public class Fire extends Material<FireAD>{

	public Fire(){
		super(new FireAD());
		ID = 32;
		name = "Fire";
		tick = true;
		solid = false;
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		for (int X = -1; X <= 1; X++) {
			for (int Y = -1; Y <= 1; Y++) {
				if(map.getID(x+X, y+Y, Map.LAYER_LIQUID)==1) {map.setID(x, y, l, 0);return true;}
			}
		}
		ad = map.getAD(x, y, l);
		byte n = ad.burntime;
		map.setlighter(x, y, (byte) (n/2+10));
		if(Math.random()*500<1){
			spread(x, y, l, map);
		}
		if(numTick%60==30){
			if(n>0){
				ad.burntime = (byte) (n-1);
			}else{
				map.setID(x, y, l, 0);return true;
			}
		}
		return true;
	}
	
	public void render(int x, int y, int l, Map map, Screen screen) {
		screen.drawPixelScaled(x, y, 0xffff0000 | (((byte)(map.<FireAD>getAD(x, y, l).burntime*2+Math.random()*55))<<8));
	}
	
	public void spread(int x, int y, int l, Map map){
		byte burntime;
		int Xt,Yt;
		byte Xd,Yd;
		if(Math.random()<0.5)Xd=1;else Xd=-1;
		if(Math.random()<0.5)Yd=1;else Yd=-1;
		for(int Y=-2; Y<=2; Y++){
			for(int X=-2; X<=2; X++){
				for(int L : Map.LAYER_ALL_PIXEL){
					Xt = x+X*Xd;Yt = y+Y*Yd;
					burntime = PixelList.GetMat(Xt, Yt, map, L).burnable;
					if((int)(Math.random()*100+1)<=burntime){
						map.setID(Xt, Yt, L, 32);
						((Fire)PixelList.GetMat(Xt, Yt, map, L)).setTime(Xt, Yt, L, burntime,map);
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
