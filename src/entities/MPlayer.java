package entities;

import gfx.SpriteSheet;
import main.Game;

public class MPlayer extends Mob {

	public int number;
	public int color;
	public int anim;

	public MPlayer(int n) {
		super(null ,"MPlayer", 0, 0, new SpriteSheet("/sprite_sheet_player.png"));
		this.number = n;
		this.color = 0xffff00ff;
		this.anim = 0;
		this.xOffset=6;
		this.yOffset=9;
		this.sheet.tileWidth = 13*Game.SCALE;
		this.sheet.tileHeight = 16*Game.SCALE;
	}
	public void setColor(int col) {color=col;}
	public void setDir(int dir) {movingDir=dir;}
	
	public byte getDir() {return (byte) movingDir;}
	public byte getAnim() {return (byte) anim;}

	public void tick(int numTick) {}
	
	public void render() {
		Game.screen.drawTile(x-xOffset, y-yOffset, anim, movingDir*16, sheet, color);
	}
}
