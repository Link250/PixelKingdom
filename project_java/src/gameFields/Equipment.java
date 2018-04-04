package gameFields;

import java.util.EnumMap;
import entities.Player;
import entities.Player.BAG;
import gfx.Mouse;
import gfx.SpriteSheet;
import gfx.Mouse.MouseType;
import gfx.Screen;
import item.*;
import main.MainConfig.GameFields;
import main.MouseInput;
import main.Game;

public class Equipment extends GameField {
	
	private SpriteSheet Background = new SpriteSheet("/Equipment.png");
	
	private EnumMap<BAG, EquipItemField> itemFields;
	
	public Equipment(Player player, EnumMap<BAG, Bag<?>> bags){
		super(210,181, GameFields.Field_Equipment);
		this.itemFields = new EnumMap<>(BAG.class);
		this.itemFields.put(BAG.TOOL_1, new EquipItemField(bags, BAG.TOOL_1, player));
		this.itemFields.put(BAG.MAT_1, new EquipItemField(bags, BAG.MAT_1, player));
		this.itemFields.put(BAG.MAT_2, new EquipItemField(bags, BAG.MAT_2, player));
		this.itemFields.put(BAG.BELT_1, new EquipItemField(bags, BAG.BELT_1, player));
		this.itemFields.put(BAG.ITEM_1, new EquipItemField(bags, BAG.ITEM_1, player));
		this.itemFields.put(BAG.ITEM_2, new EquipItemField(bags, BAG.ITEM_2, player));
//		this.bagAreas.put(BAG.ARMOR_HEAD, new PArea(30,12,10,10));
//		this.bagAreas.put(BAG.ARMOR_BODY, new PArea(30,24,10,10));
//		this.bagAreas.put(BAG.ARMOR_LEGS, new PArea(30,36,10,10));
//		this.bagAreas.put(BAG.ARMOR_FEET, new PArea(30,48,10,10));
		allignFields();
	}
	
	private void allignFields() {
		this.itemFields.get(BAG.TOOL_1).setPosition(field.x+field.width/4,field.y+field.height/2+fieldTop.height/2-38);
		this.itemFields.get(BAG.MAT_1).setPosition(field.x+field.width/4,field.y+field.height/2+fieldTop.height/2);
		this.itemFields.get(BAG.MAT_2).setPosition(field.x+field.width/4,field.y+field.height/2+fieldTop.height/2+38);
		this.itemFields.get(BAG.BELT_1).setPosition(field.x+field.width/4*3,field.y+field.height/2+fieldTop.height/2-38);
		this.itemFields.get(BAG.ITEM_1).setPosition(field.x+field.width/4*3,field.y+field.height/2+fieldTop.height/2);
		this.itemFields.get(BAG.ITEM_2).setPosition(field.x+field.width/4*3,field.y+field.height/2+fieldTop.height/2+38);
	}
	
	public void tick() {
		if(hasMoved())allignFields();
		if(mouseover()){
		Mouse.mouseType=MouseType.DEFAULT;
			for (BAG bag : BAG.values()) {
				if(this.itemFields.get(bag).getField().containsMouse(MouseInput.mouse)) {
					if(MouseInput.mousel.click()){
						this.itemFields.get(bag).mouseClick();
					}else {
						this.itemFields.get(bag).mouseOver();
					}
					break;
				}
			}
		}
	}
	
	public void render() {
		renderfield();
		Screen.drawGUISprite(field.x+87, field.y+37, Background);
		Game.sfont.render(field.x+field.width/2, field.y+fieldTop.height/2, "Equipment", 0, 0xff000000);
		
		for (EquipItemField field : itemFields.values()) {
			field.render();
		}
	}
	
	public static class EquipItemField extends ItemField{
		private BAG bagEnum;
		private EnumMap<BAG, Bag<?>> bags;
		private Player plr;
		
		public EquipItemField(EnumMap<BAG, Bag<?>> bags, BAG bagIndex, Player plr) {
			super();
			this.bags = bags;
			this.bagEnum = bagIndex;
			this.plr = plr;
		}
		
		public Item getItem() {
			return bags.get(bagEnum);
		}
		
		public boolean setItem(Item item) {
			return plr.equipItem(bagEnum, item);
		}
		
		public void mouseClick() {
			if(Mouse.item!=null) {
				Item tempItem;
				tempItem = plr.unequipItem(bagEnum);
				if(plr.equipItem(bagEnum, Mouse.item)) Mouse.item = tempItem;
				else plr.equipItem(bagEnum, tempItem);
			}else {
				Mouse.item = plr.unequipItem(bagEnum);
			}
		}
		
		public void mouseOver() {
			if(getItem()!=null)Mouse.setText(getItem().getTooltip());
		}
		
		public void render() {
			Screen.drawGUISprite(field.x, field.y, back);
			if(bags.containsKey(bagEnum)) {
				bags.get(bagEnum).render(field.x+2, field.y+2, true);
			}else{
				Screen.drawGUISprite(field.x+2, field.y+2, bagEnum.defaultSprite, 0);
//				Game.screen.drawGUITile(field.x+2, field.y+2, 0, 0, bagEnum.defaultSprite, 0);
			}
		}
	}
}
