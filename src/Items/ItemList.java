package Items;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import ItemList.*;
import Pixels.Material;
import Pixels.PixelList;

public class ItemList {

	static ArrayList<Item> itemlist = new ArrayList<Item>();
	
	public ItemList(){
		MatStack m = null;
		for(Material mat : PixelList.MatList){
			if(mat.ID!=0){m = new MatStack();m.setMat(mat.ID);itemlist.add(m);}
		}
		itemlist.add(new DevPickaxe());
		itemlist.add(new StonePickaxe());
		itemlist.add(new IronPickaxe());
		itemlist.add(new NormalItemBag());
		itemlist.add(new SmallToolBag());
		itemlist.add(new NormalMaterialBag());
		itemlist.add(new SmallBeltBag());
		itemlist.add(new GiantItemBag());
		itemlist.add(new Lighter());
		for(Item i : itemlist){
			System.out.println("Initialized Item "+i.name+"("+i.ID+")");
		}
	}
	
	public static Item GetItem(int ID){
		for(Item i : itemlist){
			if(i.ID==ID)return i;
		}
		return null;
	}

	public static Item GetItem(String name){
		for(Item i : itemlist){
			if(i.name==name)return i;
		}
		return null;
	}

	public static Item NewItem(int ID){
		for(Item i : itemlist){
			if(i.ID==ID){
				try {
					Item t = (Item) Class.forName(i.getClass().getName()).getConstructor().newInstance();
					if(t.getClass() == MatStack.class) ((MatStack)t).setMat(ID);
					System.out.println("new Instance of "+t.name+"("+t.ID+") created.");
					return (Item) t;
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
