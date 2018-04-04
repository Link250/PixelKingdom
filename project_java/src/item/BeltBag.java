package item;

public abstract class BeltBag extends Bag<Item> {

	public BeltBag(int size){
		super(Item.class, size, 4);
		this.itemPriority = Byte.MAX_VALUE;
		this.displayName = "Belt Bag";
	}
}
