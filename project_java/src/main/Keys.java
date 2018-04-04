package main;

public enum Keys{
	UP("Up"), DOWN("Down"), LEFT("Left"), RIGHT("Right"),
	JUMP("Jump"), CROUCH("Crouch"),
	INV("Inventory"), EQUIP("Equipment"), CRAFT("Craft"), HOTBAR("Hotbar"),
	DEBUGPXL("Debug Pixel"), MENU("Menu"), DEBUGINFO("Debug Info"), DEBUGMODE("Debug Mode");
	
	private String name;
	private boolean pressed = false;
	private boolean clickable = true;
	
	private Keys(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isPressed(){
		return pressed;
	}
	
	public boolean isClickable(){
		return clickable;
	}
	
	protected void toggle(boolean isPressed){
		pressed = isPressed;
	}

	public boolean click(){
		if(clickable && pressed){
			clickable = false;
			return pressed;
		}else{
			if(!pressed){
				clickable = true;
			}
		}
		return false;
	}
}