package item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import dataUtils.Loader;
import item.itemList.*;
import main.Game;
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
	
	public static Item GetItem(int ID)/* throws UnknownItemException*/{
		for(Item i : itemlist){
			if(i.ID==ID)return i;
		}
		return null;
//		throw new UnknownItemException(Integer.toString(ID));
	}

	public static Item GetItem(String name)/* throws UnknownItemException*/{
		for(Item i : itemlist){
			if(i.name==name)return i;
		}
		return null;
//		throw new UnknownItemException(name);
	}

	public static Item NewItem(Item i, int startStack){
		if(i!=null) {
			try {
				Item t = (Item) Class.forName(i.getClass().getName()).getConstructor().newInstance();
				if(t instanceof MatStack) ((MatStack)t).setMat(i.ID);
//				Game.logInfo("new Instance of "+t.name+"("+t.ID+") created.");
				t.setStack(startStack);
				return (Item) t;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Item NewItem(String itemName){
		return NewItem(GetItem(itemName), 1);
	}
	
	public static Item NewItem(String itemName, int startStack){
		return NewItem(GetItem(itemName), startStack);
	}
	
	public static Item NewItem(int ID){
		return NewItem(GetItem(ID), 1);
	}
	
	public static Item NewItem(int ID, int startStack){
		return NewItem(GetItem(ID), startStack);
	}
	
	public static class UnknownItemException extends Exception{
		private static final long serialVersionUID = -1455881875691855085L;
		
		public UnknownItemException(String itemName){
			super("Item " + itemName + " not found.");
		}
	}
}
