package Pixels;

import java.lang.reflect.InvocationTargetException;
import Maps.Map;

public class PixelList {
		
	public static Material[] matList = new Material[256];
	public static Liquid[] liqList = new Liquid[256];
	
	public PixelList(){
		addMaterial(new Air());
		addMaterial(new Stone());
		addMaterial(new Earth());
		addMaterial(new Grass());
		addMaterial(new Wood());
		addMaterial(new Leave());
		addMaterial(new Planks());
		addMaterial(new Loam());
		addMaterial(new Sand());
		addMaterial(new Torch());
		addMaterial(new Ore_Coal());
		addMaterial(new Ore_Iron());
		addMaterial(new Ore_Gold());
		addMaterial(new Oven());
		addMaterial(new Fire());
		addMaterial(new Iron());
		addMaterial(new Gold());
		addLiquid(new Water());
		addLiquid(new Lava());
	}
	
	public void addMaterial(Material m){matList[m.ID]=m;}
	public void addLiquid(Liquid l){liqList[l.ID]=l;}

	public static Material GetMat(int ID){
		return GetMat((short)ID);
	}

	public static Material GetMat(short ID){
		if(ID>0)return matList[ID];
		else return matList[0];
	}

	public static Material GetLiquid(int ID){
		return GetLiquid((byte)ID);
	}

	public static Material GetLiquid(byte ID){
		if(ID>0)return liqList[ID];
		else return matList[0];
	}

	public static Material GetMat(int x, int y, Map map, int layer){
		int ID = map.getID(x, y, layer);
		Material m;
		if(layer == Map.LAYER_LIQUID){
			m = GetLiquid(ID);
		}else{
			m = GetMat(ID);
		}
		return m;
	}

	public static Material NewMat(int ID){
		try {
			Material t = (Material) Class.forName(matList[ID].getClass().getName()).getConstructor().newInstance();
			return t;
		} catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|ClassNotFoundException e) { e.printStackTrace();}
		return null;
	}
	public static Material NewLiq(int ID){
		try {
			Material t = (Material) Class.forName(liqList[ID].getClass().getName()).getConstructor().newInstance();
			return t;
		} catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|ClassNotFoundException e) { e.printStackTrace();}
		return null;
	}
}
