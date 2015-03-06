package GameFields;

import java.awt.Point;

import Main.Game;

import Main.PArea;

public abstract class GameField {
	public PArea field;
	public PArea fieldTop;
	public Point grab;
	public boolean grabing;
	public Game game;
	public int savefile;
	
	
	public GameField(int w, int h, Game game, int savefile){
		int x = Game.configs.FieldPosX[savefile];
		int y = Game.configs.FieldPosY[savefile];
		field = new PArea(x,y,w,h);
		fieldTop = new PArea(x,y,w,10);
		grab = new Point();
		this.savefile = savefile;
		this.game = game;
	}
	
	public abstract void tick();
	
	public void Drag(){
		if(fieldTop.contains(game.input.mouse.x/Game.SCALE, game.input.mouse.y/Game.SCALE)){
			if(game.input.mousel.click()){
				grab.x = game.input.mousel.x/Game.SCALE-field.x;
				grab.y = game.input.mousel.y/Game.SCALE-field.y;
				grabing = true;
			}
		}
		if(grabing & game.input.mousel.isPressed()){
			field.x = game.input.mouse.x/Game.SCALE - grab.x;
			field.y = game.input.mouse.y/Game.SCALE - grab.y;
			if(field.x < 0)field.x = 0;
			if(field.x > game.screen.width-field.width)field.x = game.screen.width-field.width;
			if(field.y < 0)field.y = 0;
			if(field.y > game.screen.height-field.height)field.y = game.screen.height-field.height;
			fieldTop.x = field.x;
			fieldTop.y = field.y;
		}else{
			grabing = false;
			save();
		}
	}
	
	public abstract void render();
	
	protected void renderfield(){
		for(int x = 0; x < field.width; x++){
			for(int y = 0; y < field.height; y++){
				if(y < fieldTop.height){
					if(x == 0 | y == 0 | x == fieldTop.width-1 | y == fieldTop.height-1)game.screen.renderGUIScaled(game.screen.xOffset+field.x+x, game.screen.yOffset+field.y+y, 0xff404040);
					else game.screen.renderGUIScaled(game.screen.xOffset+field.x+x, game.screen.yOffset+field.y+y, 0xff808080);
				}else{
					if(x == 0 | y == 0 | x == field.width-1 | y == field.height-1)game.screen.renderGUIScaled(game.screen.xOffset+field.x+x, game.screen.yOffset+field.y+y, 0x80404040);
					else game.screen.renderGUIScaled(game.screen.xOffset+field.x+x, game.screen.yOffset+field.y+y, 0x40808080);
				}
			}
		}
	}
	
	public void save(){
		Game.configs.FieldPosX[savefile] = field.x;
		Game.configs.FieldPosY[savefile] = field.y;
		Game.configs.save();
	}

	public boolean mouseover(int mousex, int mousey){
		return field.contains(mousex, mousey);
	}
}
