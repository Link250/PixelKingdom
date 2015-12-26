package gameFields;

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
	
	public void mouseover() {
		
	}
	
	public void render() {
		Game.screen.drawGUITile(field.x, field.y, 0, 0, back, 0);
	}
}
