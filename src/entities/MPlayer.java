package entities;

import gfx.SpriteSheet;
import Main.Game;
import Maps.Map;

public class MPlayer extends Mob {

	private Game game;
	public int number;

	public MPlayer(Map map, Game game, int n) {
		super(map ,"MPlayer", 0, 0, new SpriteSheet("/sprite_sheet_player.png"));
		this.game = game;
		number = n;
		xOffset=6;
		yOffset=9;
		sheet.tileWidth = 13*Game.SCALE;
		sheet.tileHeight = 16*Game.SCALE;
	}

	public void tick(int numTick) {
		
	}
	
	public void render() {
		game.screen.renderTile(x-xOffset, y-yOffset, 0, movingDir*16, sheet, 0xffff00ff);
	}
}
