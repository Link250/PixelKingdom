package entities;

import gfx.SpriteSheet;
import main.Game;
import map.Map;

public class MPlayer extends Mob {

	private Game game;
	public int number;
	public int color;
	public int anim;

	public MPlayer(Map map, Game game, int n) {
		super(map ,"MPlayer", 0, 0, new SpriteSheet("/sprite_sheet_player.png"));
		this.game = game;
		number = n;
		color = 0xffff00ff;
		anim = 0;
		xOffset=6;
		yOffset=9;
		sheet.tileWidth = 13*Game.SCALE;
		sheet.tileHeight = 16*Game.SCALE;
	}
	
	public void setDir(int dir) {movingDir=dir;}
	public void setColor(int col) {color=col;}

	public void tick(int numTick) {
		
	}
	
	public void render() {
		game.screen.drawTile(x-xOffset, y-yOffset, anim, movingDir*16, sheet, color);
	}
}
