package entities.entityList;

import entities.Collision;
import entities.Hitbox;
import entities.Mob;
import gfx.Screen;
import gfx.SpriteSheet;
import map.Map;

public class Slime extends Mob {

	private Hitbox col = new Hitbox(-3,3,-2,2);
	
	public Slime(Map map, int x, int y) {
		super(map, "Slime", x, y, new SpriteSheet("/Mobs/slime.png", 33, 21));
		xOffset=6;
		yOffset=4;
	}

	public void tick(int numTick) {
		int n;
		if(speedY < 0){
			for(n = 0; n >= speedY; n--){
				if(!Collision.canMove(map, col, (int)x, (int)y, 0, n)) speedY = n+1;
			}
		}else{
			for(n = 0; n <= speedY; n++){
				if(!Collision.canMove(map, col, (int)x, (int)y, 0, n)) speedY = n-1;
			}
		}
		y += speedY;
	}

	public void render() {
		Screen.drawMapSprite((int)(x-xOffset), (int)(y-yOffset), sheet);
	}
	
}
