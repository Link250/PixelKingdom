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
		tick = true;
		solid = false;
	}

	public boolean tick(int x, int y, int l, int numTick, Map map) {
		map.setlighter(x, y, Map.MAX_LIGHT);
		return true;
	}
	
	public void render(int x, int y, int l, Map map, Screen screen) {
		screen.drawTile(x-1, y-1, 0, 0, sprite, 0);
	}
}
