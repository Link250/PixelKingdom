package pixel;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import map.Map;
import pixel.pixelList.Air;
import pixel.pixelList.Earth;
import pixel.pixelList.Fire;
import pixel.pixelList.Gold;
import pixel.pixelList.Grass;
import pixel.pixelList.Iron;
import pixel.pixelList.Lava;
import pixel.pixelList.Leaf;
import pixel.pixelList.Loam;
import pixel.pixelList.Ore_Coal;
import pixel.pixelList.Ore_Gold;
import pixel.pixelList.Ore_Iron;
import pixel.pixelList.Oven;
import pixel.pixelList.Planks;
import pixel.pixelList.Sand;
import pixel.pixelList.Stone;
import pixel.pixelList.Torch;
import pixel.pixelList.Water;
import pixel.pixelList.Wood;

public class PixelList {
	
	public static Material<?>[] matList = new Material[256];
	public static Liquid<?>[] liqList = new Liquid[256];
	
	private static HashMap<String, Short> idList = new HashMap<>();
	
	public PixelList(){
		addMaterial(new Air());
		addMaterial(new Stone());
		addMaterial(new Earth());
		addMaterial(new Grass());
		addMaterial(new Wood());
		addMaterial(new Leaf());
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
		
		for (Material<?> material : matList) {
			if(material!=null) {
				idList.put(material.name, (short) material.ID);
			}
		}
		for (Liquid<?> liquid : liqList) {
			if(liquid!=null) {
				idList.put(liquid.name, (short) liquid.ID);
			}
		}
	}
	
	public void addMaterial(Material<?> m){matList[m.ID]=m;}
	public void addLiquid(Liquid<?> l){liqList[l.ID]=l;}

	public static short getPixelID(String pixelName){
		return idList.get(pixelName);
	}
	
	public static Material<?> GetPixel(int ID, int l){
		return GetPixel((short)ID, l);
	}
	
	public static Material<?> GetPixel(short ID, int l){
		return l==Map.LAYER_LIQUID ? GetLiquid(ID) : GetMat(ID);
	}

	public static Material<?> GetMat(int ID){
		return GetMat((short)ID);
	}

	public static Material<?> GetMat(short ID){
		if(ID>0)return matList[ID];
		else return matList[0];
	}

	public static Material<?> GetLiquid(int ID){
		return GetLiquid((short)ID);
	}

	public static Material<?> GetLiquid(short ID){
		if(ID>0)return liqList[ID];
		else return matList[0];
	}

	public static Material<?> GetMat(int x, int y, Map map, int layer){
		int ID = map.getID(x, y, layer);
		Material<?> m;
		if(layer == Map.LAYER_LIQUID){
			m = GetLiquid(ID);
		}else{
			m = GetMat(ID);
		}
		return m;
	}

	public static Material<?> NewMat(int ID){
		try {
			Material<?> t = (Material<?>) Class.forName(matList[ID].getClass().getName()).getConstructor().newInstance();
			return t;
		} catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|ClassNotFoundException e) { e.printStackTrace();}
		return null;
	}
	public static Material<?> NewLiq(int ID){
		try {
			Material<?> t = (Material<?>) Class.forName(liqList[ID].getClass().getName()).getConstructor().newInstance();
			return t;
		} catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|ClassNotFoundException e) { e.printStackTrace();}
		return null;
	}
}
