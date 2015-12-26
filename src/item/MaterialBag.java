package item;

import item.itemList.MatStack;

public abstract class MaterialBag extends Bag<MatStack> {
	
	public MaterialBag(int size){
		super(MatStack.class, size);
		this.displayName = "MaterialBag";
	}
}
