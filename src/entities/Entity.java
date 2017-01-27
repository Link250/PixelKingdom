package entities;

import static dataUtils.PhysicConstants.*;

public abstract class Entity {
	
	public double x, y ,xOffset ,yOffset;
	protected double speedX, speedY;
	public boolean onGround = false;
	
	public Entity(){
		
	}
	
	public abstract void tick(int numTick);
	
	public abstract void render ();
	
	public void applyGravity(){
//		if(!onGround){
			speedY += g_earth/m*s;
//		}
	}
	
	public void setSpeed(double x, double y) {
		this.speedX = x;
		this.speedY = y;
	}
}
