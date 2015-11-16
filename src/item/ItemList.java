package item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import item.itemList.*;
import main.Game;
import main.Loader;
import pixel.PixelList;

public class ItemList {

	static ArrayList<Item> itemlist = new ArrayList<Item>();
	
	public ItemList(){
		Loader list = new Loader("item/itemList");
		list.loadPackage();list.createClasses();
		for(Class<?> item : list.classes){
			if(item.toString().endsWith("MatStack")){
				MatStack m = null;
				for(int i = 1; i < PixelList.matList.length; i++){
					if(PixelList.matList[i]!=null){m = new MatStack();m.setMat(i);itemlist.add(m);}
				}
			}else{
				try {
					itemlist.add((Item) item.getConstructor().newInstance());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e){
					e.printStackTrace();
				}
			}
		}
		for(Item i : itemlist){
			Game.logInfo("Initialized Item "+i.name+"("+i.ID+")");
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
					if(t instanceof MatStack) ((MatStack)t).setMat(ID);
					Game.logInfo("new Instance of "+t.name+"("+t.ID+") created.");
					return (Item) t;
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
