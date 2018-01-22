package pixel;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import dataUtils.Loader;
import map.Map;

public class PixelList {
	
	public static Material<?>[] matList = new Material[256];
	public static Liquid<?>[] liqList = new Liquid[256];
	
	private static HashMap<String, Short> idList = new HashMap<>();
	
	public PixelList(){
		Loader list = new Loader("pixel/pixelList");
		list.loadPackage();list.createClasses();
		for(Class<?> item : list.classes){
			try {
				Object mat = item.getConstructor().newInstance();
				if(mat instanceof Material) {
					if(mat instanceof Liquid) {
						liqList[((Liquid<?>) mat).ID] = (Liquid<?>) mat;
					}else {
						matList[((Material<?>) mat).ID] = (Material<?>) mat;
					}
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e){
				System.out.println(item.getName());
				e.printStackTrace();
			}
		}
		
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
