package pixel.pixelList;

import gfx.Screen;
import map.Map;
import pixel.interfaces.Heatable;
import pixel.interfaces.Smeltable;
import pixel.Material;

public class Sand extends Material<Heatable.DataStorage> implements Smeltable{

	public Sand(){
		super(new DataStorage());
		ID = 8;
		name = "Sand";
		displayName = "Sand";
		usePickaxe = 1;
		loadTexture();
	}
	
	public boolean tick(int x, int y, int l, int numTick, Map map) {
		if(map.getID(x,y+1,l)==0){
			map.movePixelAbs(x, y, l, x, y+1, l);
			y++;
			return true;
		}
		return Math.random()<0.5 ? falltoside(x, y, l, -1, map) : falltoside(x, y, l, 1, map);
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
	
	private boolean falltoside(int x, int y, int l, int side, Map map){
		if(map.getID(x+side,y,l)==0 & map.getID(x+side,y+1,l)==0){
			map.movePixelAbs(x, y, l, x+side, y+1, l);
			y++;x--;
			return true;
		}return false;
	}

	public int getMaxHeat() {
		return 1723;
	}

	public int getMoltenID() {
		return 35;
	}

}
