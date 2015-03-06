package entities;

import Maps.Map;
import entities.Hitbox;

public class Colision{

	
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