package pixel.pixelList;

import gfx.Screen;
import gfx.SpriteSheet;
import map.Map;
import pixel.AD;
import pixel.Material;

public class Torch extends Material<AD>{
	
	private SpriteSheet sprite = new SpriteSheet("/Items/Torch.png");
	
	public Torch(){
		super(null);
		usePickaxe=1;
		ID = 48;
		name = "Torch";
		displayName = "Torch";
		tick = true;
		solid = false;
	}

	public byte tickLight(int x, int y, int l, Map map) {
		return Map.MAX_LIGHT;
	}
	
	public void render(int x, int y, int l, Map map, Screen screen) {
		screen.drawMapTile(x-1, y-1, 0, 0, sprite, 0);
	}
}
