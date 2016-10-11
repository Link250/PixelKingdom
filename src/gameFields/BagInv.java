package gameFields;

import java.util.ArrayList;

import entities.Player;
import gfx.Mouse;
import gfx.SpriteSheet;
import item.Bag;
import item.Item;
import main.Game;

public class BagInv extends GameField {
	
	protected static SpriteSheet Background = new SpriteSheet("/Items/field.png");
	private int width;
	private String title;
	private ArrayList<BagItemField> itemFields;
	
	public Bag<?> bag;
	
	public BagInv(Bag<?> bag, Player.BAG bagEnum) {
		super(bagEnum.fieldEnum);
		this.width = bag.invWidth();
		this.setSize(width*38-2, bag.invSize()/width*38);
		this.bag = bag;
		this.title = bag.getDisplayName();
		this.itemFields = new ArrayList<>();
		this.bag.createFields(itemFields);
		this.allignFields();
	}
	
	private void allignFields() {
		int x=0, y=0;
		for (ItemField itemField : itemFields) {
			itemField.setPosition(field.x+(x++)*38, field.y+y*38+34, false, false);
			if(x >= this.width) {
				x = 0;
				y++;
			}
		}
	}
	
	public void tick() {
		if(Drag())this.allignFields();
		if(mouseover(Game.input.mouse.x, Game.input.mouse.y)){
			Mouse.mousetype=0;
			if(Game.input.mousel.click()){
				int mouseX = (Game.input.mousel.x-field.x), mouseY = (Game.input.mousel.y-field.y-32);
				int index = mouseX/38 + mouseY/38*this.width;
				if(mouseX%38 <= 36 && mouseY%38 <= 36 && index >= 0 && index < this.itemFields.size()) {
					this.itemFields.get(index).mouseClick();
				}
			}
		}
	}

	public void render() {
		renderfield();
		Game.sfont.render(field.x+fieldTop.width/2, field.y+fieldTop.height/2, this.title, fieldTop.width, 0xff000000, Game.screen);
		for (ItemField itemField : itemFields) {
			itemField.render();
		}
	}
	
	public static class BagItemField extends ItemField{
		protected int index;
		protected Bag<?> bag;
		
		public BagItemField linkToBag(Bag<?> bag, int index) {
			this.bag = bag;
			this.index = index;
			return this;
		}
		
		public Item getItem() {
			return bag.getItem(index);
		}

		public boolean setItem(Item item) {
			return bag.setItem(index, item);
		}
		
		public void mouseClick() {
			if(bag.canContain(Mouse.Item) || Mouse.Item == null) {
				Item temp = bag.removeItem(index);
				if(Mouse.Item!=null)bag.setItem(index, Mouse.Item);
				Mouse.Item = temp;
			}
		}

		public void render() {
			Game.screen.drawGUITile(field.x, field.y, 0, 0, back, 0);
			if(bag.getItem(index)!=null) bag.getItem(index).render(Game.screen, field.x+2, field.y+2, true);
		}
	}
}
