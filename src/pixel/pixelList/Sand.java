package pixel.pixelList;

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
		requiredType = MINING_TYPE_PICKAXE;
		requiredTier = 1;
		miningResistance = 1;
		loadTexture();
	}
	
	public boolean tick(int x, int y, int l, Map map, int numTick) {
		if(map.getID(x,y+1,l)==0){
			map.movePixelAbs(x, y, l, x, y+1, l);
			y++;
			return true;
		}
		return Smeltable.super.tick(x, y, l, map, numTick) | (numTick%2==0 ? falltoside(x, y, l, -1, map) : falltoside(x, y, l, 1, map));
	}
	
	public int render(int x, int y, int l, Map map) {
		return Smeltable.super.render(x, y, l, map);
	}
	
	private boolean falltoside(int x, int y, int l, int side, Map map){
		if(map.getID(x+side,y,l)==0 & map.getID(x+side,y+1,l)==0){
			map.movePixelAbs(x, y, l, x+side, y+1, l);
			y++;x--;
			return true;
		}return false;
	}

	public int getMoltenID() {return 35;}

	public int getMeltingPoint() {return 1723/*+273*/;}

	public int getBoilingPoint() {return 2230;}
	
	public int getHeatExchange() {return 33;}
	
}
