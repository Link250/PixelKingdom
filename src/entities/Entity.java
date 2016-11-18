package entities;

import static dataUtils.PhysicConstants.*;

public abstract class Entity {
	
	public double x, y ,xOffset ,yOffset;
	protected double speedX, speedY;
	public boolean isinair = true;
	
	public Entity(){
		
	}
	
	public abstract void tick(int numTick);
	
	public abstract void render ();
	
	public void applyGravity(){
		if(isinair){
			speedY += g_earth*m;
		}
	}
	
}
