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
	
	public static boolean canMove(Map map, Hitbox HB, int x, int y, int mx, int my){
		if(my<0){
		for(int i = HB.xmin; i <= HB.xmax; i++){
			if(map.isSolid(x+i,y+HB.ymin+my)) return false;
		}}
		if(my>0){
		for(int i = HB.xmin; i <= HB.xmax; i++){
			if(map.isSolid(x+i,y+HB.ymax+my)) return false;
		}}
		if(mx<0){
		for(int i = HB.ymin; i <= HB.ymax; i++){
			if(map.isSolid(x+HB.xmin+mx,y+i)) return false;
		}}
		if(mx>0){
		for(int i = HB.ymin; i <= HB.ymax; i++){
			if(map.isSolid(x+HB.xmax+mx,y+i)) return false;
		}}
		return true;
	}

	public static Collision canMoveTo(Map map, Hitbox hitbox, double originX, double originY, Vector2d m){
		int interaction = getInteraction(m);
		int collisionFlags = 0;
		List<Vector2d> collisionPos = new ArrayList<>();
		double l = m.length();
		double x = originX, y = originY;
		for (double sl = 0; sl < l; sl++) {
			x = originX + sl*m.x/l;
			y = originY + sl*m.y/l;
			if(m.y!=0){
			for(int i = hitbox.xmin; i <= hitbox.xmax; i++){
				if((map.getSolidity(x+i,y+(m.y<0 ? hitbox.ymin : hitbox.ymax)) & interaction) != 0) {
					collisionPos.add(new Vector2d(x+i,y+(m.y<0 ? hitbox.ymin : hitbox.ymax)));
					collisionFlags |= Y_COLLISION;
				}
			}}
			if(m.x!=0){
			for(int i = hitbox.ymin; i <= hitbox.ymax; i++){
				if((map.getSolidity(x+(m.x<0 ? hitbox.xmin : hitbox.xmax),y+i) & interaction) != 0) {
					collisionPos.add(new Vector2d(x+(m.x<0 ? hitbox.xmin : hitbox.xmax),y+i));
					collisionFlags |= X_COLLISION;
				}
			}}
			if(collisionPos.size()>0) {
				if(sl>0)sl--;
				return new Collision(collisionPos, new Vector2d(originX + sl*m.x/l, originY + sl*m.y/l), l, collisionFlags);
			}
		}
		return null;
	}
	
	public static int getInteraction(Vector2d vec) {
		return (vec.x<0 ? Map.SOLID_RIGHT : vec.x>0 ? Map.SOLID_LEFT : 0) | (vec.y<0 ? Map.SOLID_BOTTOM : vec.y>0 ? Map.SOLID_TOP : 0);
	}

	public static boolean onGround(Map map, Hitbox HB, int x, int y){
		for(int i = HB.xmin; i <= HB.xmax; i++){
			if(map.isSolid(x+i,y+HB.ymax+1)) return true;
		}
		return false;
	}
	
	public static boolean canWalkOver(Map map, Hitbox HB, int x, int y, int mx, int my){
		for(int i = HB.ymin; i < HB.ymax; i++){
			if (map.isSolid(x + HB.xmin + mx, y + i))
				return false;
		}
		return true;
	}

}