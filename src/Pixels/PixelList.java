package Pixels;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import Maps.Map;

public class PixelList {
		
	public static ArrayList<Material> MatList = new ArrayList<Material>();
	public static ArrayList<Material> LiquidList = new ArrayList<Material>();
	
	public PixelList(){
		MatList.add(new Air());
		MatList.add(new Stone());
		MatList.add(new Earth());
		MatList.add(new Grass());
		MatList.add(new Wood());
		MatList.add(new Leave());
		MatList.add(new Planks());
		MatList.add(new Loam());
		MatList.add(new Sand());
		MatList.add(new Torch());
		MatList.add(new Ore_Coal());
		MatList.add(new Ore_Iron());
		MatList.add(new Oven());
		MatList.add(new Fire());
		MatList.add(new Iron());
		LiquidList.add(new Water());
		LiquidList.add(new Lava());
	}

	public static Material GetMat(int ID){
		return GetMat((short)ID);
	}

	public static Material GetMat(short ID){
		for(Material m : MatList){
			if(m.ID==ID){
				return m;
			}
		}
		return MatList.get(0);
	}

	public static Material GetLiquid(int ID){
		return GetLiquid((byte)ID);
	}

	public static Material GetLiquid(byte ID){
		for(Material m : LiquidList){
			if(m.ID==ID){
				return m;
			}
		}
		return MatList.get(0);
	}

	public static Material GetMat(int x, int y, Map map, int layer){
		int ID = map.getID(x, y, layer);
		Material m;
		if(layer == 2){
			m = GetLiquid(ID);
		}else{
			m = GetMat(ID);
		}
		m.x = x;
		m.y = y;
		m.l = layer;
		return m;
	}

	public static Material NewMat(int ID){
		for(Material m : MatList){
			if(m.ID==ID){
				try {
					Material t = (Material) Class.forName(m.getClass().getName()).getConstructor().newInstance();
//					System.out.println("new Instance of "+t.name+"("+t.ID+") created.");
					return t;
				} catch (InstantiationException e) { e.printStackTrace();
				} catch (IllegalAccessException e) { e.printStackTrace();
				} catch (IllegalArgumentException e) { e.printStackTrace();
				} catch (InvocationTargetException e) { e.printStackTrace();
				} catch (NoSuchMethodException e) { e.printStackTrace();
				} catch (SecurityException e) { e.printStackTrace();
				} catch (ClassNotFoundException e) { e.printStackTrace();};
			}
		}
		return null;
	}
	public static Material NewLiq(int ID){
		for(Material m : LiquidList){
			if(m.ID==ID){
				try {
					Material t = (Material) Class.forName(m.getClass().getName()).getConstructor().newInstance();
//					System.out.println("new Instance of "+t.name+"("+t.ID+") created.");
					return t;
				} catch (InstantiationException e) { e.printStackTrace();
				} catch (IllegalAccessException e) { e.printStackTrace();
				} catch (IllegalArgumentException e) { e.printStackTrace();
				} catch (InvocationTargetException e) { e.printStackTrace();
				} catch (NoSuchMethodException e) { e.printStackTrace();
				} catch (SecurityException e) { e.printStackTrace();
				} catch (ClassNotFoundException e) { e.printStackTrace();};
			}
		}
		return null;
	}
}
