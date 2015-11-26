package item;

import java.util.ArrayList;

import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import main.InputHandler;
import main.conversion.ConvertData;
import map.Map;

public abstract class BeltBag extends Bag {

	public static SpriteSheet typegfx = new SpriteSheet("/Items/BeltBag.png");

	public BeltBag(int size){
		super(size);
		inventory = new Item[size];
	}

	public static void show(Screen screen, int x, int y){
		screen.drawTile(x, y, 0, 0, typegfx, 0);
	}

	public abstract void useItem(InputHandler input, Player plr, Map map, Screen screen);

	public void save(ArrayList<Byte> file) {
		ConvertData.I2B(file, ID);
		ConvertData.I2B(file, inventory.length);
		for(Item item : inventory){
			try{
				item.save(file);
			}catch(NullPointerException e){ConvertData.I2B(file, 0);}
		}
	}

	public void load(ArrayList<Byte> file) {
		inventory = new Item[ConvertData.B2I(file)];
		for(int i = 0; i < inventory.length; i++){
			inventory[i] = ItemList.NewItem(ConvertData.B2I(file));
			if(inventory[i] != null)inventory[i].load(file);
		}
	}
}
