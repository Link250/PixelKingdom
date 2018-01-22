package gameFields;

import java.util.ArrayList;

import entities.Player;
import gfx.Mouse;
import gfx.SpriteSheet;
import gfx.Mouse.MouseType;
import item.Bag;
import main.Game;
import main.MouseInput;

public class BagInv extends GameField {
	
	protected static SpriteSheet Background = new SpriteSheet("/Items/field.png");
	private int width;
	private String title;
	private ArrayList<ItemField> itemFields;
	
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
			itemField.setPosition(field.x+(x++)*38, field.y+y*38+fieldTop.height+2, false, false);
			if(x >= this.width) {
				x = 0;
				y++;
			}
		}
	}
	
	public void tick() {
		if(hasMoved())this.allignFields();
		if(mouseover()){
			Mouse.mouseType=MouseType.DEFAULT;
			int mouseX = (MouseInput.mouse.x-field.x), mouseY = (MouseInput.mouse.y-field.y-fieldTop.height);
			int index = mouseX/38 + mouseY/38*this.width;
			if(mouseX%38 >= 0 && mouseY%38 >= 0 && mouseX%38 <= 36 && mouseY%38 <= 36 && index >= 0 && index < this.itemFields.size()) {
				if(MouseInput.mousel.click()){
					this.itemFields.get(index).mouseClick();
				}else {
					this.itemFields.get(index).mouseOver();
				}
			}
		}
	}

	public void render() {
		renderfield();
		int nameSize = Game.sfont.renderLength(this.title, 0);
		if(nameSize<=fieldTop.width) {
			Game.sfont.render(field.x+fieldTop.width/2, field.y+fieldTop.height/2, this.title, fieldTop.width, 0xff000000);
		}else{
			Game.ccFont.render(field.x+fieldTop.width/2, field.y+fieldTop.height/2, this.title, fieldTop.width, 0xff000000);
		}
		for (ItemField itemField : itemFields) {
			itemField.render();
		}
	}
}
