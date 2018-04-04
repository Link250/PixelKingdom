package pixel;

import map.Map;

public abstract class Liquid<LiquidUDSType extends UDS> extends Material<LiquidUDSType>{

	public int viscosity = 100;
	
	public Liquid(LiquidUDSType uds){
		super(uds);
		name = "Liquid";
	}
	
	public static boolean flow(int x, int y, int l, int viscosity, Map map, int tickCount) {
		if(map.getID(x,y+1,l)==0 && (l!=Map.LAYER_LIQUID || map.getID(x,y+1,Map.LAYER_FRONT)==0)){
			map.movePixelAbs(x, y, l, x, y+1, l);
			y++;
			return true;
		}
		if(tickCount%2==0){
			if(flowtoside(x, y, l, -1, viscosity, map))return true;
			if(flowtoside(x, y, l, 1, viscosity, map))return true;
		}else{
			if(flowtoside(x, y, l, 1, viscosity, map))return true;
			if(flowtoside(x, y, l, -1, viscosity, map))return true;
		}
		return false;
	}
	
	private static boolean flowtoside(int x, int y, int l, int side, int viscosity, Map map){
		if(map.getID(x+side,y,l)==0 && (l!=Map.LAYER_LIQUID || map.getID(x+side,y,Map.LAYER_FRONT)==0)){
			int i = 0;
			for(i = side; Math.abs(i) < Math.abs(viscosity*side) && (map.getID(x+i,y+1,l)==map.getID(x,y,l)); i+=side){}
			if(map.getID(x+i,y+1,l)==0 && (l!=Map.LAYER_LIQUID || map.getID(x+i,y+1,Map.LAYER_FRONT)==0)){
				map.movePixelAbs(x, y, l, x+i, y+1, l);
				return true;
			}
		}return false;
	}
}
