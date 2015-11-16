package item;

import gfx.SpriteSheet;

public abstract class Tool extends Item{
	protected int strength;
	protected int size;
	protected int type;
	protected int range;
	
	public Tool(){
		super();
		catgfx = new SpriteSheet("/Items/BeltBag.png");
	}

	public int getStregth(){
		return strength;
	}

	public int getType(){
		return type;
	}
}
