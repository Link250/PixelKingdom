package pixel.pixelList;

import map.Map;
import pixel.UDS;
import pixel.interfaces.Burnable;
import pixel.interfaces.Heatable;
import pixel.Material;
import pixel.PixelList;

public class Fire extends Material<Fire.FireUDS>{

	public Fire(){
		super(new FireUDS());
		ID = 32;
		name = "Fire";
		displayName = "Fire";
		solidity = Map.SOLID_NONE;
		frontLightReduction = 0;
		backLightReduction = 0;
	}
	
	public boolean tick(int x, int y, int l, Map map, int numTick) {
		Material<?> tempPixel;
		for (int X = -1; X <= 1; X++) {
			for (int Y = -1; Y <= 1; Y++) {
				if(map.getID(x+X, y+Y, Map.LAYER_LIQUID)==1) {map.setID(x, y, l, 0);return true;}
				if((tempPixel = PixelList.GetMat(map.getID(x+X, y+Y, Map.LAYER_BACK))) instanceof Heatable)
					{((Heatable)tempPixel).heatUp(x+X, y+Y, Map.LAYER_BACK, 1700, 2, map);}
				if((tempPixel = PixelList.GetMat(map.getID(x+X, y+Y, Map.LAYER_FRONT))) instanceof Heatable)
					{((Heatable)tempPixel).heatUp(x+X, y+Y, Map.LAYER_FRONT, 1700, 2, map);}
			}
		}
		uds = map.getUDS(x, y, l);
		if(Math.random()*500 <= 1){
			spread(x, y, l, map);
		}
		if(numTick%60==30){
			if(uds.burntime>0){
				uds.burntime = (byte) (uds.burntime-1);
				map.addUDSUpdate(x, y, l, uds);
			}else{
				map.setID(x, y, l, 0);return true;
			}
		}
		return true;
	}
	
	public short tickLight(int x, int y, int l, Map map) {
		return (byte) (map.<FireUDS>getUDS(x, y, l).burntime*Map.MAX_LIGHT/100);
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
					if(PixelList.GetMat(Xt, Yt, map, L) instanceof Burnable) {
						burntime = ((Burnable)PixelList.GetMat(Xt, Yt, map, L)).getBurnStrength();
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
	}
	
	public void setTime(int x, int y, int l, byte time, Map map){
		uds = map.getUDS(x, y, l);
		uds.burntime=time;
	}
	
	public static class FireUDS extends UDS{
		public byte burntime;
	}
}
