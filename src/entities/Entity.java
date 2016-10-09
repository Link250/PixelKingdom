package entities;

public abstract class Entity {
	
	public int x, y ,xOffset ,yOffset;
	protected int gravity = 1;
	protected int speedX, speedY;
	public boolean isinair = true;
	
	public Entity(){
		init();
	}
	
	public final void init(){
		
	}
	
	public abstract void tick(int numTick);
	
	public abstract void render ();
	
	public void applyGravity(){
		if(gravity != 0 && isinair){
			if(speedX < -1) speedX = -1;
			if(speedX > 1) speedX = 1;
			speedY += gravity;
		}
	}
	
}
