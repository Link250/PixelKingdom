package entities;

import gfx.SpriteSheet;
import map.Map;


public abstract class Mob extends Entity{
	
	protected String name;
	protected Map map;
	protected SpriteSheet sheet;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir;
	protected int scale = 1;
	
	public Mob(Map map, String name, int x, int y, SpriteSheet sheet){
		this.map = map;
		this.name = name;
		this.sheet = sheet;
		this.x = x;
		this.y = y;
	}
	
	public void AddSpeed(int xs, int ys){
		speedX += xs;
		speedY += ys;
	}
		
	public String getName(){
		return name;
	}

	public int getspeedX(){
		return (int)speedX;
	}

	public int getspeedY(){
		return(int)speedY;
	}

}
