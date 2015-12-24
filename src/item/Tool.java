package item;

public abstract class Tool extends Item{
	protected int strength;
	protected int size;
	protected int type;
	protected int range;
	
	public Tool(){
		super();
	}

	public int getStregth(){
		return strength;
	}

	public int getType(){
		return type;
	}
}
