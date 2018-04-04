package entities.entityList;

import java.util.List;
import java.util.function.Predicate;

import org.joml.Vector2d;

import entities.Collision;
import entities.Entity;
import entities.Hitbox;
import item.Item;
import map.Map;

public class ItemEntity extends Entity {
	public Item item;
	public Map map;
	private Hitbox hitbox = new Hitbox(-1.5, 1.5, -1.5, 1.5);
	
	public ItemEntity(Item item, Map map, int x, int y) {
		this.item = item;
		this.map = map;
		this.x = x;
		this.y = y;
		this.speedX = 0;
		this.speedY = 0;
//		double rad = Math.random()*Math.PI*2;
//		this.speedX = Math.cos(rad);
//		this.speedY = Math.sin(rad);
	}
	
	public void tick(int numTick) {
		Collision c;
		if((c = Collision.canMoveTo(map, hitbox, x, y, new Vector2d(speedX, speedY)))==null) {
			x+= speedX;
			y+= speedY;
		}else {
			if((c.collisionFlags & Collision.X_COLLISION) != 0) {
				speedX = 0;
				x = c.entityPos.x;
			}else {
				x += speedX;
			}
			if((c.collisionFlags & Collision.Y_COLLISION) != 0) {
				speedY = 0;
				y = c.entityPos.y;
			}else {
				y += speedY;
			}
		}
	}
	
	public void combine(List<ItemEntity> list) {
		if(this.item.getStack()<this.item.getStackMax()) {
			list.stream().filter(i -> 
			(Math.abs(x-i.x)<=10 && Math.abs(y-i.y)<=10) &&
			i.item.getID() == this.item.getID() &&
			i.item.getStack()>0 &&
			i.item.getStack()<item.getStackMax() &&
			!i.equals(this)).forEach(i -> {
				item.addStack(i.item, i.item.getStack());
//				System.out.println(item.getStack()+" "+i.item.getStack());
			});
		}
	}
	
	public void collectWith(Predicate<Item> collecter) {
		if(collecter.test(item))map.removeItemEntitiy(this);
	}
	
	public void render() {
		item.renderOnMap(x, y, false, false, true);
		hitbox.render(x, y);
	}

}
