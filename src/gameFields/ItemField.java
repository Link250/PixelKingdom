package gameFields;

import java.util.function.Predicate;
import java.util.function.Supplier;

import dataUtils.PArea;
import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;
import item.Item;

public class ItemField {
	protected static final SpriteSheet back = new SpriteSheet("/Items/field.png");
	
	protected PArea field;
	protected Item item;
	
	protected SpriteSheet defaultSprite;
	
	
	protected Supplier<Item> alternateGetter;
	protected Predicate<Item> alternateSetter;
	
	protected boolean itemLocked = false;
	protected boolean alternateGetSet = false;
	
	public ItemField() {
		this.field = new PArea(0,0,36,36);
	}
	
	public ItemField(int x, int y) {
		this.field = new PArea(x,y,36,36);
	}
	
	public ItemField(Item item) {
		this.field = new PArea(0,0,36,36);
		this.item = item;
	}
	
	public ItemField(int x, int y, Item item) {
		this.field = new PArea(x,y,36,36);
		this.item = item;
	}
	
	public void setPosition(int x, int y) {
		this.setPosition(x, y, true, true);
	}
	
	public void setPosition(int x, int y, boolean centeredX, boolean centeredY) {
		this.field.x = centeredX ? x - field.width/2 : x;
		this.field.y = centeredY ? y - field.height/2 : y;
	}
	
	public PArea getField() {
		return this.field;
	}
	
	public Item getItem() {
		return alternateGetSet ? alternateGetter.get() : item;
	}

	public boolean setItem(Item item) {
		if(alternateGetSet) {
			return alternateSetter.test(item);
		}else {
			this.item = item;
			return true;
		}
	}
	
	public void setGetterAndSetter(Supplier<Item> getter, Predicate<Item> setter) {
		this.alternateGetter = getter;
		this.alternateSetter = setter;
		this.alternateGetSet = true;
	}
	
	public void setDefaultSprite(SpriteSheet sprite) {
		this.defaultSprite = sprite;
	}
	
	public void mouseClick() {
		if(!itemLocked) {
			if(getItem() != null && Mouse.item != null && getItem().getStack() != getItem().getStackMax()) {
				if(getItem().addStack(Mouse.item, Mouse.item.getStack()) == 0) {
					Mouse.item = null;
				}
			}else {
				Item temp = getItem();
				if(setItem(Mouse.item)) {
					Mouse.item = temp;
				}
			}
		}
	}

	public void mouseOver() {
		Mouse.setText(getItem()!=null ? getItem().getTooltip() : null);
	}
	
	public void render() {
		Screen.drawGUISprite(field.x, field.y, back);
		if(getItem() != null) {
			getItem().render(field.x+2, field.y+2, true);
		}else if(defaultSprite != null){
			Screen.drawGUISprite(field.x+2, field.y+2, defaultSprite);
		}
	}

	public void render(int stackSize) {
		Screen.drawGUISprite(field.x, field.y, back);
		if(getItem() != null) {
			getItem().render(field.x+2, field.y+2, stackSize);
		}else if(defaultSprite != null){
			Screen.drawGUISprite(field.x+2, field.y+2, defaultSprite);
		}
	}
}
