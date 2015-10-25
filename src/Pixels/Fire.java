package Pixels;

import gfx.Screen;
import Maps.Map;

public class Fire extends Material{

	AdditionalData ad;
	
	public Fire(){
		ID = 32;
		name = "Fire";
		tick = true;
		solid = false;
		adl = 1;
	}
	
	public boolean tick(int numTick, Map map) {
		for (int X = -1; X <= 1; X++) {
			for (int Y = -1; Y <= 1; Y++) {
				if(map.getID(x+X, y+Y, 2)==1) {map.setID(x, y, l, 0);return true;}
			}
		}
		ad = map.getAD(x, y, l);
		byte n = ad.getbyte(0);
		map.setlighter(x, y, (byte) (n/2+10));
		if(Math.random()*500<1){
			spread(map);
		}
		if(numTick%60==30){
			if(n>0){
				ad.setbyte(0, (byte) (n-1));
			}else{
				map.setID(x, y, l, 0);return true;
			}
		}
		return true;
	}
	
	public void render(Map map, Screen screen, int layer) {
		screen.drawPixelScaled(x, y, 0xffff0000 | (((byte)(map.getAD(x, y, l+1).getbyte(0)*2+Math.random()*55))<<8));
	}
	
	public void spread(Map map){
		byte burntime;
		int Xt,Yt;
		byte Xd,Yd;
		if(Math.random()<0.5)Xd=1;else Xd=-1;
		if(Math.random()<0.5)Yd=1;else Yd=-1;
		for(int Y=-2; Y<=2; Y++){
			for(int X=-2; X<=2; X++){
				for(int L=1; L<=3; L++){
					Xt = x+X*Xd;Yt = y+Y*Yd;
					burntime = PixelList.GetMat(Xt, Yt, map, L).burnable;
					if((int)(Math.random()*100+1)<=burntime){
						map.setID(Xt, Yt, L, 32);
						((Fire)PixelList.GetMat(Xt, Yt, map, L)).setTime(burntime,map);
					}
				}
			}
		}
	}
	
	public void setTime(byte time, Map map){
		ad = map.getAD(x, y, l);
		ad.setbyte(0, time);
	}
}
