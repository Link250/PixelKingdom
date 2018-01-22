package entities;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2d;

import entities.Hitbox;
import map.Map;

public class Collision{
	public static final int X_COLLISION = 0b01;
	public static final int Y_COLLISION = 0b10;
	
	public List<Vector2d> collisionPos;
	public Vector2d entityPos;
	public double collisionStrength;
	public int collisionFlags;
	
	public Collision(List<Vector2d> collisionPos, Vector2d entityPos, double collisionStrength, int collisionFlags) {
		this.collisionPos = collisionPos;
		this.entityPos = entityPos;
		this.collisionStrength = collisionStrength;
		this.collisionFlags = collisionFlags;
	}
	
	public static Collision canMoveTo(Map map, Hitbox hitbox, double originX, double originY, Vector2d m){
		return canMoveTo(map, hitbox, originX, originY, m, false);
	}
	
	private static final double stepsize = 0.5;
	
	public static Collision canMoveTo(Map map, Hitbox hitbox, double originX, double originY, Vector2d m, boolean canSlip){
		int interaction = getInteraction(m);
		int collisionFlags = 0;
		List<Vector2d> collisionPos = new ArrayList<>();
		double l = m.length();
		double newX = originX, newY = originY, lastX = originX, lastY = originY;
//		System.out.printf("eX:%f eY:%f sX:%f sY:%f\n", c.entityPos.x, c.entityPos.y, speedX, speedY);
		MainCheck:
		for (double sl = 0; sl <= l+stepsize+0.1; sl+=stepsize) {
			if(sl<=l) {
				newX = originX + sl/l*m.x;
				newY = originY + sl/l*m.y;
			}else {
				newX = originX + m.x;
				newY = originY + m.y;
			}
//			System.out.printf("eX:%f eY:%f sX:%f sY:%f\n", c.entityPos.x, c.entityPos.y, speedX, speedY);
			//gets the first Collision on the Y axis
			if((collisionFlags & Collision.Y_COLLISION) == 0 && m.y!=0){
				for(double i = hitbox.xmin; i <= hitbox.xmax; i+=0.5){
					if(i>hitbox.xmax)i = hitbox.xmax;
					if((map.getSolidity(lastX+i,newY+(m.y<0 ? hitbox.ymin : hitbox.ymax)) & interaction) != 0) {
/*						if(canSlip) {
							if((collisionFlags & Collision.X_COLLISION) == 0 && getCollision(map, hitbox, interaction, null, lastX+(m.x < 0 ? -1 : 1), newY, lastX, lastY) == 0) {
								lastX = lastX + (m.x < 0 ? -1 : 1);
								originX += (m.x < 0 ? -1 : 1) - stepsize/l*m.x;
								lastY = newY;
								continue MainCheck;
							}else if(getCollision(map, hitbox, interaction ^ Map.SOLID_X_AXIS, null, lastX+(m.x < 0 ? 1 : -1), newY, lastX, lastY) == 0){
								lastX = lastX + (m.x < 0 ? 1 : -1);
								originX += (m.x < 0 ? 1 : -1) - stepsize/l*m.x;
								lastY = newY;
								continue MainCheck;
							}
						}*/
						collisionPos.add(new Vector2d(lastX+i,newY+(m.y<0 ? hitbox.ymin : hitbox.ymax)));
						collisionFlags |= Y_COLLISION;
					}
//					System.out.printf("pX:%f pY:%f ccc\n", x+i,y+(m.y<0 ? hitbox.ymin : hitbox.ymax));
				}
			}
			
			//gets the first Collision on the X axis
			if((collisionFlags & Collision.X_COLLISION) == 0 && m.x!=0){
				for(double i = hitbox.ymin; i <= hitbox.ymax; i+=0.5){
					if(i>hitbox.ymax)i = hitbox.ymax;
					if((map.getSolidity(newX+(m.x<0 ? hitbox.xmin : hitbox.xmax),lastY+i) & interaction) != 0) {
						if(canSlip) {
							if((collisionFlags & Collision.Y_COLLISION) == 0 && getCollision(map, hitbox, interaction, null, newX, lastY+(m.y < 0 ? -1 : 1), lastX, lastY) == 0) {
								lastY = lastY + (m.y < 0 ? -1 : 1);
								originY += (m.y < 0 ? -1 : 1) - stepsize/l*m.y;
								lastX = newX;
								continue MainCheck;
							}else if(getCollision(map, hitbox, interaction ^ Map.SOLID_Y_AXIS, null, newX, lastY+(m.y < 0 ? 1 : -1), lastX, lastY) == 0){
								lastY = lastY + (m.y < 0 ? 1 : -1);
								originY += (m.y < 0 ? 1 : -1) - stepsize/l*m.y;
								lastX = newX;
								continue MainCheck;
							}
						}
						collisionPos.add(new Vector2d(newX+(m.x<0 ? hitbox.xmin : hitbox.xmax),lastY+i));
						collisionFlags |= X_COLLISION;
					}
//					System.out.printf("pX:%f pY:%f ccc\n", x+(m.x<0 ? hitbox.xmin : hitbox.xmax),y+i);
				}
			}
  			if((collisionFlags & Collision.X_COLLISION) > 0 && (collisionFlags & Collision.Y_COLLISION) > 0) {
  				break;
			}else {
				if((collisionFlags & Collision.X_COLLISION) == 0)lastX = newX;
				if((collisionFlags & Collision.Y_COLLISION) == 0)lastY = newY;
			}
		}
		if(collisionPos.size()>0 || canSlip) {
			return new Collision(collisionPos, new Vector2d(lastX, lastY), l, collisionFlags);
		}else {
			return null;
		}
	}
	
	public static int getCollision(Map map, Hitbox hitbox, int interaction, List<Vector2d> collisionPos, double newX, double newY, double lastX, double lastY) {
		int collisionFlags = 0;
		for(double i = hitbox.xmin; i <= hitbox.xmax; i+=0.5){
			if(i>hitbox.xmax)i = hitbox.xmax;
			if((map.getSolidity(newX+i,newY+(newY < lastY ? hitbox.ymin : hitbox.ymax)) & interaction) != 0) {
				if(collisionPos != null)collisionPos.add(new Vector2d(newX+i,newY+(newY < lastY ? hitbox.ymin : hitbox.ymax)));
				collisionFlags |= Y_COLLISION;
			}
		}
		
		for(double i = hitbox.ymin; i <= hitbox.ymax; i+=0.5){
			if(i>hitbox.ymax)i = hitbox.ymax;
			if((map.getSolidity(newX+(newX < lastX ? hitbox.xmin : hitbox.xmax),newY+i) & interaction) != 0) {
				if(collisionPos != null)collisionPos.add(new Vector2d(newX+(newX < lastX ? hitbox.xmin : hitbox.xmax),newY+i));
				collisionFlags |= X_COLLISION;
			}
		}
		
		return collisionFlags;
	}
	
	public static int getInteraction(Vector2d vec) {
		return (vec.x<0 ? Map.SOLID_RIGHT : vec.x>0 ? Map.SOLID_LEFT : 0) | (vec.y<0 ? Map.SOLID_BOTTOM : vec.y>0 ? Map.SOLID_TOP : 0);
	}
}