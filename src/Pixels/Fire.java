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
		map.setlighter(x, y, (byte) (Math.random()*32+16));
		if(Math.random()*500<1){
			spread(map);
		}
		if(map.getID(x, y, 2)==1){map.setID(x, y, l, 0);return true;}
		if(numTick%60==30){
			ad = map.getAD(x, y, l);
			byte n = ad.getbyte(0);
			if(n>0){
				ad.setbyte(0, (byte) (n-1));
			}else{
				map.setID(x, y, l, 0);return true;
			}
		}
		return true;
	}
	
	public void render(Map map, Screen screen, int layer) {
		screen.renderPixelScaled(x, y, 0xffff0000 | (((int)(Math.random()*256))<<8));
	}
	
	public void spread(Map map){
		byte burntime;
		int Xt,Yt;
		for(int Y=-2; Y<=2; Y++){
			for(int X=-2; X<=2; X++){
				for(int L=1; L<=3; L++){
					Xt = x+X;Yt = y+Y;
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
