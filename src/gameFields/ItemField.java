package gameFields;

import dataUtils.PArea;
import gfx.Mouse;
import gfx.SpriteSheet;
import item.Item;
import main.Game;

public class ItemField {
	protected static SpriteSheet back = new SpriteSheet("/Items/field.png");
	
	protected PArea field;
	protected Item item;
	
	public ItemField() {
		this.field = new PArea(0,0,36,36);
	}
	
	public ItemField(int x, int y) {
		this.field = new PArea(x,y,36,36);
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
		return item;
	}

	public boolean setItem(Item item) {
		this.item = item;
		return true;
	}
	
	public void mouseClick() {
		Item temp = this.item;
		this.item = Mouse.Item;
		Mouse.Item = temp;
	}

	public void mouseOver() {
		Mouse.setText(item.getTooltip());
	}
	
	public void render() {
		Game.screen.drawTileOGL(field.x, field.y, 0, back);
//		Game.screen.drawGUITile(field.x, field.y, 0, 0, back, 0);
		if(this.item != null)this.item.render(Game.screen, field.x+2, field.y+2, true);
	}
}
