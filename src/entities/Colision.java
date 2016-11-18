package entities;

import org.joml.Vector2d;

import entities.Hitbox;
import map.Map;

public class Colision{
	/**can be used to find out what sides have to be solid for interaction<p>
	 * example : <p><code><b>vector.rotation/(PI/2)</b></code> and <code><b>vector.rotation/(PI/2)+1</b></code>
	 * <p>can be used as indices to get the two solidities this vector can interact with.*/
	public static final int[] INTERACTIONS = {Map.SOLID_RIGHT, Map.SOLID_BOTTOM, Map.SOLID_LEFT, Map.SOLID_TOP};
	
	public static boolean onGround(Map map, Hitbox HB, int x, int y){
		for(int i = HB.xmin; i <= HB.xmax; i++){
			try{
				if(map.isSolid(x+i,y+HB.ymax+1)) return true;
			}catch(ArrayIndexOutOfBoundsException e){
				return true;
			}
		}
		return false;
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

	public static boolean canMoveTo(Map map, Hitbox HB, double x, double y, double mx, double my){
		
		return true;
	}
	
	public static int getInteraction(Vector2d vec) {
		if(vec.x == 0 || vec.y == 0) {
			return INTERACTIONS[(int) (vec.angle(new Vector2d(1,0))/Math.PI*2)];
		}else {
			return INTERACTIONS[(int)(vec.angle(new Vector2d(1,0))/Math.PI*2)] | INTERACTIONS[(int)(vec.angle(new Vector2d(1,0))/Math.PI*2)];
		}
	}

	public static boolean canWalkOver(Map map, Hitbox HB, int x, int y, int mx, int my){
		if(mx<0){
		for(int i = HB.ymin; i < HB.ymax; i++){
			if(map.isSolid(x+HB.xmin+mx,y+i)) return false;
		}}
		if(mx>0){
		for(int i = HB.ymin; i < HB.ymax; i++){
			if(map.isSolid(x+HB.xmax+mx,y+i)) return false;
		}}
		return true;
	}

}