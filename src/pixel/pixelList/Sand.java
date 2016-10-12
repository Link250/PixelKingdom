package pixel.pixelList;

import map.Map;
import pixel.AD;
import pixel.Material;

public class Sand extends Material<AD>{

	public Sand(){
		super(null);
		ID = 8;
		name = "Sand";
		displayName = "Sand";
		usePickaxe = 1;
		tick = true;
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		if(map.getID(x,y+1,l)==0){
			map.movePixelAbs(x, y, l, x, y+1, l);
			y++;
			return true;
		}
		return Math.random()<0.5 ? falltoside(x, y, l, -1, map) : falltoside(x, y, l, 1, map);
	}
	
	private boolean falltoside(int x, int y, int l, int side, Map map){
		if(map.getID(x+side,y,l)==0 & map.getID(x+side,y+1,l)==0){
			map.movePixelAbs(x, y, l, x+side, y+1, l);
			y++;x--;
			return true;
		}return false;
	}

}
