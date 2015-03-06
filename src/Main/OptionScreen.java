package Main;

import ItemList.MatStack;
import Main.PArea;
import gfx.SpriteSheet;
import Main.Button;
import Main.Game;

public class OptionScreen {
	
	private Game game;
	private Button  back;
	
	private Button Stacktype;
	private MatStack mat, matf, mate, matm;
	private SpriteSheet itemback;
	
	public OptionScreen(Game game) {
		this.game = game;
		back = new Button(10, 10, 20, 20, game.screen, game.input);
		back.gfxData(new SpriteSheet("/Buttons/back.png"), true);
		
		Stacktype = new Button(Game.WIDTH/Game.SCALE/2, 80, 70, 20, game.screen, game.input);
		mat = new MatStack();
		mate = new MatStack();
		matm = new MatStack();matm.addStack(511);
		matf = new MatStack();matf.addStack(1023);
		itemback = new SpriteSheet("/Items/field.png");
	}
	
	public void tick(){
		back.tick();
		if(back.isclicked){
			Game.menu=0;
		}
		if(game.input.mousel.isPressed()){
			int x = game.input.mouse.x/Game.SCALE;
			int y = game.input.mouse.y/Game.SCALE;
			if(new PArea(Game.WIDTH/Game.SCALE/2,50,128,6).contains(x, y)){
				Game.configs.PlrCol = ((Game.configs.PlrCol&0xff00ffff) + ((x-Game.WIDTH/Game.SCALE/2)*2<<16)) | 0xff000000;
				Game.configs.save();
			}
			if(new PArea(Game.WIDTH/Game.SCALE/2,57,128,6).contains(x, y)){
				Game.configs.PlrCol = ((Game.configs.PlrCol&0xffff00ff) + ((x-Game.WIDTH/Game.SCALE/2)*2<<8)) | 0xff000000;
				Game.configs.save();
			}
			if(new PArea(Game.WIDTH/Game.SCALE/2,64,128,6).contains(x, y)){
				Game.configs.PlrCol = ((Game.configs.PlrCol&0xffffff00) + ((x-Game.WIDTH/Game.SCALE/2)*2)) | 0xff000000;
				Game.configs.save();
			}
		}
		
		Stacktype.tick();
		mat.addStack(1);
		if(mat.getStack() == 1024)mat = new MatStack();
		if(Stacktype.isclicked){
			Game.configs.stacktype++;
			if(Game.configs.stacktype > 3)Game.configs.stacktype=1;
			Game.configs.save();
		}
	}
	
	public void render(){
		back.render();
		Game.font.render(game.screen.width/Game.SCALE/2-30, 10, "Options", 0, 0xff000000, game.screen);
		
		/*		PLAYER COLOR	*/
		Game.font.render(10, 50, "PlayerColor", 0, 0xff000000, game.screen);
		for(int c = -2; c < 258; c+=2){
			for(int i = 0; i < 6; i++){
				if(i==0|i==5|c<0|c>255){
					game.screen.renderPixelScaled(Game.WIDTH/Game.SCALE/2+c/2, 50+i, 0xff404040);
					game.screen.renderPixelScaled(Game.WIDTH/Game.SCALE/2+c/2, 57+i, 0xff404040);
					game.screen.renderPixelScaled(Game.WIDTH/Game.SCALE/2+c/2, 64+i, 0xff404040);
				}else{
					game.screen.renderPixelScaled(Game.WIDTH/Game.SCALE/2+c/2, 50+i, 0xff000000|(c<<16));
					game.screen.renderPixelScaled(Game.WIDTH/Game.SCALE/2+c/2, 57+i, 0xff000000|(c<<8));
					game.screen.renderPixelScaled(Game.WIDTH/Game.SCALE/2+c/2, 64+i, 0xff000000|c);
				}
			}
		}
		for(int i = 0; i < 6; i++){
			int c = (Game.configs.PlrCol&0x00ff0000)>>16;
			game.screen.renderPixelScaled(Game.WIDTH/Game.SCALE/2+c/2, 50+i, 0xff000000 + ((255-c)<<16) + ((255-c)<<8) + (255-c));
			c = (Game.configs.PlrCol&0x0000ff00)>>8;
			game.screen.renderPixelScaled(Game.WIDTH/Game.SCALE/2+c/2, 57+i, 0xff000000 + ((255-c)<<16) + ((255-c)<<8) + (255-c));
			c = Game.configs.PlrCol&0x000000ff;
			game.screen.renderPixelScaled(Game.WIDTH/Game.SCALE/2+c/2, 64+i, 0xff000000 + ((255-c)<<16) + ((255-c)<<8) + (255-c));
		}
		for(int x = 0; x < 12; x++){
			for(int y = 0; y < 12; y++){
				if(x == 0 | x == 11 | y == 0 | y == 11){
					game.screen.renderPixelScaled(x+Game.WIDTH/Game.SCALE/2+132, y+54, 0xff404040);
				}else{
					game.screen.renderPixelScaled(x+Game.WIDTH/Game.SCALE/2+132, y+54, Game.configs.PlrCol);
				}
			}
		}
		
		/*		STACK TYPE	*/
		Game.font.render(10, 80, "Stack Type", 0, 0xff000000, game.screen);
		switch(Game.configs.stacktype){
		case 1:Stacktype.TextData("Numbers", false, 0, 0);break;
		case 2:Stacktype.TextData("Colors", false, 0, 0);break;
		case 3:Stacktype.TextData("Binary", false, 0, 0);break;
		}
		Stacktype.render();
		game.screen.renderTile(Game.WIDTH/Game.SCALE/2+80, 83, 0, 0, itemback, 0);
		mate.render(game.screen, Game.WIDTH/Game.SCALE/2+81, 84, true);
		game.screen.renderTile(Game.WIDTH/Game.SCALE/2+100, 83, 0, 0, itemback, 0);
		matm.render(game.screen, Game.WIDTH/Game.SCALE/2+101, 84, true);
		game.screen.renderTile(Game.WIDTH/Game.SCALE/2+120, 83, 0, 0, itemback, 0);
		matf.render(game.screen, Game.WIDTH/Game.SCALE/2+121, 84, true);
		game.screen.renderTile(Game.WIDTH/Game.SCALE/2+140, 83, 0, 0, itemback, 0);
		mat.render(game.screen, Game.WIDTH/Game.SCALE/2+141, 84, true);
		
	}
}
