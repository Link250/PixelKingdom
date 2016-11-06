package entities.entityList;

import java.util.List;
import java.util.function.Predicate;

import entities.Colision;
import entities.Entity;
import entities.Hitbox;
import item.Item;
import map.Map;

public class ItemEntity extends Entity {
	public Item item;
	public Map map;
	
	public ItemEntity(Item item, Map map, int x, int y) {
		this.item = item;
		this.map = map;
		this.x = x;
		this.y = y;
	}
	
	public void tick(int numTick) {
		if(!Colision.canMove(map, new Hitbox(-1, -1, 1, 1), x, y, 0, speedY)) speedY--;
		else y+=speedY;
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
	}

}
