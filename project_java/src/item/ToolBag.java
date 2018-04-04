package item;

public abstract class ToolBag extends Bag<Tool> {
	
	public ToolBag(int size){
		super(Tool.class, size);
		this.displayName = "Tool Bag";
	}
}
