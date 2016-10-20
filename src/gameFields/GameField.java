package gameFields;

import java.awt.Point;

import dataUtils.PArea;
import gfx.SpriteSheet;
import main.MainConfig.GameFields;
import main.Game;
import main.MainConfig;

public abstract class GameField {
	protected PArea field;
	protected PArea fieldTop;
	private Point grab;
	private boolean grabing;
	private GameFields savefile;
	private SpriteSheet background;
	
	public GameField(GameFields savefile) {
		Point p = MainConfig.fieldPos.get(savefile);
		field = new PArea(p.x,p.y,100,32);
		fieldTop = new PArea(p.x,p.y,100,32);
		grab = new Point();
		this.savefile = savefile;
	}
	
	public GameField(int w, int h, GameFields savefile){
		Point p = MainConfig.fieldPos.get(savefile);
		field = new PArea(p.x,p.y,w,h);
		fieldTop = new PArea(p.x,p.y,w,32);
		grab = new Point();
		this.savefile = savefile;
		constructBackground();
	}
	
	/**
	 * sets the size of the bottom field (the top field will only change his width but always have the same height -> 32)
	 * @param width
	 * @param height
	 */
	protected void setSize(int width, int height) {
		field.setSize(width, height+32);
		fieldTop.setSize(width, 32);
		constructBackground();
	}
	
	private void constructBackground() {
		int[] pixels = new int[field.width * field.height];
		for(int x = 0; x < field.width; x++){
			for(int y = 0; y < field.height; y++){
				pixels[y*field.width+x] = y < (fieldTop.height)?
						((x < 2 || y < 2 || x >= fieldTop.width-2 || y >= fieldTop.height-2)? 0xff404040 : 0xff808080):
						((x < 2 || y < 2 || x >= field.width-2 || y >= field.height-2)? 0x80404040 : 0x40808080);
			}
		}
		this.background = new SpriteSheet();
		this.background.setPixels(pixels, field.width, field.height, field.width, field.height);
	}
	
	public abstract void tick();
	
	public boolean Drag(){
		if(fieldTop.contains(Game.input.mouse.x, Game.input.mouse.y)){
			if(Game.input.mousel.click()){
				grab.x = Game.input.mousel.x-field.x;
				grab.y = Game.input.mousel.y-field.y;
				grabing = true;
			}
		}
		if(grabing & Game.input.mousel.isPressed()){
			field.x = Game.input.mouse.x - grab.x;
			field.y = Game.input.mouse.y - grab.y;
			if(field.x < 0)field.x = 0;
			if(field.x > Game.screen.width-field.width)field.x = Game.screen.width-field.width;
			if(field.y < 0)field.y = 0;
			if(field.y > Game.screen.height-field.height)field.y = Game.screen.height-field.height;
			fieldTop.x = field.x;
			fieldTop.y = field.y;
			return true;
		}else{
			if(grabing) {
				grabing = false;
				save();
			}
			if(field.x < 0)field.x = 0;
			if(field.x > Game.screen.width-field.width)field.x = Game.screen.width-field.width;
			if(field.y < 0)field.y = 0;
			if(field.y > Game.screen.height-field.height)field.y = Game.screen.height-field.height;
			fieldTop.x = field.x;
			fieldTop.y = field.y;
			return false;
		}
	}
	
	public abstract void render();
	
	protected void renderfield(){
		Game.screen.drawTileOGL(field.x, field.y, 0, background);
//		Game.screen.drawGUITile(field.x, field.y, 0, 0, background, 0);
	}
	
	public void save(){
		MainConfig.fieldPos.put(savefile,new Point(field.x, field.y));
	}

	public boolean mouseover(int mousex, int mousey){
		return field.contains(mousex, mousey);
	}
}
