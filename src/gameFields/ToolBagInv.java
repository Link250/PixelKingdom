package gameFields;

import gfx.Mouse;
import gfx.SpriteSheet;
import item.*;
import main.Game;
import main.PArea;

public class ToolBagInv extends GameField {

	private SpriteSheet Background = new SpriteSheet("/Items/field.png");
	public int savefile;
	public static int width = 6;

	public ToolBag bag;
	
	public ToolBagInv(ToolBag bag, Game game, int savefile) {
		super(width*12, bag.inventory.length/width*12+11, game, savefile);
		this.bag = bag;
		this.savefile = savefile;
	}
	
	public void tick() {
		Drag();
		if(mouseover(game.input.mouse.x/Game.SCALE, game.input.mouse.y/Game.SCALE)){
			Mouse.mousetype=0;
			if(game.input.mousel.click()){
				int mx = game.input.mousel.x/Game.SCALE, my = game.input.mousel.y/Game.SCALE;
				PArea r = new PArea(0,0,10,10);
				for(int x = 0; x < width; x++){
					for(int y = 0; y < bag.inventory.length/width; y++){
						r.setBounds(x*12+1+field.x,y*12+12+field.y,10,10);
						if(r.contains(mx, my)){
							if(bag.inventory[x+y*width]!=null){
								if(Mouse.Item==null){
									Mouse.Item = bag.inventory[x+y*width]; bag.inventory[x+y*width] = null;
								}
							}else{
								try{
									bag.inventory[x+y*width]=(Tool)Mouse.Item;Mouse.Item = null;
								}catch(ClassCastException e){}
							}
						}
					}
				}
			}
		}
	}
	
	public void render() {
		renderfield();
		Game.sfont.render(game.screen.xOffset+field.x+2, game.screen.yOffset+field.y+1, "Tools", 0, 0xff000000, game.screen);
		for(int x = 0; x < width; x++){
			for(int y = 0; y < bag.inventory.length/width; y++){
				game.screen.drawGUITile(game.screen.xOffset+field.x+x*12, game.screen.yOffset+field.y+y*12+11, 0, 0, Background, 0xff000000);
				if(bag.inventory[x+y*width]!=null) bag.inventory[x+y*width].render(game.screen, game.screen.xOffset+field.x+x*12+1, game.screen.yOffset+field.y+y*12+12,true);
			}
		}
	}
}
