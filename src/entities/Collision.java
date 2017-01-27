package entities;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2d;
import entities.Hitbox;
import map.Map;

public class Collision{
	public static int X_COLLISION = 0b01;
	public static int Y_COLLISION = 0b10;
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
	
	@Deprecated
	public static boolean canMove(Map map, Hitbox HB, int x, int y, int mx, int my){
		if(my<0){
		for(int i = (int) HB.xmin; i <= HB.xmax; i++){
			if(map.isSolid(x+i,(int) (y+HB.ymin+my))) return false;
		}}
		if(my>0){
		for(int i = (int) HB.xmin; i <= HB.xmax; i++){
			if(map.isSolid(x+i,(int) (y+HB.ymax+my))) return false;
		}}
		if(mx<0){
		for(int i = (int) HB.ymin; i <= HB.ymax; i++){
			if(map.isSolid((int) (x+HB.xmin+mx),y+i)) return false;
		}}
		if(mx>0){
		for(int i = (int) HB.ymin; i <= HB.ymax; i++){
			if(map.isSolid((int) (x+HB.xmax+mx),y+i)) return false;
		}}
		return true;
	}

	public static Collision canMoveTo(Map map, Hitbox hitbox, double originX, double originY, Vector2d m){
		int interaction = getInteraction(m);
		int collisionFlags = 0;
		List<Vector2d> collisionPos = new ArrayList<>();
		double l = m.length();
		double x = originX, y = originY, lastX = originX, lastY = originY;
//		System.out.printf("eX:%f eY:%f sX:%f sY:%f\n", c.entityPos.x, c.entityPos.y, speedX, speedY);
		for (double sl = 0; sl <= l+0.6; sl+=0.5) {
			if(sl<=l) {
				x = originX + sl/l*m.x;
				y = originY + sl/l*m.y;
			}else {
				x = originX + m.x;
				y = originY + m.y;
			}
//			System.out.printf("eX:%f eY:%f sX:%f sY:%f\n", c.entityPos.x, c.entityPos.y, speedX, speedY);
			if(m.y!=0){
			for(double i = hitbox.xmin; i <= hitbox.xmax; i+=0.5){
				if(i>hitbox.xmax)i = hitbox.xmax;
				if((map.getSolidity(lastX+i,y+(m.y<0 ? hitbox.ymin : hitbox.ymax)) & interaction) != 0) {
					collisionPos.add(new Vector2d(lastX+i,y+(m.y<0 ? hitbox.ymin : hitbox.ymax)));
					collisionFlags |= Y_COLLISION;
				}
//				System.out.printf("pX:%f pY:%f ccc\n", x+i,y+(m.y<0 ? hitbox.ymin : hitbox.ymax));
			}}
			if(m.x!=0){
			for(double i = hitbox.ymin; i <= hitbox.ymax; i+=0.5){
				if(i>hitbox.ymax)i = hitbox.ymax;
				if((map.getSolidity(x+(m.x<0 ? hitbox.xmin : hitbox.xmax),lastY+i) & interaction) != 0) {
					collisionPos.add(new Vector2d(x+(m.x<0 ? hitbox.xmin : hitbox.xmax),lastY+i));
					collisionFlags |= X_COLLISION;
				}
//				System.out.printf("pX:%f pY:%f ccc\n", x+(m.x<0 ? hitbox.xmin : hitbox.xmax),y+i);
			}}
			
  			if(collisionPos.size()>0) {
				return new Collision(collisionPos, new Vector2d(lastX, lastY), l, collisionFlags);
			}else {
				lastX = x;
				lastY = y;
			}
		}
		return null;
	}
	
	public static int getInteraction(Vector2d vec) {
		return (vec.x<0 ? Map.SOLID_RIGHT : vec.x>0 ? Map.SOLID_LEFT : 0) | (vec.y<0 ? Map.SOLID_BOTTOM : vec.y>0 ? Map.SOLID_TOP : 0);
	}

	public static boolean onGround(Map map, Hitbox HB, int x, int y){
		for(int i = (int) HB.xmin; i <= HB.xmax; i++){
			if(map.isSolid(x+i,(int) (y+HB.ymax+1))) return true;
		}
		return false;
	}
	
	public static boolean canWalkOver(Map map, Hitbox HB, int x, int y, int mx, int my){
		for(int i = (int) HB.ymin; i < HB.ymax; i++){
			if (map.isSolid((int) (x + HB.xmin + mx), y + i))
				return false;
		}
		return true;
	}

}