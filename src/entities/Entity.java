package entities;

public abstract class Entity {
	
	public int x, y ,xOffset ,yOffset;
	
	public Entity(){
		init();
	}
	
	public final void init(){
		
	}
	
	public abstract void tick(int numTick);
	
	public abstract void render ();
}
