package item;

public abstract class ItemBag extends Bag<Item> {
	
	public ItemBag(int size){
		super(Item.class, size);
		this.displayName = "Item Bag";
	}
}
