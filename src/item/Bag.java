package item;

import java.util.ArrayList;

import entities.Player;
import gfx.Mouse;
import gfx.Screen;
import main.InputHandler;
import main.conversion.ConvertData;
import map.Map;

public abstract class Bag<ItemType extends Item> extends Item {
	
	private Class<ItemType> clazz;
	private ArrayList<ItemType> inventory;
	private int invSize;
	private int invWidth;
	protected byte itemPriority;
	
	public Bag(Class<ItemType> clazz, int invSize){
		this.clazz = clazz;
		this.invSize = invSize;
		this.invWidth = 6;
		this.init();
	}
	
	public Bag(Class<ItemType> clazz, int invSize, int invWidth){
		this.clazz = clazz;
		this.invSize = invSize;
		this.invWidth = invWidth;
		this.init();
	}
	
	private void init() {
		this.stack=1;
		this.stackMax=1;
		this.anim = 0;
		this.itemPriority = 0;
		
		//setting the priority relative to the distance from ItemType.class to Item.class
		Class<?> supClazz = clazz;
		while(!supClazz.equals(Item.class)){
			this.itemPriority++;
			supClazz = supClazz.getSuperclass();
		}
		
		initInventory();
	}
	
	private void initInventory() {
		this.inventory = new ArrayList<ItemType>(this.invSize);
		for (int i = 0; i < this.invSize; i++) {this.inventory.add(null);}
	}
	
	public abstract void useItem(InputHandler input, Player plr, Map map, Screen screen);
	
	public void setMouse() {
		Mouse.mousetype=0;
	}
	
	/**
	 * 
	 * @param item
	 * @return true if item has been inserted successfully
	 */
	@SuppressWarnings("unchecked")
	public boolean setItem(int i, Item item) {
		if(canContain(item)) {
			this.inventory.set(i, (ItemType) item);
			return true;
		}else {
			return false;
		}
	}
	
	public boolean insertItem(int i, Item item) {
		if(this.inventory.get(i)==null) {
			return setItem(i, item);
		}
		if(item.ID == this.inventory.get(i).ID) {
			this.inventory.get(i).addStack(item.stack);
			return true;
		}
		return false;
	}
	
	public ItemType removeItem(int index) {
			return inventory.set(index, null);
	}
	
	public ItemType removeItem(ItemType item) {
		return this.inventory.contains(item) ?
				this.removeItem(this.inventory.indexOf(item)) : null;
	}
	
	public boolean removeItem(Object item) {
		if (this.inventory.contains(item)) {
			return this.removeItem(this.inventory.indexOf(item))!=null;
		}
		return false;
	}
	
	public ItemType getItem(int i) {
		return this.inventory.get(i);
	}
	
	public boolean canContain(Item item) {
		return clazz.isInstance(item);
	}
	
	public int hasSpace(Item item) {
		int space = -1;
		int i = 0;
		for (ItemType cItem : inventory) {
			if(cItem==null) {
				space=i;
				i++;
				continue;
			}
			if(cItem.ID == item.ID && (cItem.stackMax-cItem.stack)>=item.stack){
				return i;
			}else {
				i++;
			}
		}
		return space;
	}
	
	public int invSize() {
		return this.invSize;
	}
	
	public int invWidth() {
		return this.invWidth;
	}
	
	public byte getItemPriority() {
		return this.itemPriority;
	}
	
	public void save(ArrayList<Byte> file) {
		ConvertData.I2B(file, this.ID);
		ConvertData.I2B(file, this.invSize);
		for(Item item : inventory){
			try{
				item.save(file);
			}catch(NullPointerException e){ConvertData.I2B(file, 0);}
		}
	}
	
	public void load(ArrayList<Byte> file) {
		this.invSize = ConvertData.B2I(file);
		initInventory();
		for(int i = 0; i < inventory.size(); i++){
			this.setItem(i, ItemList.NewItem(ConvertData.B2I(file)));
			if(inventory.get(i) != null)inventory.get(i).load(file);
		}
	}
}
