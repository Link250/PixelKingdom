package pixel;

import map.Map;

public abstract class Liquid<LiquidUDSType extends UDS> extends Material<LiquidUDSType>{

	public int viscosity = 100;
	
	public Liquid(LiquidUDSType uds){
		super(uds);
		name = "Liquid";
	}
	
	public boolean flow(int x, int y, int l, Map map) {
		if(map.getID(x,y+1,Map.LAYER_FRONT)==0 && map.getID(x,y+1,Map.LAYER_LIQUID)==0){
			map.movePixelAbs(x, y, Map.LAYER_LIQUID, x, y+1, Map.LAYER_LIQUID);
			y++;
			return true;
		}
		if(Math.random()<0.5){
			if(flowtoside(x, y, l, -1, map))return true;
			if(flowtoside(x, y, l, 1, map))return true;
		}else{
			if(flowtoside(x, y, l, 1, map))return true;
			if(flowtoside(x, y, l, -1, map))return true;
		}
		return false;
	}
	
	private boolean flowtoside(int x, int y, int l, int side, Map map){
		if(map.getID(x+side,y,Map.LAYER_FRONT)==0 && map.getID(x+side,y,Map.LAYER_LIQUID)==0){
			int i = 0;
			if(side<0)for(i = side; i > viscosity*side && map.getID(x+i,y+1,Map.LAYER_LIQUID)!=0; i+=side){}
			if(side>0)for(i = side; i < viscosity*side && map.getID(x+i,y+1,Map.LAYER_LIQUID)!=0; i+=side){}
			if(map.getID(x+i,y+1,Map.LAYER_FRONT)==0 && map.getID(x+i,y+1,Map.LAYER_LIQUID)==0){
				map.movePixelAbs(x, y, Map.LAYER_LIQUID, x+i, y+1, Map.LAYER_LIQUID);
				return true;
			}
		}return false;
	}
}
