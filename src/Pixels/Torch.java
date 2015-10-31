package Pixels;

import gfx.Screen;
import gfx.SpriteSheet;
import Maps.Map;

public class Torch extends Material{
	
	private SpriteSheet sprite = new SpriteSheet("/Items/Torch.png");
	
	public Torch(){
		usePickaxe=1;
		ID = 48;
		name = "Torch";
		tick = true;
		solid = false;
	}

	public boolean tick(int x, int y, int l, int numTick, Map map) {
		map.setlighter(x, y, (byte)64);
		return true;
	}
	
	public void render(int x, int y, int l, Map map, Screen screen) {
		screen.drawTile(x-1, y-1, 0, 0, sprite, 0);
	}
}
