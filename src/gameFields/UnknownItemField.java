package gameFields;

import java.util.ArrayList;

import gfx.Mouse;
import gfx.Screen;
import gfx.SpriteSheet;
import item.Item;

public class UnknownItemField extends ItemField {
	
	protected static final SpriteSheet mainSprite = new SpriteSheet("/Items/fieldUnknown.png");
	protected static final ArrayList<String> unknownMouseOver = new ArrayList<>();
	static {
		unknownMouseOver.add("Unknown Item");
	}
	
	public Item getItem() {
		return null;
	}

	public boolean setItem(Item item) {
		return false;
	}
	
	public void mouseClick() {
		return;
	}

	public void mouseOver() {
		Mouse.setText(unknownMouseOver);
	}
	
	public void render() {
		Screen.drawGUISprite(field.x, field.y, mainSprite);
	}

	public void render(int stackSize) {
		Screen.drawGUISprite(field.x, field.y, mainSprite);
	}
}
