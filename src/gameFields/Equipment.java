package gameFields;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import entities.Player;
import entities.Player.BAG;
import gfx.Mouse;
import gfx.SpriteSheet;
import item.*;
import main.Game;
import main.PArea;

public class Equipment extends GameField {
	
	private SpriteSheet Background = new SpriteSheet("/Equipment.png");
	
	private EnumMap<BAG, PArea> bagAreas;
	private EnumMap<BAG, Bag<?>> bags;
	private Player player;
	
	public Equipment(Player player, EnumMap<BAG, Bag<?>> bags){
		super(70,59, 0);
		this.player = player;
		this.bags = bags;
		this.bagAreas = new EnumMap<>(BAG.class);
//		this.bagAreas.put(BAG.ARMOR_HEAD, new PArea(30,12,10,10));
//		this.bagAreas.put(BAG.ARMOR_BODY, new PArea(30,24,10,10));
//		this.bagAreas.put(BAG.ARMOR_LEGS, new PArea(30,36,10,10));
//		this.bagAreas.put(BAG.ARMOR_FEET, new PArea(30,48,10,10));
		this.bagAreas.put(BAG.TOOL_1, new PArea(15,18,10,10));
		this.bagAreas.put(BAG.MAT_1, new PArea(15,30,10,10));
		this.bagAreas.put(BAG.MAT_2, new PArea(15,42,10,10));
		this.bagAreas.put(BAG.BELT_1, new PArea(45,18,10,10));
		this.bagAreas.put(BAG.ITEM_1, new PArea(45,30,10,10));
		this.bagAreas.put(BAG.ITEM_2, new PArea(45,42,10,10));
	}
	
	public void tick() {
		Drag();
		if(mouseover(Game.input.mouse.x/Game.SCALE, Game.input.mouse.y/Game.SCALE)){
			Mouse.mousetype=0;
			if(Game.input.mousel.click()){
				int mx = Game.input.mousel.x/Game.SCALE, my = Game.input.mousel.y/Game.SCALE;
				for (BAG bag : BAG.values()) {
					if(this.bagAreas.get(bag).contains(mx, my)) {
						if(Mouse.Item==null) {
							Mouse.Item = player.unequipItem(bag);
						}else {
							if(player.equipItem(bag, Mouse.Item)) {
								Mouse.Item=null;
							}
						}
						break;
					}
				}
			}
		}
	}
	
	public void render() {
		renderfield();
		Game.screen.drawGUITile(Game.screen.xOffset+field.x, Game.screen.yOffset+field.y, 0, 0, Background, 0xff000000);
		Game.sfont.render(Game.screen.xOffset+field.x+2, Game.screen.yOffset+field.y+1, "Equipment", 0, 0xff000000, Game.screen);
		
		for (BAG bag : Player.BAG.values()) {
			if(this.bags.containsKey(bag)) {
				this.bags.get(bag).render(Game.screen, Game.screen.xOffset+this.bagAreas.get(bag).x, Game.screen.yOffset+this.bagAreas.get(bag).y);
			}else{
				Game.screen.drawGUITile(Game.screen.xOffset+this.bagAreas.get(bag).x, Game.screen.yOffset+this.bagAreas.get(bag).y, 0, 0, bag.defaultSprite, 0);
			}
		}
	}
}
