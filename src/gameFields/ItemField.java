package gameFields;

import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;
import item.Item;
import main.Game;
import main.PArea;

public class ItemField {
	private static SpriteSheet back = new SpriteSheet("/Items/field.png");
	
	private PArea field;
	public Item item;
	
	public ItemField(PArea field) {
		this.field = field;
	}
	
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	public void mouseClick() {
		Item temp = this.item;
		this.item = Mouse.Item;
		Mouse.Item = temp;
	}

	public void mouseOver() {
		Game.logInfo("mouseover");
	}
	
	public void render(Screen screen) {
		Game.screen.drawGUITile(field.x, field.y, 0, 0, back, 0);
		if(this.item != null)this.item.render(screen, field.x, field.y, true);
	}
}
