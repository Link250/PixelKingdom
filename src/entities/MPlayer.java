package entities;

import gfx.Screen;
import gfx.SpriteSheet;
import map.Map;

public class MPlayer extends Mob {

	public int number;
	public int color;
	public int anim;

	public MPlayer(int n) {
		super(null ,"MPlayer", 0, 0, null);
		this.number = n;
		this.color = 0xffff00ff;
		this.anim = 0;
		this.xOffset=6;
		this.yOffset=9;
	}
	public void setColor(int col) {color=col;}
	public void setDir(int dir) {movingDir=dir;}
	
	public byte getDir() {return (byte) movingDir;}
	public byte getAnim() {return (byte) anim;}

	public void tick(int numTick) {}
	
	public void render() {
		if(this.sheet == null)this.sheet = new SpriteSheet("/sprite_sheet_player.png", 13*Screen.MAP_SCALE, 16*Screen.MAP_SCALE);
		Screen.drawMapSprite(x-xOffset, y-yOffset, sheet, anim, movingDir==1, false, color);
	}
}
