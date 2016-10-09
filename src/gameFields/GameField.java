package gameFields;

import java.awt.Point;

import gfx.SpriteSheet;
import main.MainConfig.GameFields;
import main.Game;
import main.MainConfig;
import main.PArea;

public abstract class GameField {
	protected PArea field;
	protected PArea fieldTop;
	private Point grab;
	private boolean grabing;
	private GameFields savefile;
	private SpriteSheet background;
	
	public GameField(GameFields savefile) {
		Point p = MainConfig.fieldPos.get(savefile);
		field = new PArea(p.x,p.y,30,10);
		fieldTop = new PArea(p.x,p.y,30,10);
		grab = new Point();
		this.savefile = savefile;
	}
	
	public GameField(int w, int h, GameFields savefile){
		Point p = MainConfig.fieldPos.get(savefile);
		field = new PArea(p.x,p.y,w,h);
		fieldTop = new PArea(p.x,p.y,w,10);
		grab = new Point();
		this.savefile = savefile;
		constructBackground();
	}
	
	protected void setSize(int width, int height) {
		field.setSize(width, height);
		fieldTop.setSize(width, 10);
		constructBackground();
	}
	
	private void constructBackground() {
		int[] pixelscaled = new int[field.width * field.height * 9];
		int[] pixels = new int[field.width * field.height];
		this.background = new SpriteSheet();
		this.background.setPixels(pixelscaled, field.width*3, field.height*3, field.width*3, field.height*3);
		for(int x = 0; x < field.width; x++){
			for(int y = 0; y < field.height; y++){
				pixels[y*field.width+x] = y < (fieldTop.height)?
						((x == 0 || y == 0 || x == fieldTop.width-1 || y == fieldTop.height-1)? 0xff404040 : 0xff808080):
						((x == 0 || y == 0 || x == field.width-1 || y == field.height-1)? 0x80404040 : 0x40808080);
			}
		}
		for(int x = 0; x < field.width*3; x++){
			for(int y = 0; y < field.height*3; y++){
				pixelscaled[y*field.width*3+x] = pixels[(y/3)*field.width+x/3];
			}
		}
	}
	
	public abstract void tick();
	
	public void Drag(){
		if(fieldTop.contains(Game.input.mouse.x/Game.SCALE, Game.input.mouse.y/Game.SCALE)){
			if(Game.input.mousel.click()){
				grab.x = Game.input.mousel.x/Game.SCALE-field.x;
				grab.y = Game.input.mousel.y/Game.SCALE-field.y;
				grabing = true;
			}
		}
		if(grabing & Game.input.mousel.isPressed()){
			field.x = Game.input.mouse.x/Game.SCALE - grab.x;
			field.y = Game.input.mouse.y/Game.SCALE - grab.y;
			if(field.x < 0)field.x = 0;
			if(field.x > Game.screen.width-field.width)field.x = Game.screen.width-field.width;
			if(field.y < 0)field.y = 0;
			if(field.y > Game.screen.height-field.height)field.y = Game.screen.height-field.height;
			fieldTop.x = field.x;
			fieldTop.y = field.y;
		}else{
			grabing = false;
			save();
		}
	}
	
	public abstract void render();
	
	protected void renderfield(){
//		long t = System.nanoTime();
		Game.screen.drawGUITile(field.x+Game.screen.xOffset, field.y+Game.screen.yOffset, 0, 0, background, 0);
//		for(int x = 0; x < field.width; x++){
//			for(int y = 0; y < field.height; y++){
//				if(y < fieldTop.height){
//					if(x == 0 | y == 0 | x == fieldTop.width-1 | y == fieldTop.height-1)Game.screen.drawGUIScaled(Game.screen.xOffset+field.x+x, Game.screen.yOffset+field.y+y, 0xff404040);
//					else Game.screen.drawGUIScaled(Game.screen.xOffset+field.x+x, Game.screen.yOffset+field.y+y, 0xff808080);
//				}else{
//					if(x == 0 | y == 0 | x == field.width-1 | y == field.height-1)Game.screen.drawGUIScaled(Game.screen.xOffset+field.x+x, Game.screen.yOffset+field.y+y, 0x80404040);
//					else Game.screen.drawGUIScaled(Game.screen.xOffset+field.x+x, Game.screen.yOffset+field.y+y, 0x40808080);
//				}
//			}
//		}
//		System.out.println(System.nanoTime()-t);
	}
	
	public void save(){
		MainConfig.fieldPos.put(savefile,new Point(field.x, field.y));
	}

	public boolean mouseover(int mousex, int mousey){
		return field.contains(mousex, mousey);
	}
}
