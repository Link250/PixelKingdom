package Items;

import java.util.ArrayList;

import Main.ConvertData;
import Main.InputHandler;
import Maps.Map;
import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;

public abstract class ToolBag extends Bag {
	
	public static SpriteSheet typegfx = new SpriteSheet("/Items/ToolBag.png");

	public ToolBag(int size){
		super(size);
		inventory = new Tool[size];
	}

	public static void show(Screen screen, int x, int y){
		screen.renderTile(x, y, 0, 0, typegfx, 0);
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
		inventory = new Tool[ConvertData.B2I(file)];
		for(int i = 0; i < inventory.length; i++){
			try{inventory[i] = (Tool) ItemList.NewItem(ConvertData.B2I(file));}catch(ClassCastException e){}
			if(inventory[i] != null)inventory[i].load(file);
		}
	}
}
